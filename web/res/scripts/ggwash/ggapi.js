slapi = {
	server : "",
	channel : "",
	platform : 3,
	format : 'jsonp',
	lat : 0,
	lng : 0,
    gloab_lat : 0,
    gloab_lng : 0,
	weixin_logined : false,
	
	get_location : function(callback){
	    var geol;         
	    try {  
	        if (typeof(navigator.geolocation) == 'undefined') {  
	            geol = google.gears.factory.create('beta.geolocation');  
	        } else {  
	            geol = navigator.geolocation;  
	        }  
	    } catch (error) {  
	        callback(0,0,"定位错误："+error.message+"。");
	        return;
	    }  
	      
	    if (geol) {  
	        geol.getCurrentPosition(function(position) {  
	      
	            var nowLatitude = position.coords.latitude;  
	            var nowLongitude = position.coords.longitude;  
	
	            callback(nowLatitude,nowLongitude,"");
	            
	        }, function(error) {  
	            switch(error.code){  
	            case error.TIMEOUT :  
	                callback(0,0,"定位连接超时。"); 
	                break;  
	            case error.PERMISSION_DENIED :  
	                callback(-1,-1,"您拒绝了定位请求。"); 
	                break;  
	            case error.POSITION_UNAVAILABLE :    
	                callback(0,0,"我们暂时无法获取您的位置。");
	                break;  
	            }  
	        }, {timeout:10000});
	    }
	},
	init : function(){
    	var c = getUrlParam('channel');
    	if(!c){
    	    c = 'wapdefault';
    	}
		this.server = "http://slapi.guaguaxiche.com";
		//this.server = "http://dev.guaguaxiche.com:8000";
		this.channel = c;
		if(is_weixin()){
			init_weixin();
		}
		this.get_location(function(lat,lon,msg){
            if(lat > 0 && lon > 0){
				this.lat = lat;
				this.lng = lon;
            	slapi.gloab_lat = lat;
            	slapi.gloab_lng = lon;
        	}
		});
		
	},
	conn : function(p_type, p_url, p_data, listener){
	    //if(p_isjson){
    	//    ct = "application/json";
    	//}
//else{
    	//    ct = "application/x-www-from-urlencode";   
    	//}
    p_data["rand"] = new Date().getTime();
    p_data["user_lat"] = slapi.gloab_lat;
    p_data["user_lon"] = slapi.gloab_lng;
		$.ajax({
			type: p_type,
			url: p_url,
			data: p_data,
			//contentType: ct,
			dataType: this.format,
			//crossDomain: true,
			//xhrFields: {
			//	withCredentials: true
			//},
			timeout: 30000,
			context: $('body'),
			success: function(data){
				if(listener && listener.success){
					listener.success(data);
				}
			},
			error: function(xhr, type){
				if(listener && listener.error){
					listener.error(xhr, type);
				}
			}
		})	
	},
	user_logout : function(listener){
		type = 'GET';
		url = this.server+'/slapi/user/logout';
		data = {format: this.format,'platform':this.platform, 'channel':this.channel};
		this.conn(type, url, data, listener);
	},
	user_vcode_send : function(listener, mobile){
	    if(!mobile){
	        if(listener && listener.success){
	            listener.success({code:-1,message:"请输入手机号"});   
	        }
	        return;   
	    }
		type = 'GET';
		url = this.server+'/slapi/user/vcode/send';
		data = {format: this.format, 'mobile': mobile,'platform':this.platform, 'channel':this.channel};
		this.conn(type, url, data, listener);
	},
	user_vcode_validate : function(listener, mobile, vc){
	    if(!mobile){
	        if(listener && listener.success){
	            listener.success({code:-1,message:"请输入手机号"});   
	        }
	        return;   
	    }
	    if(!vc){
	        if(listener && listener.success){
	            listener.success({code:-1,message:"请输入验证码"});   
	        }
	        return;   
	    }
	    if(slapi.gloab_lat <= 0 || slapi.gloab_lng <= 0){
	        if(listener && listener.success){
	            listener.success({code:-1,message:"请定位成功后再登录"});   
	        }
	        return;  
	    }
       
		type = 'GET';
		url = this.server+'/slapi/user/vcode/validate';
		data = {format:this.format,'mobile':mobile,'vc':vc,'platform':this.platform,'version':'1.0.0.0','channel':this.channel};
		this.conn(type, url, data, listener);
	},
	user_profile : function(listener){
		type = 'GET';
		url = this.server+'/slapi/user/profile';
		data = {format:this.format,'platform':this.platform,'channel':this.channel,'carorder':1};
		this.conn(type, url, data, listener);
	},
	user_car_save : function(listener, id, name, color, brand, model, plate){
		type = 'GET';//'POST';
		url = this.server+'/slapi/user/car/save?autonew=1&format='+this.format+'&platform='+this.platform+'&channel='+this.channel;
		json = {'id':id, 'name':name, 'color':color, 'brand':brand, 'model':model, 'plate':plate};
		data = {'json':JSON.stringify(json)};
		this.conn(type, url, data, listener);
	},
	order_preorder : function(listener, lat, lon, coupon){
		type = 'GET';
		url = this.server+'/slapi/order/preorder';
		data = {format:this.format,'lon':lon,'lat':lat,'coupon':coupon,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);
	},
	order_add : function(listener, order_lat, order_lon, user_lat, user_lon, region, address, comment, couponid, carid,clean_inside){
		type = 'GET';//'POST';
		url = this.server+'/slapi/order/add?format='+this.format+'&platform='+this.platform+'&channel='+this.channel;
		data = {'channel':this.channel,'order_lon':order_lon,'order_lat':order_lat,'user_lon':user_lon,'user_lat':user_lat,'region':region,'address':address,'comment':comment,'carid':carid,'couponid':couponid,'clean_inside':clean_inside};
		this.conn(type, url, {'json':JSON.stringify(data)}, listener);
	},
	order_detail : function(listener, orderid){
	    this.order_list({success:function(data){
	            if(data.code == 0){
	                if(data.orders){
	                    for(var i = 0; i < data.orders.length; i ++){
	                        if(data.orders[i].orderid == orderid){
	                            data.order = data.orders[i];   
	                        }
	                    }   
	                }
	                if(!data.order){
	                    data.code = -1;
	                    data.message = "订单未找到";   
	                }
	            }
	            if(listener && listener.success){
					listener.success(data);
				}
	        },error:function(xhr, type){
	            if(listener && listener.error){
					listener.error(xhr, type);
				}
	        }})
	},
	order_list : function(listener){
        type = 'GET';
		url = this.server+'/slapi/order/list';
		data = {format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);   
	},
	order_refund : function(listener, orderid){
        type = 'GET';
		url = this.server+'/slapi/order/refund';
		data = {'orderid':orderid,format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);   
	},
	order_comment_add : function(listener, orderid, rating, comment, rating_swift, rating_attitude){
			if(!check_rating(rating) || !check_rating(rating_swift) || !check_rating(rating_attitude)){
	        if(listener && listener.success){
	            listener.success({code:-1,message:"请选择评价"});   
	        }
	        return;   
	    }
    type = 'GET';
		url = this.server+'/slapi/order/comment/add?format='+this.format+'&platform='+this.platform+'&channel='+this.channel;
		data = {'orderid':orderid, 'rating':rating, 'rating_swift':rating_swift, 'rating_attitude':rating_attitude, 'comment':comment};
		this.conn(type, url, data, listener);   
	}, 
	pay_prepay_alipay : function(listener, paytype, call_back_url, merchant_url){
		type = 'GET';
		url = this.server+'/slapi/pay/prepay';
		data = {'pay_platform':3,'pay_type':paytype,'call_back_url':call_back_url,'merchant_url':merchant_url,format:this.format,'channel':this.channel};
		this.conn(type, url, data, listener);   
	}, 
	pay_prepay_weixin : function(listener, paytype){
		type = 'GET';
		url = this.server+'/slapi/pay/prepay';
		data = {'pay_platform':4,'pay_type':paytype,format:this.format,'channel':this.channel};
		this.conn(type, url, data, listener);   
	},
	pay_type_list : function(listener){
    type = 'GET';
		url = this.server+'/slapi/pay/type_list';
		data = {'groupid':2,format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);   
	},
	misc_c :function(listener, a, b){
    type = 'GET';
		url = this.server+'/slapi/misc/c';
		data = {'a':a,'b':b,format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);
	},
	coupon_list : function(listener){
    type = 'GET';
		url = this.server+'/slapi/coupon/list';
		data = {format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);
	},coupon_bind : function(listener, code){
    if(!code){
        if(listener && listener.success){
            listener.success({code:-1,message:"请输入兑换码"});   
        }
        return;   
    }
    type = 'GET';
		url = this.server+'/slapi/coupon/bind';
		data = {'code':code,format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);
	},weixin_get_signature:function(listener,code){
		type = 'GET';
		url = this.server+'/slapi/weixin/get_signature';
		data = {format:this.format,'platform':this.platform,'channel':this.channel,'code':code};
		this.conn(type, url, data, listener);	
	},car_models:function(listener){
		type = 'GET';
		url = this.server+'/slapi/user/car/models';
		data = {format:this.format,'platform':this.platform,'channel':this.channel};
		this.conn(type, url, data, listener);	
	}
}
slapi.init();
function check_rating(rating){
	if(isNaN(rating)){
		return false;	
	}
	rating = parseInt(rating);
	return [10, 20, 30].indexOf(rating) >= 0;
}