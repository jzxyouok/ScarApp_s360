/**
 * validate组件中添加自定义校验方法
 */
(function($) {
	fts.baseURI = '${base}';
	var url = {
		nameRepet : fts.baseURI + '/user/validate/nameRepet',
		mobileVC : fts.baseURI + '/user/validate/mobileVC.v',
		imgCode : fts.baseURI + '/user/validate/imgCodeEquals.v',
		mobileRequired : fts.baseURI + '/user/validate/mobileRequired.v',
		pwdIsValid : fts.baseURI + '/user/validate/pwdIsValid',
		invitationCodeRepet : fts.baseURI + "/user/validate/invitationCodeRepet",
		emailReq : fts.baseURI + '/user/validate/emailReq.v'
	}
	
	//注册用户名格式校验
	$.validator.addMethod("namefmt", function(value, element) {
	    var regex = /^\w{5,20}$/;
	    return this.optional(element) || (regex.test(value));
	}, "用户名由5-20位数字或字母组成");
	//邀请码格式校验
	$.validator.addMethod("invitationCodefmt", function(value, element) {
	    var regex = /^\w{10,10}$/;
	    return this.optional(element) || (regex.test(value));
	}, "邀请码由10位数字或字母组成");
	//校验1-50位字符
	$.validator.addMethod("strfmt", function(value, element) {
	    var regex = /^[\u4e00-\u9fa5\w]{1,50}$/;
	    return this.optional(element) || (regex.test(value));
	}, "由1-50位数字、字母或汉字组成");
	
	//校验用户名是否存在
	$.validator.addMethod("nameRepet", function(value, element) {
		var result = true;
		//远程信息处理
		$.ajax({
			url : url.nameRepet,
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				userName : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
			}
		});
	    return result;
	}, "用户名已存在");
	
	//校验邀请码是否存在
	$.validator.addMethod("invitationCodeRepet", function(value, element) {
		var result = true;
		if(value) {
			//远程信息处理
			$.ajax({
				url : url.invitationCodeRepet,
				type : 'POST',
				timeout : '1000',
				async : false,
				data : {
					invitationCode : value
				},
				dataType : 'json',
				success : function (responseText) {
					result = responseText.result;
					var message = responseText.message;
					$.validator.messages['invitationCodeRepet'] = message !== undefined ? message : $.validator.messages['invitationCodeRepet'];
				}
			});
		}
		return result;
	}, "");
	//注册用户密码格式校验
	$.validator.addMethod("pwdfmt", function(value, element) {
	    var regex = /^\w{6,16}$/;
	    return this.optional(element) || (regex.test(value));
	}, "密码由6-16位数字或字母组成");
	
	//注册用户手机号格式校验
	$.validator.addMethod("mobilefmt", function(value, element) {
	    var regex = /^1\d{10}$/;
	    return this.optional(element) || (regex.test(value));
	}, "请输入正确手机号");
	
	//注册用户手机号唯一验证
	$.validator.addMethod("mobileRequired", function (value, element) {
		
		var result = true;
		
		var hx = $('#mobile_hx');
		var value = value || $(element).attr('placeholder');
		if(hx) {
			if(hx.val() == value) {
				return true;
			}
		}
		
		//远程信息处理
		$.ajax({
			url : url.mobileRequired,
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				mobile : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
				var message = responseText.message;
				$.validator.messages['mobileRequired'] = message !== undefined ? message : $.validator.messages['mobileRequired'];
			}
		});
		
		return result;
	}, "手机号已经被注册过");
	
	//注册用户手机号唯一验证
	$.validator.addMethod("mobileValid", function (value, element) {
		
		var result = true;
		
		//远程信息处理
		$.ajax({
			url : url.mobileRequired,
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				mobile : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
				var message = responseText.message;
				$.validator.messages['mobileRequired'] = message !== undefined ? message : $.validator.messages['mobileRequired'];
			}
		});
		
		return !result;
	}, "手机号未注册");
	
	//注册用户验证码校验
	$.validator.addMethod("vcode", function(value, element, param) {
		
		var result = true;
		
		//远程信息处理
		$.ajax({
			url : url[param],
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				code : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
			}
		});
		return result;
	}, "请输入正确验证码");
	
	//文件格式校验
	$.validator.addMethod("fileFmt", function(value, element, param) {
		$('#' + param + 'Text-error').remove();
		var regex = /\.jpg$|\.jpeg$|\.gif$|\.png$|\.bmp$/i;
	    return this.optional(element) || (regex.test(value));
	}, "请上传2M以内的jpg、gif、bmp、png、jpeg格式图片");
	
	//数字格式校验
	$.validator.addMethod("numFmt", function(value, element) {
	    var regex = /^\d+$/;
	    return this.optional(element) || (regex.test(value));
	}, "必须为数字");
	
	//用户详细信息真实姓名校验
	$.validator.addMethod("rnfmt", function(value, element) {
	    var regex = /^[\u4e00-\u9fa5]+$/;
	    return this.optional(element) || (regex.test(value));
	}, "真实姓名必须为中文");
	
	//校验原始密码是否有效
	$.validator.addMethod('pwdIsValid', function (value, element, param) {
		var result = true;
		
		//远程信息处理
		$.ajax({
			url : url[param],
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				oldPwd : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
			}
		});
		return result;
	}, "原始密码错误");
	
	//校验新密码与老密码是否相同
	$.validator.addMethod("differentTo", function(value, element, param) {  
		var val = $(param).val();
        return this.optional(element) || (val != value);  
    }, "新密码不能与老密码重复");
	
	//邮箱格式校验，不使用validate插件提供的邮箱地址验证
	$.validator.addMethod("emailfmt", function(value, element, param) {  
		var regex = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        return this.optional(element) || (regex.test(value));  
    }, "请输入正确邮箱");
	
	//校验邮箱是否为注册邮箱
	$.validator.addMethod("emailReq", function(value, element, param) {  
		
		var result = true;
		
		//远程信息处理
		$.ajax({
			url : url[param],
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				email : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
			}
		});
		
		return result;
    }, "此邮箱未在财富街注册");
	
	//校验邮箱是否未被注册
	$.validator.addMethod("notEmailReq", function(value, element, param) {  

		var result = true;
		
		var valid = $('#emailReqValid').val();
		
		if(valid && valid == value) 
		{
			return true;
		} 
		
		//远程信息处理
		$.ajax({
			url : url[param],
			type : 'POST',
			timeout : '1000',
			async : false,
			data : {
				email : value
			},
			dataType : 'json',
			success : function (responseText) {
				result = responseText.result;
			}
		});
		return !result;
    }, "此邮箱已在财富街注册");
	
	//身份证格式校验
	$.validator.addMethod("isIdCardNo", function(value, element, param) {  
		 return this.optional(element) || idCardNoUtil.checkIdCardNo(value);   
    }, "请输入正确身份证号码");
	//邮编校验
	$.validator.addMethod("postcodeFmt", function(value, element) {  
		var regex = /^[1-9][0-9]{5}$/;
        return this.optional(element) || (regex.test(value));  
    }, "请输入正确邮编");
	
	//名片上传校验
	$.validator.addMethod("uploadRequired", function(value, element, param) {  
		var val = $(param).val();
		return this.optional(element) || (val != value);    
    }, "请选择名片上传");
	
	/*
	 * 添加失去焦点时校验，默认为只提交时校验
	 */
	$.focusout = function(element) {
		$(element).valid();
	}
	
	/*
	 * 调整错误显示的位置
	 */
	$.errorPlacement = function(error, element) {
		error.appendTo(element.parent().next());
		if (element.is(":radio")) 
		{
			error.appendTo(element.siblings("span"));
		}
		else if (element.is(":checkbox")) 
		{
			error.appendTo(element.siblings("span"));
		} 
		else
		{
			if(element.next().is('img')) 
			{
				error.css('margin-left', '110px');
			}
			error.appendTo(element.parent());
		}
	}
	
})(jQuery);