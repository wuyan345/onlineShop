var orderId = GetQueryString("orderId");
$(document).ready(function() {
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
			}else{
				alert(result.msg);
			}
		},
		error : function() {
			alert("error进入");
		}
	});
});
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}