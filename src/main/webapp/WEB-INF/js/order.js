var orderJson = null;
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
	$.ajax({
        url: "order/listOrder",
        type: "post",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	orderJson = result;
        	if(orderJson.status == 0){
        		showOrder();
        	}else if(orderJson.status == 1){
        		if(orderJson.msg == "该用户没有订单"){
        			$("#notice").removeAttr("hidden");
        		}
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
	
});

function showOrder(){
	
	var i = 0;
	for( ; orderJson.data.list[i]; i++){
		var oid = orderJson.data.list[i].order.id;
		var time = orderJson.data.list[i].createTime;
		var orderNo = orderJson.data.list[i].order.orderNo;
		var status = orderJson.data.list[i].order.status;
		var btn0Id = oid + "-btn0";
		var btn1Id = oid + "-btn1";
		
		// div0
		$("#start").append($("<div></div>").attr({"id":oid+"-div0", "class":"panel panel-default"}));
		// div1
		$("#"+oid+"-div0").append($("<div></div>").attr({"id":oid+"-div1", "class":"panel-body"}));
		// div2
		$("#"+oid+"-div1").append($("<div></div>").attr({"id":oid+"-div2", "class":"row"}));
		// div3
		$("#"+oid+"-div2").append($("<div>"+time+"</div>").attr({"id":oid+"-div3", "class":"col-md-3 col-sm-3"}));
		// div4
		$("#"+oid+"-div2").append($("<div>"+orderNo+"</div>").attr({"id":oid+"-div4", "class":"col-md-3 col-sm-3"}));
		// div5
		$("#"+oid+"-div2").append($("<div>数量</div>").attr({"id":oid+"-div5", "class":"col-md-1 col-sm-1"}));
		// div6
		$("#"+oid+"-div2").append($("<div>收货人</div>").attr({"id":oid+"-div6", "class":"col-md-1 col-sm-1"}));
		// div7
		$("#"+oid+"-div2").append($("<div>金额</div>").attr({"id":oid+"-div7", "class":"col-md-1 col-sm-1"}));
		// div8
		$("#"+oid+"-div2").append($("<div>状态</div>").attr({"id":oid+"-div8", "class":"col-md-1 col-sm-1"}));
		// div9
		$("#"+oid+"-div2").append($("<div></div>").attr({"id":oid+"-div9", "class":"col-md-1 col-sm-1"}));
		// btn0
		if(status == 0){
			$("#"+oid+"-div9").append($("<button>"+"去付款"+"</button>").attr({"id":oid+"-btn0", "name":"s0", "type":"button", "class":"btn btn-danger btn-xs", "onclick":"payOrRefund("+"\""+btn0Id+"\""+")"}));
		}else if(status == 1){
			$("#"+oid+"-div9").append($("<button>"+"退款"+"</button>").attr({"id":oid+"-btn0", "name":"s1", "type":"button", "class":"btn btn-danger btn-xs", "onclick":"payOrRefund("+"\""+btn0Id+"\""+")"}));
		}
		// div10
		$("#"+oid+"-div2").append($("<div></div>").attr({"id":oid+"-div10", "class":"col-md-1 col-sm-1"}));
		// btn1
		if(status == 0){
			$("#"+oid+"-div10").append($("<button>"+"取消订单"+"</button>").attr({"id":oid+"-btn1", "type":"button", "class":"btn btn-danger btn-xs", "onclick":"cancelOrder("+"\""+btn1Id+"\""+")"}));
		}
		
		var j = 0;
		for( ; orderJson.data.list[i].orderItemList[j]; j++){
			
			var oiid = orderJson.data.list[i].orderItemList[j].id;
			var imgHost = "http://img.zxshopdemo.com/";
			var imgSrc = imgHost + orderJson.data.list[i].orderItemList[j].mainImage;
			var name = orderJson.data.list[i].orderItemList[j].name;
			var quantity = orderJson.data.list[i].orderItemList[j].quantity;
			var shippingName = orderJson.data.list[i].shipping.name;
			var totalPrice = orderJson.data.list[i].orderItemList[j].totalPrice;
			var itemStatus = null;
			if(status == 0){
				itemStatus= "等待付款";
			}else if(status == 1){
				itemStatus= "已付款，等待发货";
			}else if(status == 2){
				itemStatus= "已发货";
			}else if(status == 6){
				itemStatus= "订单已关闭";
			}
			
			// div0
			$("#"+oid+"-div1").append($("<div></div>").attr({"id":oiid+"-div0", "class":"row"}));
			// div1
			$("#"+oiid+"-div0").append($("<div></div>").attr({"id":oiid+"-div1", "class":"col-md-2 col-sm-2"}));
			// img0
			$("#"+oiid+"-div1").append($("<img></img>").attr({"id":oiid+"-img0", "src":imgSrc, "width":"100px"}));
			// div2
			$("#"+oiid+"-div0").append($("<div></div>").attr({"id":oiid+"-div2", "class":"col-md-4 col-sm-4 margin-top-20"}));
			// strong0
			$("#"+oiid+"-div2").append($("<strong>"+name+"</strong>"));
			// div3
			$("#"+oiid+"-div0").append($("<div>"+"x"+quantity+"</div>").attr({"id":oiid+"-div3", "class":"col-md-1 col-sm-1 margin-top-20"}));
			// div4
			$("#"+oiid+"-div0").append($("<div>"+shippingName+"</div>").attr({"id":oiid+"-div4", "class":"col-md-1 col-sm-1 margin-top-20"}));
			// div5
			$("#"+oiid+"-div0").append($("<div>"+totalPrice+"</div>").attr({"id":oiid+"-div5", "class":"col-md-1 col-sm-1 margin-top-20"}));
			// div6
			$("#"+oiid+"-div0").append($("<div>"+itemStatus+"</div>").attr({"id":oiid+"-div6", "class":"col-md-2 col-sm-2 margin-top-20"}));
		}
	}
}

function payOrRefund(btnId){
	var oid = btnId.split("-")[0];
	var status = $("#"+btnId).attr("name");
	if (status == "s0"){
		// 去付款
		location.href = "pay.html?orderId=" + oid;
	}else if(status == "s1"){
		// 退款
		$.ajax({
	        url: "order/cancelOrder?orderId=" + oid,
	        type: "post",
	        contentType: false,
	        processData: false,
	        dataType: "json",
	        success: function(result) {
	        	if(result.status == 0){
	        		location.reload();
	        	}
			},
			error: function(){
				alert("error进入");
			}
		});
	}
}

function cancelOrder(btnId){
	var oid = btnId.split("-")[0];
	$.ajax({
        url: "order/cancelOrder?orderId=" + oid,
        type: "post",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	if(result.status == 0){
        		location.reload();
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