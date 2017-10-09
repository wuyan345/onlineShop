
function register(){
	var formData = new FormData();
	formData.append("username", $("#username").val());
	formData.append("password", $("#password").val());
	formData.append("phone", $("#phone").val());
	formData.append("email", $("#email").val());
	formData.append("question", $("#question").val());
	formData.append("answer", $("#answer").val());
	$.ajax({
        url: "user/register",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	if(result.status == 1){
        		alert(result.msg);
        	}else{
        		alert("注册成功");
        		login();
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
}
