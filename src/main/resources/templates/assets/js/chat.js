let Chat = (function () {

    let User;
    let Conversation;
    let UserName;
    let UserLastname;
    let Route = "https://127.0.0.1:8000/";
    // CONNEXION AU WEBSOCKET A L'OUVERTURE DE LA PAGE
    let socket = new WebSocket("ws://127.0.0.1:3001");
    socket.addEventListener('open', function () {
        console.log("CONNECTED");
    });

    // PARAMETRE DU SCRIPT
    function init(ParamUser, ParamConversation, ParamUserName, ParamUserLastname) {
        User = ParamUser;
        Conversation = ParamConversation;
        UserName = ParamUserName;
        UserLastname = ParamUserLastname;
    }

    let userMessagePrime;
    let messagePrime;
    let userNamePrime;
    let navBarPrime;
    let playButtonPrime;
    let messNumber = 0;
    function clone(){
        userMessagePrime = document.getElementById('user-message').cloneNode(true);
        messagePrime = document.getElementById('message').cloneNode(true);
        userNamePrime = document.getElementById('user-name').cloneNode(true);
        navBarPrime = document.getElementById('progress').cloneNode(true);
        playButtonPrime = document.getElementById('playButton').cloneNode(true);
        messNumber++;
    }

    // FONCTION POUR AUTOSCOLL A L'AJOUT D'UN MESSAGE
    const scrollDown = function () {
        let chatBox = document.getElementById('chatBox');
        chatBox.scrollTop = chatBox.scrollHeight;
    }
    scrollDown();

    let sendMessage;
    let writeMessage;
    let deleteMessage;
    function initWebSocket() {

        // ECOUTE LE WEBSOCKET POUR TRAITER LES MESSAGES RECU PAR UN AUTRE UTILISATEUR SEULEMENT
        socket.addEventListener("message", function (e) {
            try {
                const message = JSON.parse(e.data);
                addMessage(message.UserId, message.ConversationId, message.Message, message.UserName, message.UserLastname);
                scrollDown();
            } catch (e) {
                // Catch any errors
            }
        });

        // MESSAGE D'UN UTILISATEUR
        // RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
        sendMessage = function () {
            const message = {
                UserId: User,
                ConversationId: Conversation,
                UserName: UserName,
                UserLastname: UserLastname,
                Message: document.getElementById("Message").value
            };
            socket.send(JSON.stringify(message));
            addMessage(message.UserId, message.ConversationId, message.Message, message.UserName, message.UserLastname);
            scrollDown();
        }

        // MESSAGE QUAND UN UTILISATEUR ECRIT UN MESSAGE
        // RENVOIE LA REPONSE AU WEBSOCKET
        writeMessage = function () {
            const message = {
                UserId: User,
                ConversationId: Conversation,
                UserName: UserName,
                UserLastname: UserLastname,
                Message: "..."
            };
            socket.send(JSON.stringify(message));
        }

        // MESSAGE QUAND UN UTILISATEUR ANNULE SONT MESSAGE
        // RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
        deleteMessage = function () {
            const message = {
                    UserId: User,
                    ConversationId: Conversation,
                    UserName: UserName,
                    UserLastname: UserLastname,
                    Message: "delete"
                }
            ;
            socket.send(JSON.stringify(message));
        }

    }

    function initForm() {

        // ECOUTE LE BOUTON POUR TRAITER LES MESSAGE
        document.getElementById("sendBtn").addEventListener("click", function () {
            sendMessage();
            document.getElementById("Message").value = ""
        });

        // ECOUTE LA TEXTAREA POUR TRAITER LES MESSAGE
        document.getElementById("Message").addEventListener("keyup", function (event) {
            if (event.key === "Enter") {
                sendMessage();
                document.getElementById("Message").value = ""
            } else if (event.key === "Backspace") {
                deleteMessage();
            } else {
                writeMessage();
            }

        });

        // ECOUTE LES CHANGEMENTS DE L'INPUT FILE OU DE LA DROP ZONE
        document.getElementById("form_upload_add_file").addEventListener("change", function () {
            const files_data = $('#form_upload_add_file').prop('files');
            handleFiles(files_data)
        }, false)
        window.addEventListener("drop", handleDrop, false);

    }

    // RECUPERE LES FICHIERS DANS LA DROP ZONE
    function handleDrop(e) {
        e.preventDefault()
        let dt = e.dataTransfer
        let files = dt.files
        handleFiles(files)
    }

    // TRAITE SI IL Y A PLUSIEURS FICHIERS
    function handleFiles(files) {
        ([...files]).forEach(sendFile)
    }

    // ENVOIE LES FICHIER A PHP ET RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
    function sendFile(file) {
        const form_data = $('form[name="form_upload_add"]');
        let url = form_data.attr('action');
        let form = $('form')[0];
        let data = new FormData(form);
        data.append('file', file);
        console.log(file);
        $.ajax({
            xhr: function() {
                let xhr = new window.XMLHttpRequest();
                const messageHTML = "<div class='progress' style='width:200px'><div class='progress-bar' role='progressbar' style='width: 0%' aria-valuenow='0' aria-valuemin='0' aria-valuemax='100'></div></div>";
                document.getElementById("ChatInfo").innerHTML = messageHTML;
                xhr.upload.addEventListener("progress", function(evt) {
                    if (evt.lengthComputable) {
                        let percentComplete = ((evt.loaded / evt.total) * 100);
                        $(".progress-bar").width(percentComplete + '%');
                        $(".progress-bar").html(percentComplete+'%');
                    }
                }, false);
                return xhr;
            },
            url: url,
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            beforeSend: function(){
                $(".progress-bar").width('0%');
            },
            error:function(){
                document.getElementById("ChatInfo").innerHTML ='';
                alert('Problème lors du transfert :\nFichier accepté : Documents Texte, Images, Archives\nMax : 50Mo');
            },
            success: function (php_script_response) {
                if (php_script_response.fileSuccess === true) {
                    document.getElementById("ChatInfo").innerHTML ='';
                    const message = {
                        messUserId: User,
                        messConversationId: Conversation,
                        messUserName: UserName,
                        messUserLastname: UserLastname,
                        message: php_script_response.message,
                        messSize: php_script_response.size,
                        messFilename: php_script_response.filename,
                    };
                    console.log(php_script_response);
                    socket.send(JSON.stringify(message));
                    addMessage(message.messUserId, message.messConversationId, message.message, message.messUserName, message.messUserLastname, message.messFilename, message.messSize);
                } else {
                    alert(php_script_response.message);
                }

            }
        });
    }

    // TRIE POUR AJOUTER LES MESSAGES
    // AUTRE QUE L'UTILISATEUR ACTUELLE (UTILISATEUR QUI ECRIT, UTILISATEUR QUI EFFACE)
    // UTILISATEUR ACTUELLE (FICHIER IMAGE, FICHIER, MESSAGE)
    // AUTRE QUE L'UTILISATEUR ACTUELLE (FICHIER IMAGE, FICHIER, MESSAGE)
    function addMessage(messUserId, messGroupId, message, messUserName, messUserLastname, messFilename, messSize) {
        if (message === '...' && messUserId !== User) {
            const messageHTML = messUserName + " " + messUserLastname + " est en train d'écrire...";
            document.getElementById("ChatInfo").innerHTML = messageHTML;
        } else if (message === 'delete' || message === '...') {
            document.getElementById("ChatInfo").innerHTML = "";
        } else {
            document.getElementById("ChatInfo").innerHTML = "";
            if (messUserId === User) {
                if (message.substring(0, 5) === 'file:') {
                    if (message.substring(message.lastIndexOf('.') + 1) === 'jpg' || message.substring(message.lastIndexOf('.') + 1) === 'png' || message.substring(message.lastIndexOf('.') + 1) === 'jpeg' || message.substring(message.lastIndexOf('.') + 1) === 'gif') {
                        userMessagePrime.id = messNumber;
                        const messageHTML ="<img src='" + message.substring(5) + "' class='rtext' alt='Size "+ messSize +"'/>";
                        document.getElementById("chatBox").appendChild(userMessagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    } else if(message.substring(message.lastIndexOf('.') + 1) === 'wav'){
                        userMessagePrime.id = messNumber;
                        const messageHTML = "<audio id='"+ message +"' src='" + message.substring(5) + "' > Your browser does not support the <code>audio</code> element.</audio> <a onclick=\"document.getElementById('"+ message +"').play()\"><svg id=\"playButton\" xmlns=\"http://www.w3.org/2000/svg\" width=\"29\" height=\"29\" fill=\"currentColor\" class=\"bi bi-play-circle play\" viewBox=\"0 0 16 16\"><path d=\"M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z\"/><path d=\"M6.271 5.055a.5.5 0 0 1 .52.038l3.5 2.5a.5.5 0 0 1 0 .814l-3.5 2.5A.5.5 0 0 1 6 10.5v-5a.5.5 0 0 1 .271-.445z\"/></svg></a>";
                        document.getElementById("chatBox").appendChild(userMessagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }else {
                        userMessagePrime.id = messNumber;
                        const messageHTML = "<a href='" + message.substring(5) + "' download='' class='a' >Télécharger <i class='fas fa-download'></i><br>"+messFilename+"<br>"+messSize+" octet</a>";
                        document.getElementById("chatBox").appendChild(userMessagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }
                } else {
                    userMessagePrime.id = messNumber;
                    document.getElementById("chatBox").appendChild(userMessagePrime).innerHTML = message;
                    document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                }
            } else {
                if (message.substring(0, 5) === 'file:') {
                    if (message.substring(message.lastIndexOf('.') + 1) === 'jpg' || message.substring(message.lastIndexOf('.') + 1) === 'png' || message.substring(message.lastIndexOf('.') + 1) === 'jpeg' || message.substring(message.lastIndexOf('.') + 1) === 'gif') {
                        messagePrime.id = messNumber;
                        const messageHTML = "<img src='" + message.substring(5) + "' class='ltext' alt='Size "+ messSize +" />";
                        document.getElementById("chatBox").appendChild(messagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    } else if(message.substring(message.lastIndexOf('.') + 1) === 'wav') {
                        userMessagePrime.id = messNumber;
                        const messageHTML = "<audio id='"+ message +"' src='" + message.substring(5) + "' > Your browser does not support the <code>audio</code> element.</audio> <a onclick=\"document.getElementById('"+ message +"').play()\"><svg id=\"playButton\" xmlns=\"http://www.w3.org/2000/svg\" width=\"29\" height=\"29\" fill=\"currentColor\" class=\"bi bi-play-circle play\" viewBox=\"0 0 16 16\"><path d=\"M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z\"/><path d=\"M6.271 5.055a.5.5 0 0 1 .52.038l3.5 2.5a.5.5 0 0 1 0 .814l-3.5 2.5A.5.5 0 0 1 6 10.5v-5a.5.5 0 0 1 .271-.445z\"/></svg></a>";
                        document.getElementById("chatBox").appendChild(messagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }else {
                        messagePrime.id = messNumber;
                        const messageHTML = "<a href='" + message.substring(5) + "' download='' class='a' >Télécharger <i class='fas fa-download'></i><br>"+messFilename+"<br>"+messSize+" octet</a>";
                        document.getElementById("chatBox").appendChild(messagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }
                } else {
                    messagePrime.id = messNumber;
                    document.getElementById("chatBox").appendChild(messagePrime).innerHTML = message;
                    document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                }
            }
            clone();
            scrollDown(messGroupId);
        }
    }

    return {
        init: init,
        initWebSocket: initWebSocket,
        initForm: initForm,
        clone: clone,
        sendFile: sendFile,
    }

}());