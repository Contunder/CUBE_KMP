
    $(document).ready(function () {

        // Search
        $("#searchText").on("input", function () {
            var searchText = $(this).val();
            if (searchText == ""){
                document.getElementById('searchList').innerHTML="";
                return;
            }else {
                $.post('/search',
                    {
                        key: searchText
                    },
                    function (data, status) {
                        $("#searchList").html(data);
                    });
            }

        });

        // Search using the button
        $("#serachBtn").on("click", function () {
            var searchText = $("#searchText").val();
            if (searchText == ""){
                document.getElementById('searchList').innerHTML="";
                return;
            }else {
                $.post('/search',
                    {
                        key: searchText
                    },
                    function (data, status) {
                        $("#searchList").html(data);
                    });
            }
        });

    });
