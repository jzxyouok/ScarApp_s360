var couponbuy_callback;

function has_weixin_pay(){
	return slapi.weixin_logined;
	/*
	var ua = navigator.userAgent.toLowerCase();
		if(ua.match(/MicroMessenger/i)=="micromessenger") {
	return true;
	} else {
		return false;
	}
	*/
}
function couponbuy_show_couponbuy(callback){
    if(!$("#couponbuy")){
         return;  
    }
    couponbuy_callback = callback;
    
    var html = "<div id='couponbuy_dialog'><div id='couponbuy_box'>";
    html += "<div id='couponbuy_title'>购买张数<span onclick='couponbuy_hide();'>取消</span></div>";
    html += "<div id='couponbuy_list'></div>";
    html += "<div id='couponbuy_buy' style='width:100%'><input id='couponbuy_btn_alipay' style='width:90%' class='lcouponbuy_btn' type='button' onclick='couponbuy_alipay()' value='支付宝支付' /><br><br>";
    html += "<input id='couponbuy_btn_weixin' style='width:90%' class='lcouponbuy_btn' type='button' onclick='couponbuy_weixin()' value='微信支付' /></div>";
   	html += "<div id='couponbuy_buy_busy' style='width:100%;display:none;'>请求中……</div></div></div>";
    $("#couponbuy").html(html);
    $("#couponbuy").show();
    couponbuy_show_buy_list();
}
function couponbuy_hide(){
	if(couponbuy_callback && couponbuy_callback.cancel){
		couponbuy_callback.cancel();
	}
	$("#couponbuy").hide();
}
function couponbuy_select(typeid){
		var last = $("input[name='typeid']:checked");
		var now = $("#typeid_"+typeid);
		if(now){
			last.removeAttr('checked');
			now.attr('checked' ,true);
		}
}
function couponbuy_show_buy_list(){
	if (has_weixin_pay()){
		$('#couponbuy_btn_weixin').show();
	}else{
		$('#couponbuy_btn_weixin').hide();
	}
	set_enable($("#couponbuy_btn_alipay"), false);
	set_enable($("#couponbuy_btn_weixin"), false);
    slapi.pay_type_list({success: function(data){
		if(data.code == 0){
		    if(data.types && data.types.length > 0){
		    	var html = "<table>";
		    	for(var i = 0; i < data.types.length; i++){
		    		var c = "";
		    		if(i == 0){
		    			c = "checked";
		    		}
		    		html += "<tr onclick='couponbuy_select(\""+data.types[i].typeid+"\")'><td><input type='radio' name='typeid' id='typeid_"+data.types[i].typeid+"' "+c+" value='"+data.types[i].typeid+"'/></td><td>"+data.types[i].desc+"</td><td>"+data.types[i].needpay_amount+"元</td><td>(<del><font color='#ccc'>原价"+data.types[i].origin_amount+"元</font></del>)</td></tr>"
		    	}
		    	html += "</table>";
		    	html += "<div id='couponbuy_desc'>购买之日起，有效期一年</div>";
		    	$("#couponbuy_list").html(html);
		    	/*
		        var typeid = data.types[0].typeid;
	            var couponshow = "支付方式：洗车券一张";
	            
	            couponshow += "<br>"+data.types[0].needpay_amount+"元<del><font color='#cccccc'>(原价"+data.types[0].origin_amount+"元)</font></del>";
		        $("#coupon_show").html(couponshow);
		        $("#buycoupon").attr("onclick","buycoupon('"+typeid+"');");
		        $("#buycoupon").show();
		        $("#order").hide();
		        */
					set_enable($("#couponbuy_btn_alipay"), true);
					set_enable($("#couponbuy_btn_weixin"), true);
					if(data.coupon_buy_msg){
						alert(data.coupon_buy_msg);
					}
    		}else{
    		    showToast("获取购买列表失败"); 
    		    couponbuy_hide(); 
    		}
		}else{
        if(data.prompt){
            showToast(data.prompt);
        }else{
            showToast(data.message);
        }
        couponbuy_hide();
		}
	},
	error: function(xhr, type){
	    showToast("网络错误，请重试");
	}});   
}
function couponbuy_busy(busy){
	if(busy){
		$("#couponbuy_buy_busy").show();
		$("#couponbuy_buy").hide();
	}else{
		$("#couponbuy_buy_busy").hide();
		$("#couponbuy_buy").show();
	}
}
function couponbuy_alipay(){
		paytype = $("input[name='typeid']:checked").val();
    var url = location.origin+location.pathname;
    couponbuy_busy(true);
    if(couponbuy_callback && couponbuy_callback.save_param){
    		couponbuy_callback.save_param();
    }
    var call_back_url = url + "?pay=ok";
    var merchant_url = url + "?pay=merchant";
    slapi.pay_prepay_alipay({success: function(data){
		if(data.code == 0){
		    location.href=data.alipay;
    		couponbuy_busy(false);
		}else{
        if(data.prompt){
            showToast(data.prompt);
        }else{
            showToast(data.message);
        }
    		couponbuy_busy(false);
		}
	},
	error: function(xhr, type){
	    showToast("网络错误，请重试");
    	couponbuy_busy(false);
	}},paytype, call_back_url, merchant_url);
}

function couponbuy_weixin(){
		paytype = $("input[name='typeid']:checked").val();
    couponbuy_busy(true);
    slapi.pay_prepay_weixin({success: function(data){
		if(data.code == 0){
		    
		    wx.chooseWXPay({
				    timestamp: data.timeStamp,
				    nonceStr: data.nonceStr,
				    package: data.package,
				    signType: data.signType,
				    paySign: data.paySign,
				    success: function (res) {
				        location.reload(true);
				    },
				    fail: function(res){
    					couponbuy_busy(false);
				    },
				    cancel: function(res){
    					couponbuy_busy(false);
				    }
				});
		}else{
        if(data.prompt){
            showToast(data.prompt);
        }else{
            showToast(data.message);
        }
    		couponbuy_busy(false);
		}
	},
	error: function(xhr, type){
	    showToast("网络错误，请重试");
    	couponbuy_busy(false);
	}},paytype);
}