$(function () {

    getEntriesTree();

    function getEntriesTree() {
        $.ajax({
            url: '/entries',
            type: 'GET',
            contentType: 'application/json',
            dataType: 'json',
            success: (result)=>{
                console.log(result);
                $("#tree").treeview({
                    data: result,
                    emptyIcon: "glyphicon glyphicon-file",
                    onNodeSelected: function (event, data) {
                        if(data.clickable){
                            console.log(data.text);
                        }
                    }
                })
            }
        });
    }
})