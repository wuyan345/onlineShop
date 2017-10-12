var orderId = GetQueryString("orderId");
var userInfoJson = null;

$(document).ready(function() {
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
		url : "pay/alipay?orderId=" + orderId,
		type : "post",
		contentType : false,
		processData : false,
		dataType : "json",
		success : function(result) {
			if (result.status == 0) {
				var imgHost = "http://img.zxshopdemo.com/";
				var imgSrc = imgHost + result.data;
				$("#qr-img").attr({"src" : imgSrc,"width" : "300px"});
				$("#wait").attr("hidden", "hidden");
				$("#notice").removeAttr("hidden");
				setInterval(function(){getPaymentStatus()}, 2000);
			}else{
				alert(result.msg);
				if(result.msg == "该订单已付款"){
					location.href = "order.html";
				}
			}
		},
		error : function() {
			alert("error进入");
		}
	});
});

function getPaymentStatus(){
	$.ajax({
		url : "order/getPaymentStatus?orderId=" + orderId,
		type : "post",
		contentType : false,
		processData : false,
		dataType : "json",
		success : function(result) {
			if (result.status == 0 && result.data == 1) {
				location.href = "order.html";
			}
		},
		error : function() {
			alert("error进入");
		}
	});
}

function showUserName(){
	if($.isEmptyObject(userInfoJson))
		return;
	if(userInfoJson.status == 1)
		location.href = "login.html";
	// 删除login、register节点
	document.getElementById("nav-ul").removeChild(document.getElementById("login"));
	document.getElementById("nav-ul").removeChild(document.getElementById("register"));
	
	document.getElementById("username").style.display = "";
	document.getElementById("logout").style.display = "";
	document.getElementById("username").innerHTML = "你好，" + userInfoJson.data.username;
}

function GetQueryString(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}