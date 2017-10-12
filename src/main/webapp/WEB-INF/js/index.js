

$(document).ready(function(){
	
	$("#btn-search").click(function(){
		var keyword = $("#input-search").val();
		location.href = "goods_list.html?keyword=" + keyword;
	});
});