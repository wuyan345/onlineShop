$(document).ready(function(){
	//初始化文本编辑器
	var editor = new Simditor({
		//textarea的id
		textarea: $('#editor'),
		//工具条都包含哪些内容
		toolbar:['title','bold','italic','underline','strikethrough','fontScale','color','ol','ul','blockquote','code','table','link',
			'image','hr','indent','outdent','alignment'],
		defaultImage:'simditor-2.3.6/images/image.png',
		//若需要上传功能，上传的参数设置。
		upload:{
			url: '',
			params: null,
	    	fileKey: 'upload_file',
	    	connectionCount: 3,
	    	leaveConfirm: '正在上传文件'
	    }
	});
});