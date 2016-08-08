/*
 * 文件上传部分处理
 * userCard
 */
function fileChange(sign) {
	$('#' + sign).change(function (){
		var form = createUploadForm(sign + '_upload_return');
		var parent = $(this).parent();
		append(form, $(this));
		append(form, $('#' + sign + 'FileName'));
		form.submit();
		append(parent, $(this));
		append(parent, $('#' + sign + 'FileName'));
		destroyUploadForm();
	});
	
	iframeLoad(sign);
}

function iframeLoad(sign) {
	$('#' + sign + '_upload_return').load(function () {
		$('#' + sign + 'Text-error').remove();
		$('#' + sign + 'Text').next('img').remove();
		var data =  $(this).contents().find("body").html();
		if(data) 
        {
			var responseJson = $.parseJSON(data);
        	//若iframe携带返回数据，则显示在file_upload_return_img中
            if(responseJson && responseJson.result)
            {
                $('#' + sign + 'Img').attr('src', responseJson.url);
                $('#' + sign + 'FileName').val(responseJson.fileName);
                $('#' + sign + 'File').val(responseJson.fileName);
                $('#' + sign + 'Text').val(responseJson.fileName);
            }
            else 
            {
            	if($('#' + sign + 'Text-error').length == 0) 
            	{
            		$('#' + sign + 'Text').val('');
            		var error = $('<div><label id="' + sign + 'Text-error" class="error" style="display: inline;" for="' + sign + 'Text">' + responseJson.msg + '</label></div>');
                	$('#' + sign + 'Text').parent().parent().after(error);
            	}
            	$('#' + sign + 'Img').attr('src', '../../res/images/login_bg.png');
            }
        }
	});
}

/*
 * 构建上传表单
 */
function createUploadForm(target) {
	var uploadForm = $('<form id="uploadForm" method="post" action="' + fts.baseURI + '/img/upload.act" target="' + target + '" enctype="multipart/form-data" style="display:none"></form>');
	$('#Container').append(uploadForm);
	return uploadForm;
}

/*
 *	销毁上传表单
 */
function destroyUploadForm() {	
	if($('#uploadForm')) {
		$('#uploadForm').remove();
	}
}

/*
 *	添加数据
 */
function append(target, element) {
	if(target) 
	{
		target.append(element);
	}
}

/*
 * 用户数据更新
 */
function update(arr, backView, show, edit, validate) {
	var data = {};
	for(var i = 0; i < arr.length; i++){
		var name = arr[i];
		var elements = validate.findByName(name);
		
		if(elements) 
		{
			var result = validate.element(elements);
			if(!result)
			{
				return;
			}
		}
		
		var element = elements[0];
		
		if($(element).is(':checkbox')) 
		{
			$.each(elements, function (i, checkbox) {
				if(checkbox.checked) 
				{
					data[name] = data[name] ? data[name] + ',' + $(checkbox).val() : $(checkbox).val();
				}
			});
			
		}
		else
		{
			data[name] = $(element).val();
		}
	}
	
	var result = updateUser(data);
	if(result) 
	{
		ShowEditBox(show, edit);
		if(backView) 
		{
			for(p in data) 
			{
				$("#" + backView).html(data[p]);
				return;
			}
		}
	}
}

function updateUser(data) {
	var result = false;
	var t = this;
	$.ajax({
		url : fts.baseURI + '/user/update',
		type : 'POST',
		timeout : '1000',
		async : false,
		dataType : 'json',
		data : data,
		success : function(responseText) {
			result = responseText.result;
		}
	});
	return result;
}

/*
 * 显示和修改
 */
function ShowEditBox(Show, Hide) {
	$("#" + Show).show();
    $("#" + Hide).hide();
    
    $('#' + Hide + ' > span.error').remove();
}
/**
 * 校验
 * @return
 */
function submitC(){
	var mobile=$("#mobile").val();
	var vcode=$("#vcode").val();
	var password=$("#userPassword").val();
	var password1=$("#password1").val();
	var userIrId=$("#userIrId").val();
	var errMsg="";
	var infoMsg="'警告对话框'";
	if(''==mobile||null==mobile){
		
		errMsg="手机号码不能为空";
		Dialog.show(errMsg, 2, 2000);
		return ;
	}else{
		var myreg = /(^13\d{9}$)|(^14)[5,7]\d{8}$|(^15[0,1,2,3,5,6,7,8,9]\d{8}$)|(^17)[6,7,8]\d{8}$|(^18\d{9}$)/g ; 
		
		if (myreg.test(mobile)){
			
		}else{
			errMsg='请输入正确的手机号';
			Dialog.show(errMsg, 2, 2000);
			return;
		}
	}
	if(''==vcode||null==vcode){
		errMsg="验证码不能为空";
		Dialog.show(errMsg, 2, 2000);
		return;
	}
	if(''==password||null==password){
		errMsg="密码不能为空";
		Dialog.show(errMsg, 2, 2000);
		return;
	}
	if(''==password1||null==password1){
		errMsg="确认密码不能为空";
		Dialog.show(errMsg, 2, 2000);
		return;
	}
	if(password!=password1){
		errMsg="两次输入的密码不一致";
		Dialog.show(errMsg, 2, 2000);
		return;
	}
	if(password.length<6){
		errMsg="密码长度小于6位";
		Dialog.show(errMsg, 2, 2000);
		return;
	}
	if(''==userIrId||null==userIrId){
		errMsg="车牌号不能为空";
		Dialog.show(errMsg, 2, 2000);
		return;
	}else if(userIrId.length!=7){
        errMsg="车牌号填写错误";
        Dialog.show(errMsg, 2, 2000);
		return;
    }
	   $.ajax({
           url : '/code/getMobileCode.act',
           type : 'POST',
           timeout : '1000',
           async : false,
           dataType : 'json',
           data : {
               vcode : vcode
           },
           success : function(data) {
              if(data.success){
            	  submit();
              }else{
            	  errMsg="验证码输入错误";
          		  Dialog.show(errMsg, 2, 2000);
              }
              
           },error:function(){
               alert("操作失败");
           }
       });
}
function submit(){
	  var data=$("#form1").serialize();
	   $.ajax({
        url : '/user/register.html',
        type : 'POST',
        timeout : '1000',
        async : false,
        dataType : 'json',
        data : data,
        success : function(data) {
		   if(data.success==''){
			   	  errMsg="手机号码已经存在";
	       		  Dialog.show(errMsg, 2, 2000);
	       		  return ;
		   }else{
			   if(data.success){
	        	   Dialog.show("注册成功", 1, 2000);
	        	   setTimeout(function() {
	       			window.location.href="/";
	       		},2000);
	           }else{
	         	  errMsg="注册失败";
	       		  Dialog.show(errMsg, 2, 2000);
	           }
		   }
          
           
        },error:function(){
            alert("操作失败");
        }
    });
}
/*
 * 获取手机验证码
 */
function getMobileCode (btn, mobile, validate, target, time) {
	if(''==mobile||null==mobile){
		jAlert($.messages['1003'], '警告对话框');
		return ;
	}
	//获取验证码
	alert("ss");
		
		var telephone = mobile;
		alert(telephone);return;
		if (true) {
			$.ajax({
				url : '/code/mobileCode.act',
				type : 'POST',
				timeout : '1000',
				async : false,
				dataType : 'json',
				data : {
					telephone : telephone
				},
				success : function(responseText) {
					timing(time, target);
				}
			});
		}

}

function timing(/*number 毫秒数*/time, /*String 父节点ID*/element) {
	var _t = this, minutes, seconds, showText = '', _time = parseFloat(time) / 1000, _element = element, _a = _element.parent().next().find('a');
	
	if(_time && _time > 0) {
		
			//minutes = parseInt(_time / 60);
			//if(minutes && minutes > 0) {
			//	showText = parseInt(minutes*60)
			//}
			//seconds = parseInt((parseFloat(_time /60.0) - parseInt(_time /60.0)) * 60);
			//if(seconds && seconds > 0) {
			$("#timing").show();
				showText =parseInt(_time) + '秒后重发';
			//}
		
	}
	if(_time == 0) {
		showText = '重新获取';
		$("#btngry").hide();
		$("#vcodeBtn").show();
		$("#vcodeBtn").html(showText);
	}else {
		setTimeout(function() {
			timing(time - 1000, _element);
		},1000);
		//_a.html('');
		$("#vcodeBtn").hide();
		$("#timing").parent().show();
		$("#timing").html('<button type="button" id="btngry"  class="ajax-post button bg-gray" target-form="form-x">'+showText+'</button>');
		//_element.parent().show();
	}
}

function timmingEmail(time, element, suffix, callback) {
	var _t = this, minutes, seconds, showText = '', _time = parseFloat(time) / 1000, _element = element, _a = _element.next();
	if(_time && _time > 0) {
		if(_time > 60) {
			minutes = parseInt(_time / 60);
			if(minutes && minutes > 0) {
				showText = minutes + '分';
			}
			seconds = parseInt((parseFloat(_time /60.0) - parseInt(_time /60.0)) * 60);
			if(seconds && seconds > 0) {
				showText += ' ' + seconds + '秒';
			}
		}else{
			showText = parseInt(_time) + '秒';
		}
	}
	showText = showText + suffix;
	if(_time == 0) {
		_element.html('');
		_a.html(showText);
		callback();
	}else {
		setTimeout(function() {
			timmingEmail(time - 1000, _element, suffix, callback);
		},1000);
		_a.html('');
		_a.attr('disabled');
		_element.html(showText);
	}
}