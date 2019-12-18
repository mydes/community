function post() {
    var questionId = $("#question-id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type:"post",
        url:"/comment",
        dataType:"json",
        contentType:"application/json",
        data:JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success:function (response) {
            if (response.code == 200){
                $("#comment_section").hide();
            }else if(response.code == 2003){
                var isAccepted = confirm(response.message);
                if (isAccepted){
                    window.open("https://github.com/login/oauth/authorize?client_id=26a686ea62ea585f3d68&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    window.localStorage.setItem("closable",true);
                }
            }else {
                alert(response.message);
            }
        },
    });
}