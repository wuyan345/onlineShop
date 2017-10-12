var userInfoJson = null;
var goodsListJson = null;

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
	// subCategoryId -> scId
	var scId = GetQueryString("scId");
	if(scId == 0)
		getGoodsListData(100074);	// 电视
	if(scId == 1)
		getGoodsListData(100084);	// 手机通讯
	if(scId == 2)
		getGoodsListData(100092);	// 电脑配件
	if(scId == 3)
		getGoodsListData(100039);	// 家具
	if(scId == 4)
		getGoodsListData(100099);	// 女装
	if(scId == 5)
		getGoodsListData(100046);	// 美妆个护
	if(scId == 6)
		getGoodsListData(100049);	// 箱包
	if(scId == 7)
		getGoodsListData(100052);	// 男鞋
	if(scId == 8)
		getGoodsListData(100056);	// 汽车用品
	if(scId == 9)
		getGoodsListData(100116);	// 玩具
	if(scId == 10)
		getGoodsListData(100061);	// 酒类
	if(scId == 11)
		getGoodsListData(100064);	// 礼品
	if(scId == 12)
		getGoodsListData(100120);	// 文艺
	var keyword = GetQueryString("keyword");
	if(keyword != null){
		getSearchListData(keyword);
	}
	
	$("#btn-search").click(function(){
		var keyword = $("#input-search").val();
		location.href = "goods_list.html?keyword=" + keyword;
	});
});

function getGoodsListData(categoryId){
	$.ajax({
        url: "goods/list?categoryId=" + categoryId,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	goodsListJson = result;
        	showGoodsList();
		},
		error: function(){
			alert("error进入");
		}
	});
}

function showGoodsList(){
	if($.isEmptyObject(goodsListJson))
		return;
	if(goodsListJson.status == 1){
		return;
	}
	if($.isEmptyObject(goodsListJson.data.list)){
//		alert("很抱歉，没有找到您需要的商品。");
		$("#notice").removeAttr("hidden");
		return;
	}
	
	var i = 0;
	for( ; goodsListJson.data.list[i]; i++){
		var imgHost = "http://img.zxshopdemo.com/";
		var gid = goodsListJson.data.list[i].id;
		var name = goodsListJson.data.list[i].name;
		var imgSrc = imgHost + goodsListJson.data.list[i].mainImage;
		var price = goodsListJson.data.list[i].price;
		var href = "goods_detail.html?goodsId=" + gid;
		
		// 0-div0
		$("#start").append($("<div></div>").attr({"id":gid+"-div0", "class":"col-md-3 col-sm-3"}));
		// 0-div1
		$("#"+gid+"-div0").append($("<div></div>").attr({"id":gid+"-div1", "class":"thumbnail"}));
		// 0-img0
		$("#"+gid+"-div1").append($("<img></img>").attr({"id":gid+"-img0", "src":imgSrc}));
		// 0-div2
		$("#"+gid+"-div1").append($("<div></div>").attr({"id":gid+"-div2", "class":"caption"}));
		// 0-p0
		$("#"+gid+"-div2").append($("<p></p>").attr({"id":gid+"-p0"}));
		// 0-a0
		$("#"+gid+"-p0").append($("<a>"+name+"</a>").attr({"id":gid+"-a0", "href":href}));
		// 0-h30
		$("#"+gid+"-div2").append($("<h3></h3>").attr({"id":gid+"-h30", "style":"color:red"}));
		// 0-strong0
		$("#"+gid+"-h30").append($("<strong>"+"¥"+price+"</strong>").attr({"id":gid+"-strong0"}));
	}
}

function getSearchListData(keyword){
	var formData = new FormData();
	formData.append("keyword", keyword);
	$.ajax({
        url: "goods/searchGoods",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	goodsListJson = result;
        	showGoodsList();
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
		return;
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