var userInfoJson = null;

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
});

function showUserName(){
	if($.isEmptyObject(userInfoJson))
		return;
	if(userInfoJson.status == 1)
		return;
	// 删除login、register节点
	document.getElementById("nav-ul").removeChild(document.getElementById("login"));
	document.getElementById("nav-ul").removeChild(document.getElementById("register"));
	
	document.getElementById("username").style.display = "";
	document.getElementById("logout").style.display = "";
	document.getElementById("username").innerHTML = "你好，" + userInfoJson.data.username;
}