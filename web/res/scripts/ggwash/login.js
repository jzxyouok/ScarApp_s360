var login_callback;
function login_logout(callback){
    slapi.user_logout({success: function(data){
		if(data.code == 0){
		    if(callback){
		        callback();
		    }
		}else{
            if(data.prompt){
                showToast(data.prompt);
            }else{
                showToast(data.message);   
            }
		}
	},
	error: function(xhr, type){
		showToast('网络错误，请重试');
	}});
}
function login_show_login(callback){
    if(!$("#login")){
         return;  
    }
    login_callback = callback;
    var html = "<div id='login_dialog'>";
    html += "<div class='login_line' style='height:auto;'><font color='#39cd89'>呱呱洗车承诺严格保密车主隐私</font></div>";
    html += "<div class='login_line'><center><input type='text' class='login_input' id='mobile' placeholder='请输入手机号' style='width:70%'/></center></div>";
    html += "<div class='login_line'><center><input id='send' type='text' class='login_btn' value='获取验证码' readonly='readonly' onclick='login_send_code()' style='width:70%' /></center></div>";
    html += "<div class='login_line'><center><input class='login_input' id='code' type='text' style='width:45%' placeholder='请输入验证码' /><input id='vc' type='text' class='login_btn' style='width:20%' style='margin-left:5px' value='登录' onclick='login_vc_code()'readonly='readonly'  /></center></div>";
    html += "</div>";
    $("#login").html(html);
    $("#login").show();
}

function login_vc_code(){
    set_enable($("#vc"), false);
    $("#vc").val("登录中……");
    slapi.user_vcode_validate({success: function(data){
		if(data.code == 0){
		    showToast('登录成功');
		    $("#login").hide();
		    if(login_callback){
		        login_callback();
		    }
		}else{
            if(data.prompt){
                showToast(data.prompt);
            }else{
                showToast(data.message);   
            }
            $("#vc").val("登录");
            set_enable($("#vc"), true);
		}
	},
	error: function(xhr, type){
		showToast('网络错误，请重试');
        $("#vc").val("获取验证码");
        set_enable($("#vc"), true);
	}},$("#mobile").val(), $("#code").val());
}
var nIntervId=-1;
var seconds = 0;
function login_starttick(){
    nIntervId = setInterval(login_flashCode, 1000);
}
function login_flashCode(){
    $("#send").val('验证码发送成功(' + (seconds)  + ')');
    seconds --;
    if(seconds <= 0){
        $("#send").val("获取验证码");
        set_enable($("#send"), true);
        if(nIntervId != -1) clearInterval(nIntervId);
        nIntervId = -1;
    }
}
function login_send_code(){
    set_enable($("#send"), false);
    $("#send").val("验证码发送中……");
    slapi.user_vcode_send({success: function(data){
		if(data.code == 0){
		    showToast('验证码发送成功');
		    seconds = data.ttl;
            login_starttick();
            $("#vc").show();
		}else{
            if(data.prompt){
                showToast(data.prompt);
            }else{
                showToast(data.message);   
            }
            $("#send").val("获取验证码");
            set_enable($("#send"), true);
		}
	},
	error: function(xhr, type){
		showToast('网络错误，请重试');
        $("#send").val("获取验证码");
        set_enable($("#send"), true);
	}},$("#mobile").val());
}