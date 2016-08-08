
function showToast(msg){
    new Toast({context:$('body'),message:msg}).show();
}
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
 }
 function get_os(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/android/i)=="android") {
		return 1;
	} else if(ua.match(/iphone/i)=="iphone") {
		return 2;
	} else {
		return 3;
	}
 }
function is_weixin(){
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		return true;
	} else {
		return false;
	}
}
function close(){
    window.opener=null;
    window.open('','_self');
    window.close();
}
function set_enable(obj, enable){
    if(enable){
        obj.removeAttr("disabled");
        obj.removeClass("ui-state-disabled");
    }else{
        obj.attr("disabled","disabled");
        obj.addClass("ui-state-disabled");
    }
}
function init_weixin(){
		
	var code = getUrlParam('code');
	if(code == null){
		code = '';	
	}
	slapi.weixin_get_signature({success: function(data){
			slapi.weixin_logined = data.logined;
			if(data.code == 0){
				wx.ready(function(){
					init_weixin_share(data.user);
				});
				wx.error(function(res){
				});
				wx.config({
			    debug: false,
			    appId: data.appid,
			    timestamp: data.timestamp,
			    nonceStr: data.noncestr,
			    signature: data.signature,
			    jsApiList: ['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','chooseWXPay']
				});
			}
		},
		error: function(xhr, type){
			
		}}, code);
}
function init_weixin_share(user){
  var url = location.origin+location.pathname;
  url = "http://m.guaguaxiche.com/"
  url += "?channel=weixinreshare";
	if(user != ""){
		url += "&from_user="+user;
	}
  var s_title = '上门洗车顶呱呱';
  var s_desc = '首次注册就送免费上门洗车券，躺在家里就能洗车了，小伙伴们速来下载呀!';
  var s_img = "http://m.guaguaxiche.com/img/share_icon.png";

	wx.onMenuShareTimeline({
	    title: s_desc,
	    link: url,
	    imgUrl: s_img,
	    success: function (res) { 
	    },
	    cancel: function (res) { 
	    },
	    trigger: function (res) {
      },
      fail:function (res) {
      }
	});
	wx.onMenuShareAppMessage({
	    title: s_title,
	    desc: s_desc,
	    link: url,
	    imgUrl: s_img,
	    type: 'link',
	    dataUrl: '',
	    success: function (res) { 
	    },
	    cancel: function (res) { 
	    },
	    trigger: function (res) {
      },
      fail:function (res) {
      }
	});
	
	wx.onMenuShareQQ({
	    title: s_title,
	    desc: s_desc,
	    link: url,
	    imgUrl: s_img,
	    success: function (res) { 
	    },
	    cancel: function (res) {
	    },
	    trigger: function (res) {
      },
      fail:function (res) {
      }
	});
	
	wx.onMenuShareWeibo({
	    title: s_title,
	    desc: s_desc,
	    link: url,
	    imgUrl: s_img,
	    success: function (res) { 
	    },
	    cancel: function (res) { 
	    },
	    trigger: function (res) {
      },
      fail:function (res) {
      }
	});
	
}