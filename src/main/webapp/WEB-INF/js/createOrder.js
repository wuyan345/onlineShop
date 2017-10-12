var userInfoJson = null;
var shippingInfo = null;
var shippingJson = null;
var preorderJson = null;
var shippingId = null;

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
        url: "order/preorder",
        type: "post",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	preorderJson = result;
        	if(preorderJson.status == 0){
        		showPreorder();
        	}else{
        		if(preorderJson.msg == "没有已勾选的购物车"){
        			alert(preorderJson.msg);
        			location.href = history.go(-1);
        		}
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
	
	$("#fill_shipping").click(function(){
		$("#check_shipping").attr("style", "display:none");
		$("#shipping_add").removeAttr("style");
	});
	
	$("#add_shipping").click(function(){
		var status = addShipping();
		if(status == false){
			return;
		}
		status = getShipping();
		if(status == false){
			return;
		}
		var name = shippingJson.data[0].name;
		var phone = shippingJson.data[0].phone;
		var province = shippingJson.data[0].province;
		var city = shippingJson.data[0].city;
		var district = shippingJson.data[0].district;
		var street = shippingJson.data[0].street;
		var address = shippingJson.data[0].address;
		var zip = shippingJson.data[0].zip;
		shippingId = shippingJson.data[0].id;
		shippingInfo = name+" "+phone+"  "+province+" "+city+" "+district+" "+street+address+" "+zip;
		$("#show_shipping").removeAttr("style");
		$("#show_shipping").html(shippingInfo);
		$("#shipping_add").attr("style", "display:none");
		$("#check_shipping").attr("style", "display:none");
	});
});

function addShipping(){
	var formData = new FormData();
	formData.append("name", $("#name").val());
	formData.append("phone", $("#phone").val());
	formData.append("province", $("#province").val());
	formData.append("city", $("#city").val());
	formData.append("district", $("#district").val());
	formData.append("street", $("#street").val());
	formData.append("address", $("#address").val());
	formData.append("zip", $("#zip").val());
	
	var status = false;
	$.ajax({
		url : "shipping/addShipping",
		type : "post",
		data: formData,
		contentType : false,
		processData : false,
		async : false,
		dataType : "json",
		success : function(result) {
			if(result.status == 0){
				status = true;
			}else{
				status = false;
				alert(result.msg);
			}
		},
		error : function() {
			alert("error进入");
			status = false;
		}
	});
	return status;
}

function getShipping(){
	var status = false;
	$.ajax({
		url : "shipping/list",
		type : "get",
		contentType : false,
		processData : false,
		async : false,
		dataType : "json",
		success : function(result) {
			if(result.status == 0){
				shippingJson = result;
				status = true;
			}else{
				status = false;
			}
		},
		error : function() {
			alert("error进入");
			status = false;
		}
	});
	return status;
}

function showPreorder(){
	// 展示收货信息
	if($.isEmptyObject(preorderJson.data.shippingList)){
		$("#check_shipping").removeAttr("style");
		$("#shipping_add").attr("style", "display:none");
	}else{
		var name = preorderJson.data.shippingList[0].name;
		var phone = preorderJson.data.shippingList[0].phone;
		var province = preorderJson.data.shippingList[0].province;
		var city = preorderJson.data.shippingList[0].city;
		var district = preorderJson.data.shippingList[0].district;
		var street = preorderJson.data.shippingList[0].street;
		var address = preorderJson.data.shippingList[0].address;
		var zip = preorderJson.data.shippingList[0].zip;
		shippingId = preorderJson.data.shippingList[0].id;
		shippingInfo = name+" "+phone+"  "+province+" "+city+" "+district+" "+street+address+" "+zip;
		$("#show_shipping").removeAttr("style");
		$("#show_shipping").html(shippingInfo);
		$("#shipping_add").attr("style", "display:none");
		$("#check_shipping").attr("style", "display:none");
	}
	// 展示送货清单
	var totalPrice = 0;
	var i = 0;
	for( ; preorderJson.data.cartGoodsBoList[i]; i++){
		var gid = preorderJson.data.cartGoodsBoList[i].goodsId;
		var imgHost = "http://img.zxshopdemo.com/";
		var imgSrc = imgHost + preorderJson.data.cartGoodsBoList[i].mainImage;
		var name = preorderJson.data.cartGoodsBoList[i].name;
		var price = preorderJson.data.cartGoodsBoList[i].goodsPrice;
		var quantity = preorderJson.data.cartGoodsBoList[i].quantity;
		totalPrice += price * quantity;
		
		// 0-row0
		$("#start").append($("<div></div>").attr({"id":gid+"-row0", "class":"row"}));
		// 0-div0
		$("#"+gid+"-row0").append($("<div></div>").attr({"id":gid+"-div0", "class":"col-md-1 col-sm-1"}));
		// 0-div1
		$("#"+gid+"-row0").append($("<div></div>").attr({"id":gid+"-div1", "class":"col-md-2 col-sm-2"}));
		// 0-img0
		$("#"+gid+"-div1").append($("<img></img>").attr({"id":gid+"-img0", "src":imgSrc, "width":"100px"}));
		// 0-div2
		$("#"+gid+"-row0").append($("<div></div>").attr({"id":gid+"-div2", "class":"col-md-3 col-sm-3 margin-top-20"}));
		$("#"+gid+"-div2").append($("<strong>"+name+"</strong>"));
		// 0-div3
		$("#"+gid+"-row0").append($("<div></div>").attr({"id":gid+"-div3", "class":"col-md-2 col-sm-2 margin-top-20"}));
		// 0-div4
		$("#"+gid+"-div3").append($("<div>"+"￥"+price+"</div>").attr({"id":gid+"-div4", "class":"pull-right", "style":"color:red"}));
		// 0-div5
		$("#"+gid+"-row0").append($("<div></div>").attr({"id":gid+"-div5", "class":"col-md-1 col-sm-1 margin-top-20"}));
		// 0-div6
		$("#"+gid+"-row0").append($("<div>"+"x"+quantity+"</div>").attr({"id":gid+"-div6", "class":"col-md-1 col-sm-1 margin-top-20"}));
	}
	$("#totalPrice").html("￥"+totalPrice);
}

function generateOrder(){
	if(shippingId == null){
		alert("缺少收货信息");
		return false;
	}
	$.ajax({
        url: "order/generateOrder?shippingId="+shippingId,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	if(result.status == 0){
        		alert(result.msg);
        		var orderId = result.data.id;
        		location.href = "pay.html?orderId=" + orderId;
        	}else{
        		alert(result.msg);
        	}
		},
		error: function(){
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