var userInfoJson = null;
var userAllInfoJson = null;

$(document).ready(function(){
	$.ajax({
        url: "user/getInfo",
        type: "post",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	userInfoJson = result;
        	showUserName();
		},
		error: function(){
			alert("error进入");
		}
	});
	$.ajax({
        url: "user/getAllInfo",
        type: "post",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	if(result.status == 0){
	        	userAllInfoJson = result;
	        	showInfo();
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
	$("#modify").click(function(){
		var formData = new FormData();
		formData.append("phone", $("#phone").val());
		formData.append("email", $("#email").val());
		formData.append("question", $("#question").val());
		formData.append("answer", $("#answer").val());
		$.ajax({
	        url: "user/editInfo",
	        type: "post",
	        data: formData,
	        contentType: false,
	        processData: false,
	        dataType: "json",
	        success: function(result) {
	        	if(result.status == 0){
		        	alert(result.msg);
		        	location.reload();
	        	}else{
	        		alert(result.msg);
	        	}
			},
			error: function(){
				alert("error进入");
			}
		});
	});
});

function showInfo(){
	if($.isEmptyObject(userAllInfoJson))
		return;
	if(userInfoJson.status == 1){
		return;
	}
	
	var name = userAllInfoJson.data.username;
	var phone = userAllInfoJson.data.phone;
	var email = userAllInfoJson.data.email;
	var question = userAllInfoJson.data.question;
	var answer = userAllInfoJson.data.answer;
	$("#username").attr("placeholder", name);
	$("#phone").attr("placeholder", phone);
	$("#email").attr("placeholder", email);
	$("#question").attr("placeholder", question);
	$("#answer").attr("placeholder", answer);
	
}

function showUserName(){
	if($.isEmptyObject(userInfoJson))
		return;
	if(userInfoJson.status == 1){
		location.href = "login.html";
	}
	
	// 删除login、register节点
	document.getElementById("nav-ul").removeChild(document.getElementById("login"));
	document.getElementById("nav-ul").removeChild(document.getElementById("register"));
	
	document.getElementById("nav-username").style.display = "";
	document.getElementById("logout").style.display = "";
	document.getElementById("nav-username").innerHTML = "你好，" + userInfoJson.data.username;
	
	document.getElementById("username").setAttribute("placeholder", userInfoJson.data.username);
}