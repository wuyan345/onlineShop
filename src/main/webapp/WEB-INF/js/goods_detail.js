var goodsDetailJson = null;

$(document).ready(function(){
	var goodsId = GetQueryString("goodsId");
	$.ajax({
        url: "goods/getGoodsDetail?goodsId=" + goodsId,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	goodsDetailJson = result;
        	showDetail();
		},
		error: function(){
			alert("error进入");
		}
	});
});

function add(){
	var cartNum = parseInt(document.getElementById("cartNum").value);
	if(cartNum >= 1){
		document.getElementById("btn-sub").removeAttribute("disabled");
	}
	document.getElementById("cartNum").value = cartNum + 1;
}

function sub(){
	var cartNum = parseInt(document.getElementById("cartNum").value);
	if(cartNum == 2){
		document.getElementById("cartNum").value = cartNum - 1;
		document.getElementById("btn-sub").setAttribute("disabled", "disabled");
	}else if(cartNum > 2)
		document.getElementById("cartNum").value = cartNum - 1;
}

function showDetail(){
	if($.isEmptyObject(goodsDetailJson))
		return;
	if(goodsDetailJson.status == 1)
		return;
	
	var name = goodsDetailJson.data.goods.name;
	var subtitle = goodsDetailJson.data.goods.subtitle;
	var mainImg = goodsDetailJson.data.goods.mainImage;
	var detail = goodsDetailJson.data.goods.detail;
	var price = goodsDetailJson.data.goods.price;
	var stock = goodsDetailJson.data.goods.stock;
	var imgHost = goodsDetailJson.data.imgHost;
	document.getElementById("mainImg").setAttribute("src", imgHost + mainImg);
	document.getElementById("name").innerHTML = name;
	document.getElementById("subtitle").innerHTML = subtitle;
	document.getElementById("price").innerHTML = price;
	document.getElementById("stock").innerHTML = stock;
	document.getElementById("detail").innerHTML = detail;
}

function addCart(){
	var goodsId = GetQueryString("goodsId");
	var goodsQuantity = document.getElementById("cartNum").value;
	$.ajax({
        url: "cart/addCart?goodsId=" + goodsId + "&goodsQuantity=" + goodsQuantity,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	alert(result.msg);
		},
		error: function(){
			alert("error进入");
		}
	});
}

function GetQueryString(key) {
	var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
	var result = window.location.search.substr(1).match(reg);
	return result ? decodeURIComponent(result[2]) : null;
}