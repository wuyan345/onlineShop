var imageName = "";
var userInfoJson = null;

$(document).ready(function(){
	
	$.ajax({
        url: "manage/user/getInfo",
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
        url: "manage/category/getChrildrenCategory?categoryId=0",
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	var i = 0;
        	for( ; result.data[i]; i++){
        		var parent = document.getElementById("selectA");
        		var optNode = document.createElement("option");
        		optNode.setAttribute("value", result.data[i].id);
        		optNode.innerHTML = result.data[i].name;
        		parent.appendChild(optNode);
        	}
		},
		error: function(){
			alert("error进入");
		}
	});
	//初始化文本编辑器
	var editor = new Simditor({
		//textarea的id
		textarea: $('#editor'),
		//工具条都包含哪些内容
		toolbar: ['title','bold','italic','underline','strikethrough','fontScale','color','ol','ul','blockquote','code','table','link',
			'image','hr','indent','outdent','alignment'],
		defaultImage: 'simditor-2.3.6/images/image.png',
		toolbarHidden: false,
		toolbarFloat: true,
		toolbarFloatOffset: 50,
		//若需要上传功能，上传的参数设置。
		upload:{
			url: "manage/goods/uploadRichText",
			params: null,
	    	fileKey: 'upload_richText',
	    	connectionCount: 3,
	    	leaveConfirm: '正在上传文件'
	    }
	});
});

function uploadImage(){
	var fileObj = document.getElementById("upload_file").files[0]; // js获取文件对象
    var formData = new FormData();
	formData.append("upload_image", fileObj);
    $.ajax({
        url: "manage/goods/uploadImage",
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        dataType: "json",
        success: function(result) {
        	/* result就是json对象，不需要JSON.parse(result) */
        	if(result.status == 0){
        		imageName = result.data;
        		document.getElementById("uploadSuccess").innerHTML = result.data + "上传成功";
        		// 隐藏按钮
        		document.getElementById("upload_file").setAttribute("style", "display:none");
        		document.getElementById("btn_upload_file").setAttribute("style", "display:none");
        	}else{
        		document.getElementById("uploadSuccess").innerHTML = "上传失败，重新上传";
        	}
		},
		error: function(){
			alert("error进入");
		}
    });
}

function formSubmit(){
	var formData = new FormData();
	// 装配表单数据
	var categoryId = null;
	if(document.getElementById("selectB"))
		categoryId = document.getElementById("selectB").value;
	else
		categoryId = document.getElementById("selectA").value;
	formData.append("name", document.getElementById("name").value);
	formData.append("subtitle", document.getElementById("subtitle").value);
	formData.append("categoryId", categoryId);
	formData.append("price", document.getElementById("price").value);
	formData.append("stock", document.getElementById("stock").value);
	formData.append("mainImage", imageName);
	formData.append("detail", document.getElementById("editor").value);
	$.ajax({
        url: "manage/goods/addGoods",
        type: "post",
        contentType: false,
        processData: false,
        data: formData,
        dataType: "json",
        success: function(result) {
        	if(result.status == 0){
        		alert("添加商品成功");
        		location.reload();
        	}else
        		alert("添加商品失败");
		},
		error: function(){
			alert("error进入");
		}
    });
}

function btnChange(value) {
	$.ajax({
        url: "manage/category/getChrildrenCategory?categoryId=" + value,
        type: "get",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(result) {
        	// 判断selectB是否存在
        	if(document.getElementById('selectB')){
	        	// 删除selectB选择标签
	        	var parent = document.getElementById("category");
				parent.removeChild(document.getElementById('selectB'));
        	}
        	// 判断有无data数据
			if(!$.isEmptyObject(result.data)){
				// 创建selectB选择标签
				var parent = document.getElementById("category");
        		var selectNode = document.createElement("select");
        		selectNode.setAttribute("id", "selectB");
        		selectNode.setAttribute("class", "form-control cate-select col-md-5");
        		selectNode.setAttribute("style", "width:200px");
        		parent.appendChild(selectNode);
    		
				// selectB创建第一个option标签
				var parent = document.getElementById("selectB");
        		var optNode = document.createElement("option");
        		optNode.innerHTML = "选择二级类别";
        		parent.appendChild(optNode);
        		
        		// selectB创建余下的option标签
	        	var i = 0;
	        	for( ; result.data[i]; i++){
	        		var parent = document.getElementById("selectB");
	        		var optNode = document.createElement("option");
	        		optNode.setAttribute("value", result.data[i].id);
	        		optNode.innerHTML = result.data[i].name;
	        		parent.appendChild(optNode);
	        	}
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
	document.getElementById("username").innerHTML = "你好，管理员" + userInfoJson.data.username;
}