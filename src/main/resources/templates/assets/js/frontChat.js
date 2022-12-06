let frontChat = (function () {

    let userId;
    let userName;
    let userLastname;
    let body;
    let socket;

    function init(paramUser, paramUserName, paramUserLastname) {
        userId = paramUser;
        userName = paramUserName;
        userLastname = paramUserLastname;
        body = $('body');

        body.on('click','.element-survole', function() {
            let groupId = $(this).attr('data-group-id');
            frontChat.sendConv(groupId);
        });

    }

    let userMessagePrime;
    let messagePrime;
    let userNamePrime;
    let navBarPrime;
    let messNumber = 0;
    function clone(){
        userMessagePrime = document.getElementById('user-message').cloneNode(true);
        messagePrime = document.getElementById('message').cloneNode(true);
        userNamePrime = document.getElementById('user-name').cloneNode(true);
        navBarPrime = document.getElementById('progress').cloneNode(true);
        messNumber++;
    }

    function sendConv(groupId) {
        let url = "/chat/front/render";
        let data = new FormData();
        data.append('groupId', groupId);
        $.ajax({
            url: url,
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (php_script_response) {
                if (php_script_response.success === true) {
                    if (document.getElementById('Chat' + groupId) === null) {
                        document.getElementById("Chat").innerHTML += php_script_response.view;
                        scrollDown(groupId);
                    }

                }
            }
        })
    }

    let sendMessage;
    let writeMessage;
    let deleteMessage;
    function initWebSocket(){

        // CONNEXION AU WEBSOCKET A L'OUVERTURE DE LA PAGE
        socket = new WebSocket("ws://127.0.0.1:3001");
        socket.addEventListener('open', function () {
            console.log("CONNECTED");
            connectMessage();
        });
        socket.addEventListener('close', function () {
            console.log("DISCONNECTED"+ socket.onerror);
        });


        // MESSAGE QUAND UN UTILISATEUR SE CONNECTE
        // RENVOIE LA REPONSE AU WEBSOCKET
        connectMessage = function () {
            const message = {
                messUserId: userId,
                message: "CONNECTED",
            };
            socket.send(JSON.stringify(message));
        }
        setInterval(connectMessage, 58000);

        // MESSAGE D'UN UTILISATEUR
        // RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
        sendMessage = function (groupId) {
            const message = {
                messUserId: userId,
                messGroupId: groupId,
                messUserName: userName,
                messUserLastname: userLastname,
                message: document.getElementById("Message" + groupId).value,
            };
            socket.send(JSON.stringify(message));
            addMessage(message.messUserId, message.messGroupId, message.message, message.messUserName, message.messUserLastname);
            scrollDown(groupId);
        }

        // MESSAGE QUAND UN UTILISATEUR ECRIT UN MESSAGE
        // RENVOIE LA REPONSE AU WEBSOCKET
        writeMessage = function (groupId) {
            const message = {
                messUserId: userId,
                messGroupId: groupId,
                messUserName: userName,
                messUserLastname: userLastname,
                message: "...",
            };
            socket.send(JSON.stringify(message));
        }

        // MESSAGE QUAND UN UTILISATEUR ANNULE SONT MESSAGE
        // RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
        deleteMessage = function (groupId) {
            const message = {
                messUserId: userId,
                messGroupId: groupId,
                messUserName: userName,
                messUserLastname: userLastname,
                message: "delete",
            };
            socket.send(JSON.stringify(message));
        }

        // ECOUTE LE WEBSOCKET POUR TRAITER LES MESSAGES RECU PAR UN AUTRE UTILISATEUR SEULEMENT
        socket.addEventListener("message", function (e) {
            try {
                const message = JSON.parse(e.data);
                addMessage(message.messUserId, message.messGroupId, message.message, message.messUserName, message.messUserLastname, message.messFilename, message.messSize);
                scrollDown(message.messGroupId);
            } catch (e) {

            }
        });
    }

    function initForm(){
        // ECOUTE LA TEXTAREA POUR TRAITER LES MESSAGE
        body.on('keyup', '.message', function (event){
            let textArea = $(this);
            let groupId = textArea.attr('data-group');

            if (event.key === "Enter") {
                sendMessage(groupId);
                document.getElementById("Message" + groupId).value = ""
            } else if (event.key === "Backspace") {
                deleteMessage(groupId);
            } else {
                writeMessage(groupId);
            }
        });

        //ECOUTE DE L'INPUT DE FICHIER
        body.on('change', "input.form_upload_file", function (e) {
             let inputFile = $(this),
                 form = inputFile.closest('form'),
                 groupId = form.attr('data-group'),
                 url = form.attr('action');

            if(inputFile.length > 0)
            {
                let input = inputFile[0],
                    files = input.files;

                if(files.length > 0){
                    for(let i=0;i<files.length;i++){
                        sendFile(files[i], groupId, url, form);
                    }
                }
            }
        });
        window.addEventListener("drop", handleDrop, false);

        // ECOUTE LE BOUTON POUR FERMER LA FENETRE
        body.on('click', '.close', function () {
            let cross = $(this);
            let groupId = cross.attr('data-group');
            if (document.getElementById("remove" + groupId) !== null){
                let Chat = document.getElementById("remove" + groupId);
                Chat.parentNode.removeChild(Chat);
            }
        })
    }

    // FONCTION POUR AUTOSCOLL A L'AJOUT D'UN MESSAGE
    function scrollDown(groupId) {
        let chatBox = document.getElementById('chatBox' + groupId);
        setTimeout(chatBox.scrollTop = chatBox.scrollHeight, 1000);
    }

    // RECUPERE LES FICHIERS DANS LA DROP ZONE
    function handleDrop(e) {
        e.preventDefault();
        let inputFile = $("input.form_upload_file"),
            form = inputFile.closest('form'),
            groupId = form.attr('data-group'),
            url = form.attr('action');


        let dt = e.dataTransfer,
            files = dt.files;

        if(files.length > 0){
            for(let i=0;i<files.length;i++){
                sendFile(files[i], groupId, url, form);
            }
        }

    }

    // ENVOIE LES FICHIER A PHP ET RENVOIE LA REPONSE AU WEBSOCKET AINSI QU'A L'UTILISATEUR ACTUELLE
    function sendFile(file, groupId, url, jqueryForm) {
        let form = jqueryForm[0],
            data = new FormData(form);
            data.append('file', file);
        $.ajax({
            xhr: function() {
                let xhr = new window.XMLHttpRequest();
                document.getElementById("ChatInfo" + groupId).appendChild(navBarPrime);
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
                document.getElementById("ChatInfo" + groupId).innerHTML='';
                alert('Problème lors du transfert :\nFichier accepté : Documents Texte, Images, Archives\nMax : 50Mo');
            },
            success: function (php_script_response) {
                if (php_script_response.fileSuccess === true) {
                    document.getElementById("ChatInfo" + groupId).innerHTML='';
                    const message = {
                        messUserId: userId,
                        messGroupId: groupId,
                        messUserName: userName,
                        messUserLastname: userLastname,
                        message: php_script_response.message,
                        messSize: php_script_response.size,
                        messFilename: php_script_response.filename,
                    };
                    socket.send(JSON.stringify(message));
                    addMessage(message.messUserId, message.messGroupId, message.message, message.messUserName, message.messUserLastname, message.messFilename, message.messSize);
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
        if (message === '...' && messUserId !== userId) {
            const messageHTML = messUserName + " " + messUserLastname + " est en train d'écrire...";
            document.getElementById("ChatInfo" + messGroupId).innerHTML = messageHTML;
        } else if (message === 'delete' || message === '...') {
            document.getElementById("ChatInfo" + messGroupId).innerHTML = "";
        } else {
            document.getElementById("ChatInfo" + messGroupId).innerHTML = "";
            if (messUserId === userId) {
                if (message.substring(0, 5) === 'file:') {
                    if (message.substring(message.lastIndexOf('.') + 1) === 'jpg' || message.substring(message.lastIndexOf('.') + 1) === 'png' || message.substring(message.lastIndexOf('.') + 1) === 'jpeg' || message.substring(message.lastIndexOf('.') + 1) === 'gif') {
                        userMessagePrime.id = messNumber;
                        const messageHTML ="<img src='" + message.substring(5) + "' class='rtext' alt='Size "+ messSize +"'/>";
                        document.getElementById("chatBox" + messGroupId).appendChild(userMessagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    } else {
                        userMessagePrime.id = messNumber;
                        const messageHTML = "<a href='" + message.substring(5) + "' download='' class='a' >Télécharger <i class='fas fa-download'></i><br>"+messFilename+"<br>"+messSize+" octet</a>";
                        document.getElementById("chatBox" + messGroupId).appendChild(userMessagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }
                } else {
                    userMessagePrime.id = messNumber;
                    document.getElementById("chatBox" + messGroupId).appendChild(userMessagePrime).innerHTML = message;
                    document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                }
            } else {
                if (message.substring(0, 5) === 'file:') {
                    if (message.substring(message.lastIndexOf('.') + 1) === 'jpg' || message.substring(message.lastIndexOf('.') + 1) === 'png' || message.substring(message.lastIndexOf('.') + 1) === 'jpeg' || message.substring(message.lastIndexOf('.') + 1) === 'gif') {
                        messagePrime.id = messNumber;
                        const messageHTML = "<img src='" + message.substring(5) + "' class='ltext' alt='Size "+ messSize +" />";
                        document.getElementById("chatBox" + messGroupId).appendChild(messagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    } else {
                        messagePrime.id = messNumber;
                        const messageHTML = "<a href='" + message.substring(5) + "' download='' class='a' >Télécharger <i class='fas fa-download'></i><br>"+messFilename+"<br>"+messSize+" octet</a>";
                        document.getElementById("chatBox" + messGroupId).appendChild(messagePrime).innerHTML = messageHTML;
                        document.getElementById(messNumber).appendChild(userNamePrime).innerHTML = messUserName + " " + messUserLastname;
                    }
                } else {
                    messagePrime.id = messNumber;
                    document.getElementById("chatBox" + messGroupId).appendChild(messagePrime).innerHTML = message;
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
        sendConv: sendConv,
        initForm: initForm,
        clone: clone,
    }

}());