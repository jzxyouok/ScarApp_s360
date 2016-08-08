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

/*
 * 获取手机验证码
 */
function getMobileCode (btn, mobile, validate, target, time) {
	
	//获取验证码
	btn.click(function () {
		
		var telephone = mobile.val();
		// 调用验证手机号方法
		var result = validate.element(mobile);
		if (result) {
			$.ajax({
				url : fts.baseURI + '/code/mobileCode.act',
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
	});	
}

function timing(/*number 毫秒数*/time, /*String 父节点ID*/element) {
	var _t = this, minutes, seconds, showText = '', _time = parseFloat(time) / 1000, _element = element, _a = _element.parent().next().find('a');
	
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
	if(_time == 0) {
		showText = '重新获取';
		_element.html('');
		_element.parent().hide();
		_a.html(showText);
		_a.parent().show();
	}else {
		setTimeout(function() {
			timing(time - 1000, _element);
		},1000);
		_a.html('');
		_a.parent().hide();
		_element.html(showText);
		_element.parent().show();
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