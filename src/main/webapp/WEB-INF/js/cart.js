var userInfoJson = null;
var cartJson = null;

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
        url: "cart/listCart",
        type: "post",	// get会出现传不了sessionid到server的情况，原因未知
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	cartJson = result;
        	showCartList();
		},
		error: function(){
			alert("error进入");
		}
	});
});

function add(btnId){
	var cid = btnId.split("-")[0];
	var quantity = parseInt(document.getElementById(cid+"-input1").value);
	if(quantity >= 1){
		document.getElementById(cid+"-btn0").removeAttribute("disabled");
	}
	document.getElementById(cid+"-input1").value = quantity + 1;
	changeQuantity(cid, quantity + 1);
}

function sub(btnId){
	var cid = btnId.split("-")[0];
	var quantity = parseInt(document.getElementById(cid+"-input1").value);
	if(quantity == 2){
		document.getElementById(cid+"-input1").value = quantity - 1;
		document.getElementById(cid+"-btn0").setAttribute("disabled", "true");
	}else if(quantity > 2)
		document.getElementById(cid+"-input1").value = quantity - 1;
	if(quantity >= 2)
		changeQuantity(cid, quantity - 1);
}

function changeQuantity(cid, quantity){
	$.ajax({
        url: "cart/changeQuantity?cartId="+cid+"&quantity="+quantity,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	var i = 0
        	for( ; result.data.cartGoodsBoList[i]; i++){
        		if(cid == result.data.cartGoodsBoList[i].id){
        			var subTotalPrice = result.data.cartGoodsBoList[i].totalPrice;
        			var totalPrice = result.data.totalPrice;
        			$("#"+cid+"-div8").html(subTotalPrice);
        			$("#totalPrice").html("￥"+result.data.totalPrice);
        		}
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
}


function checkboxEvt(inputId){
	var cid = inputId.split("-")[0];
	if($("#"+inputId).prop("checked")){
		// 单选或全选
		if(inputId == "chkForAll"){
			$("[type='checkbox']").prop("checked",true);
			selectOrUnselect(2, null);
		}else{
			$("#"+inputId).prop("checked",true);
			// 若全部勾选，则全选框也得勾选
			if($("[name='singleChk']").prop("checked")){
				$("#chkForAll").prop("checked",true);
			}
			selectOrUnselect(0, cid);
		}
	}else{// 单不选或全不选
		if(inputId == "chkForAll"){
			$("[type='checkbox']").prop("checked",false);
			selectOrUnselect(3, null);
		}else{
			$("#"+inputId).prop("checked",false);
			// 有不勾选的情况，则全选框也得取消
			$("#chkForAll").prop("checked",false);
			selectOrUnselect(1, cid);
		}
	}
}

function selectOrUnselect(type, cid){
	var url = null;
	if(type == 0)	// 单个勾选
		url = "cart/select?cartId=" + cid;
	else if(type == 1)	// 单个不勾选
		url = "cart/unselect?cartId=" + cid;
	else if(type == 2)	// 全选
		url = "cart/select";
	else if(type == 3)	// 全不选
		url = "cart/unselect";
	
	$.ajax({
        url: url,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	var totalPrice = result.data.totalPrice;
			$("#totalPrice").html("￥"+result.data.totalPrice);
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

function showCartList(){
	if($.isEmptyObject(cartJson))
		return;
	if(cartJson.status == 1){
		if(cartJson.msg == "该用户购物车为空"){
			$("#start").attr("style", "display:none");
			$("#createOrder").attr("style", "display:none");
			$("#emptyCart").removeAttr("style");
		}
		return;
	}
	
	$("#totalPrice").html("￥"+cartJson.data.totalPrice);
	var i = 0;
	for( ; cartJson.data.cartGoodsBoList[i]; i++){
		var cid = cartJson.data.cartGoodsBoList[i].id;
		var imgSrc = cartJson.data.cartGoodsBoList[i].mainImage;
		var name = cartJson.data.cartGoodsBoList[i].name;
		var price = cartJson.data.cartGoodsBoList[i].price;
		var quantity = cartJson.data.cartGoodsBoList[i].quantity;
		var selected = cartJson.data.cartGoodsBoList[i].selected;
		var subTotalPrice = cartJson.data.cartGoodsBoList[i].totalPrice;
		var href = "deleteCart.html?cartId=" + cid;
		var input0Id = cid + "-input0";
		var btn0Id = cid + "-btn0";
		var btn1Id = cid + "-btn1";
		
		// 0-div0
		$("#start").after($("<div></div>").attr({"id":cid+"-div0", "class":"row"}));
		// 0-div1
		$("#"+cid+"-div0").after($("<div></div>").attr({"id":cid+"-div1", "class":"col-md-1 col-sm-1"}));
		// 0-div2
		$("#"+cid+"-div1").after($("<div></div>").attr({"id":cid+"-div2", "class":"col-md-2 col-sm-2"}));
		// 0-input0
		$("#"+cid+"-div2").append($("<input></input>").attr({"id":cid+"-input0", "name":"singleChk", "type":"checkbox", "onclick":"checkboxEvt("+"\""+input0Id+"\""+")"}));
		if(selected == 0)
			$("#"+cid+"-input0").prop("checked",true);
		// 0-img0
		$("#"+cid+"-div2").append($("<img></img>").attr({"id":cid+"-img0", "src":imgSrc, "width":"100px"}));
		// 0-div3
		$("#"+cid+"-div2").after($("<div></div>").attr({"id":cid+"-div3", "class":"col-md-3 col-sm-3 margin-top-20"}));
		$("#"+cid+"-div3").append($("<strong>"+name+"</strong>"));
		// 0-div4
		$("#"+cid+"-div3").after($("<div></div>").attr({"id":cid+"-div4", "class":"col-md-1 margin-top-20"}));
		// 0-div5
		$("#"+cid+"-div4").append($("<div>"+price+"</div>").attr({"id":cid+"-div5", "class":"pull-right"}));
		// 0-div6
		$("#"+cid+"-div4").after($("<div></div>").attr({"id":cid+"-div6", "class":"col-md-2 col-sm-2 margin-top-20"}));
		// 0-div7
		$("#"+cid+"-div6").append($("<div></div>").attr({"id":cid+"-div7", "class":"input-group input-group-sm col-md-offset-4"}));
		// 0-span0
		$("#"+cid+"-div7").append($("<span></span>").attr({"id":cid+"-span0", "class":"input-group-btn"}));
		// 0-btn0
		$("#"+cid+"-span0").append($("<button>-</button>").attr({"id":cid+"-btn0", "type":"button", "class":"btn btn-default", "onclick":"sub("+"\""+btn0Id+"\""+")"}));
		// 0-input1
		$("#"+cid+"-div7").append($("<input></input>").attr({"id":cid+"-input1", "type":"text", "value":quantity, "class":"form-control", "disabled":"disabled"}));
		// 0-span1
		$("#"+cid+"-div7").append($("<span></span>").attr({"id":cid+"-span1", "class":"input-group-btn"}));
		// 0-btn1
		$("#"+cid+"-span1").append($("<button>+</button>").attr({"id":cid+"-btn1", "type":"button", "class":"btn btn-default", "onclick":"add("+"\""+btn1Id+"\""+")"}));
		// 0-div8
		$("#"+cid+"-div6").after($("<div>"+subTotalPrice+"</div>").attr({"id":cid+"-div8", "class":"col-md-1 margin-top-20"}));
		// 0-div9
		$("#"+cid+"-div8").after($("<div></div>").attr({"id":cid+"-div9", "class":"col-md-1 margin-top-20"}));
		// 0-a0
		$("#"+cid+"-div9").append($("<a>"+"删除"+"</a>").attr({"id":cid+"-a0", "href":href}));
		// 0-div10
		$("#"+cid+"-div9").after($("<div></div>").attr({"id":cid+"-div10", "class":"col-md-1"}));
	}
}

function goPay(){
	if("￥0" == $("#totalPrice").html()){
		alert("购物车为空");
		return;
	}
	location.href = "createOrder.html";
}