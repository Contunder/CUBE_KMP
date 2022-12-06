let frontGroup = (function () {

    let userId;
    let userName;
    let userLastname;
    let body;

    function init(paramUser, paramUserName, paramUserLastname) {
        userId = paramUser;
        userName = paramUserName;
        userLastname = paramUserLastname;
        body = $('body');

        body.on('click','.newGroup', function() {
            newGroup();
        });

        body.on('click','.update', function() {
            let groupId = $(this).attr('data-group-id');
            editGroup(groupId);
        });

        body.on('click','.delete', function() {
            let groupId = $(this).attr('data-group-id');
            deleteGroup(groupId);
        });
    }

    function initGroup(){
        // ECOUTE LE BOUTON POUR FERMER LA FENETRE
        body.on('click', '.close', function () {
            if (document.getElementById("newGroup") !== null){
                let group = document.getElementById("newGroup");
                group.parentNode.removeChild(group);
            }
        })

        // ECOUTE LES CHANGEMENTS DE L'INPUT FILE

        body.on('click', ".createGroup", function () {

            let inputFile = $('input.formGroup'),
                form = inputFile.closest('form'),
                url = form.attr('action');

            createGroup(url, form);
        });

    }

    function newGroup() {
        let url = "/chat/front/group";
        let data = new FormData();
        $.ajax({
            url: url,
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (php_script_response) {
                if (php_script_response.groupSuccess === true) {
                    if (document.getElementById('newGroup') === null) {
                        document.getElementById("Chat").innerHTML += php_script_response.view;
                        document.getElementById("form_new_group_add_user_to_groups").classList.add("chosen-1.8.7-select");
                    }

                }
            }
        })
    }


    function createGroup(url, jqueryForm) {
        let form = jqueryForm[0],
            data = new FormData(form);
        $.ajax({
            url: '/chat/front/group',
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (php_script_response) {
                if (php_script_response.createGroupSuccess === true) {
                    alert('Création Réussit');
                    location.reload();
                }else if (php_script_response.groupSuccess === true) {
                    if (document.getElementById('newGroup') === null) {
                        document.getElementById("Chat").innerHTML += php_script_response.view;
                        document.getElementById("form_new_group_add_user_to_groups").classList.add("chosen-1.8.7-select");
                    }
                }else {
                    alert('Problème lors de la création');
                }

            }
        });
    }

    function editGroup(id) {
        let url = "/chat/front/update/group/"+id;
        let data = new FormData();
        $.ajax({
            url: url,
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (php_script_response) {
                if (php_script_response.groupSuccess === true) {
                    if (document.getElementById('newGroup') === null) {
                        document.getElementById("Chat").innerHTML += php_script_response.view;
                        document.getElementById("form_new_group_add_user_to_groups").classList.add("chosen-1.8.7-select");
                    }

                }
            }
        })
    }

    function deleteGroup(id) {
        let url = "/chat/front/delete/group/"+id;
        console.log(url);
        let data = new FormData();
        $.ajax({
            url: url,
            type: "POST",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            success: function (php_script_response) {
                if (php_script_response.deleteGroupSuccess === true) {
                    alert(php_script_response.groupMessage);
                    location.reload();
                    }

                }
        })
    }

    return {
        init: init,
        initGroup: initGroup,
    }

}());