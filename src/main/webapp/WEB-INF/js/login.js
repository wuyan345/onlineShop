
function login(){
	
	var formData = new FormData();
	formData.append("username", document.getElementById("username").value);
	formData.append("password", document.getElementById("password").value);
	$.ajax({
        url: "user/login",
        type: "post",
        data: formData,
        async: false,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	if(result.status == 0){
        		alert("登录成功");
        		history.go(-1);
        	}else{
        		alert(result.msg);
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
}