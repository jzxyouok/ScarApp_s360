
function get_rgeocode(lat, lng){
    lnglatXY = new AMap.LngLat(lng, lat);
    AMap.service(["AMap.Geocoder"], function() {        
        MGeocoder = new AMap.Geocoder({ 
            radius: 1000,
            extensions: "all"
        });
        MGeocoder.getAddress(lnglatXY, function(status, result){
        	if(status === 'complete' && result.info === 'OK'){
        	    var addr = result.regeocode.formattedAddress;
        	    //addr = addr.replace(result.regeocode.addressComponent.districe,"");
        	    addr = addr.replace(result.regeocode.addressComponent.province,"");
        	    $("#address").val(addr);
        	    $("#maplocation").html(addr);
        	}
        });
    });
}
function get_location(callback){
    //showToast("获取位置中……");
    
    //callback(39.958275,116.444511,"");
    //return;
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
            //showToast("error:"+JSON.stringify(error));
            switch(error.code){  
            case error.TIMEOUT :  
                //alert("连接超时，请重试"); 
                callback(0,0,"定位连接超时。"); 
                break;  
            case error.PERMISSION_DENIED :  
                //alert("您拒绝了使用位置共享服务，查询已取消");  
                callback(-1,-1,"您拒绝了定位请求。"); 
                break;  
            case error.POSITION_UNAVAILABLE :   
                //alert("非常抱歉，我们暂时无法通过浏览器获取您的位置信息");  
                callback(0,0,"我们暂时无法获取您的位置。");
                break;  
            }  
        }, {timeout:10000});    //设置十秒超时  
    }
}
function order(){
    car_save(function (){
    		if($("#order_errmsg").val()){
    			showToast($("#order_errmsg").val());
    			return;	
    		}
    		if($("#order_warnmsg").val()){
    			if(!confirm($("#order_warnmsg").val())){
    				return;	
    			}	
    		}
        slapi.order_add({
        success: function(data){
			if(data.code == 0){
			    $("#ordernum").html(data.order.ordernum);
			    $("#orderprice").html(data.order.origin_amount+"元(洗车券抵扣)");
			    var eta = data.order.eta;
			    eta = eta.replace("T"," ");
			    var i = eta.lastIndexOf(":");
			    if(i > 0){
			        eta = eta.substr(0, i);   
			    }
			    $("#ordereta").html(eta);
			    $("#orderinfo").hide();
			    $("#eta").hide();
			    $("#order_ok").show();
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
		}
        },$("#lat").val(), $("#lon").val(),$("#lat").val(), $("#lon").val(), $("#region").val(), $("#address").val(), $("#comment").val(), $("#coupon").val(), $("#carid").val(),$('#cleanCheckBox').is(':checked')?1:0);
    });
}
/*
function locationok(data){
    $("#lat").val(lat);
    $("#lon").val(lon);
    get_rgeocode(lat,lon);
    order_preorder(false);   
}
*/
function showlocation(){
    if($("#address").val()){       
        showToast($("#address").val());
    }
}
function set_order_enable(enable,msg){
	$("#order_errmsg").val(msg);
	var obj = $("#order");
  if(enable){
      obj.removeClass("order-ui-state-disabled");
      $("#order_errmsg").val("");
  }else{
	   	obj.addClass("order-ui-state-disabled");
  }	
}

function clean_change(){
    if($('#cleanCheckBox').is(':checked')){
        $('#cleanText').val('车内简洗(需在车旁等待)');
        $('#cleanText').css('color','#FF0000');
    } else {
        $('#cleanText').val('车内简洗(无需车旁等待)');
        $('#cleanText').css('color','#000000');
    }
}
function set_location(lat, lon){
	$("#lat").val(lat);
  $("#lon").val(lon);
  get_rgeocode(lat,lon);
  order_preorder(false);
}
var locok = false;
function showmap(){
	if(locok){
		select_location(set_location);
	}
}
function order_preorder(checklocation){
    $("#eta").html("服务时间预计中……");
    $("#mapeta").html("服务时间预计中……");
    if(checklocation){
        get_location(function(lat,lon,msg){
        		locok = true;
        		//$("#showmap").click(function(){select_location(set_location);});
            if(lat == -1 && lon == -1){
                alert(msg+"请关闭后重新打开页面，并允许获取位置");   
                $("#lat").val(lat);
                $("#lon").val(lon);
                $("#address").val(msg);
                $("#maplocation").html(msg);
                $("#eta").html(msg);
                $("#mapeta").html(msg);
                return;
            }
            if(lat == 0 && lon == 0){
                if(msg == ""){
                    msg = "定位失败。";   
                }
                if(confirm(msg+"对洗车员寻找车辆速度会有影响，是否重试？")){
                    order_preorder(true); 
                }else{
                    $("#lat").val(lat);
                    $("#lon").val(lon);
                    $("#address").val(msg);
                    $("#maplocation").html(msg);
                    $("#eta").html(msg);
                    $("#mapeta").html(msg);
                    //order_preorder(false);
                }
            }else{
                slapi.misc_c({success:function(data){
                        if(data.code == 0){
                            lon = data.a;  
                            lat = data.b; 
                        }
                        $("#lat").val(lat);
                        $("#lon").val(lon);
                        get_rgeocode(lat,lon);
                        order_preorder(false);
                    },error:function(){
                        $("#lat").val(lat);
                        $("#lon").val(lon);
                        get_rgeocode(lat,lon);
                        order_preorder(false);
                    }},lon,lat);
            }
        });
        return;
    }
    slapi.order_preorder({success: function(data){
		if(data.code == 0){
		    if(data.ETA.stop_service){
		        $("#eta").html(data.ETA.stop_description);
		        $("#mapeta").html(data.ETA.stop_description);
    		    $("#coupon_show").html("");
    		    $("#buycoupon").hide();
		        set_order_enable(false, data.ETA.stop_description);
		    }else{
    		    $("#eta").html(data.ETA.etaPrompt);
    		    $("#mapeta").html(data.ETA.etaPrompt);
		        if(data.coupon){
		            var couponshow = "支付方式：洗车券一张";
		            if(data.coupon.special == 1){
		                couponshow += "(赠送)";
    		            $("#order").html("免费下单");
		            }
		            couponshow += "<del><font color='#cccccc'>(原价"+data.orderinfo.amount+"元)</font></del>";
    		        $("#coupon_show").html(couponshow);
    		        $("#buycoupon").hide();
    		        $("#coupon").val(data.coupon.id);
		        		set_order_enable(true, "");
		            
    		    }else{
    		    	disable_car_info();
    		    	show_buy();
    		      //show_buy_list();
    		    }
    		    if(data.ETA.needReserve && data.ETA.reserveMsg){
    		     	$("#order_warnmsg").val(data.ETA.reserveMsg);
    		    }else{
    		     	$("#order_warnmsg").val("");
    		    }
    		    $("#region").val(data.region.region_code);
    		}
		}else{
			var msg = data.prompt;
      if(data.prompt){
          $("#eta").html(data.prompt);
          $("#mapeta").html(data.prompt);
      }else{
          $("#eta").html(data.message);
          $("#mapeta").html(data.message);
          msg = data.message;
      }
      $("#coupon").html("");
      
      set_order_enable(false, msg);
		}
	},
	error: function(xhr, type){
	    $("#eta").html("网络错误，请重试");
	    $("#mapeta").html("网络错误，请重试");
	}}, $("#lat").val(), $("#lon").val(), $("#coupon").val());
}
function goto_buycoupon(){
	location.href="couponlist.html"+location.search;	
}
function show_buy(){
		var couponshow = "洗车券不足，请到洗车券界面购买";
    $("#coupon_show").html(couponshow);
    $("#buycoupon").attr("onclick","goto_buycoupon();");
    $("#buycoupon").show();
    $("#order").hide();
}
function show_buy_list(){
    slapi.pay_type_list({success: function(data){
		if(data.code == 0){
		    if(data.types && data.types.length > 0){
		        var typeid = data.types[0].typeid;
	            var couponshow = "支付方式：洗车券一张";
	            
	            couponshow += "<br>"+data.types[0].needpay_amount+"元<del><font color='#cccccc'>(原价"+data.types[0].origin_amount+"元)</font></del>";
		        $("#coupon_show").html(couponshow);
		        $("#buycoupon").attr("onclick","buycoupon('"+typeid+"');");
		        $("#buycoupon").show();
		        $("#order").hide();
    		}else{
    		    showToast("获取购买列表失败");  
    		}
		}else{
            if(data.prompt){
                showToast(data.prompt);
            }else{
                showToast(data.message);
            }
            $("#buycoupon").val("购买洗车券");
		}
	},
	error: function(xhr, type){
	    showToast("网络错误，请重试");
	}});   
}
function DeleteUrlParam(url,name) {
    var reg = new RegExp("([&\?]?)" + name + "=[^&]+(&?)", "g")

    var newUrl = url.replace(reg, function (a, b, c) {
        if (c.length == 0) {
            return '';
        } else {
            return b;
        }
    });

    return newUrl;
}
function ggdecode(p){
    return decodeURIComponent(decodeURIComponent(p));
}
function ggencode(p){
    return encodeURIComponent(encodeURIComponent(p));
}
function goto_alipay(paytype){
    var url = location.origin+location.pathname;
    var param = location.search;
    if(param.indexOf("?")==0){
        param = param.substr(1);
    }
    if(param){
        param += "&";   
    }
    param = DeleteUrlParam(param, "comment");
    param = DeleteUrlParam(param, "pay");
    if($("#comment").val()){
        param += "comment="+ggencode($("#comment").val());
    }
    $.fn.cookie('param', param);
    var call_back_url = url + "?pay=ok";
    var merchant_url = url + "?pay=merchant";
    slapi.pay_prepay({success: function(data){
		if(data.code == 0){
		    location.href=data.alipay;
		}else{
            if(data.prompt){
                showToast(data.prompt);
            }else{
                showToast(data.message);
            }
            $("#buycoupon").val("购买洗车券");
            set_enable($("#buycoupon"), true);
		}
	},
	error: function(xhr, type){
	    showToast("网络错误，请重试");
        $("#buycoupon").val("购买洗车券");
        set_enable($("#buycoupon"), true);
	}},paytype, call_back_url, merchant_url);
}
function buycoupon(typeid){
    $("#buycoupon").val("请求中……");
    set_enable($("#buycoupon"), false);
    car_save_inner(function(){
        goto_alipay(typeid);
    });
}
function disable_car_info(){
    set_enable($("#carid"), false);
    set_enable($("#brand_model_show"), false);
    set_enable($("#brand"), false);
    set_enable($("#model"), false);
    set_enable($("#color"), false);
    set_enable($("#plate"), false);
    set_enable($("#name"), false);
    set_enable($("#comment"), false);
    //set_order_enable(false, msg);
    //set_enable($("#order"), false);
}
function enable_car_info(){
    set_enable($("#carid"), true);
    set_enable($("#brand_model_show"), true);
    set_enable($("#brand"), true);
    set_enable($("#model"), true);
    set_enable($("#color"), true);
    set_enable($("#plate"), true);
    set_enable($("#name"), true);
    set_enable($("#comment"), true);
    //set_enable($("#address"), true);
    if($("#lat").val() == 0 && $("#lon").val() == 0){
    	set_order_enable(false, "定位未成功");
    }
}
function show_car_info(car){
    if(car){
        $("#carid").val(car.id);
        $("#brand").val(car.brand);
        $("#model").val(car.model);
        $("#color").val(car.color);
        $("#plate").val(car.plate);
        $("#brand_model_show").val(car.brand+" "+car.model);
    }else{
        $("#carid").val(-1);
        $("#brand").val("");
        $("#model").val("");
        $("#color").val("");
        $("#plate").val("");
        $("#brand_model_show").val("");
    }
}
function car_save_inner(callback){
    var lis = null;
    if(callback){
        lis = {success:function(data){
                    callback();
                },error:function(){
                    callback();
               }};
    }
    slapi.user_car_save(lis,$('#carid').val(), $('#name').val(), $('#color').val(), $('#brand').val(), $('#model').val(), $('#plate').val()); 
}
function car_save(callback){
    slapi.user_car_save({success:function(data){
            if(data.code == 0){
                if(data.car){
                    show_car_info(data.car);
                    callback();
                }
                enable_car_info();
            }else{
                if(data.prompt){
                    showToast(data.prompt);
                }else{
                    showToast(data.message);   
                }
            }
        },
        error:function(xhr, type){
		    showToast('网络错误，请重试');
        }},$('#carid').val(), $('#name').val(), $('#color').val(), $('#brand').val(), $('#model').val(), $('#plate').val());  
}
function show_login_dialog(){
    $("#orderinfo").hide();
    $("#eta").hide();
    login_show_login(function(){
        $("#orderinfo").show();
        $("#eta").show();
        user_profile();   
    });
}
function logout(){
		if(!confirm("确认退出登陆？")){
			return;	
		}
    login_logout(show_login_dialog);
}
function user_profile(){
    slapi.user_profile({success:function(data){
            document.body.style.display = 'block';
            if(data.code == 0){
                set_enable($("#logout"), true);
                $("#mobile_show").val(data.profile.mobile);
                $("#name").val(data.profile.name);
                if(nIntervId != -1) clearInterval(nIntervId);
                nIntervId = -1;
                if(data.cars && data.cars.length > 0){
                    show_car_info(data.cars[0]);//data.cars.length-1]);
                }else{
                    show_car_info(null);
                    $("#carid").val('-1');
                }
                enable_car_info();
                order_preorder(true);
            }else{
                show_login_dialog();
            }
        },
        error:function(xhr, type){
            document.body.style.display = 'block';
            show_login_dialog();
        }});   
}
var models = null;
function hide_brand(){
    $("#orderinfo").show();
    $("#eta").show();
    $("#list").hide();
}
function select_brand_model(b, m){
    $("#brand").val(b);
    $("#model").val(m);
    $("#brand_model_show").val(b+" "+m);
    $("#orderinfo").show();
    $("#eta").show();
    $("#list").hide();
}
function show_model(b, m){
    var html = "<ul>";
    html += "<li>";
    html += "<ul>"
    html += "<li><a href='javascript:show_brand()'>后退</a></li>";  
    for(var k in m){
        k = m[k];
        html += "<li><a href='javascript:select_brand_model(\""+b+"\", \""+k+"\")'>"+k+"</a></li>";   
    }
    html += "</ul>";
    html += "</li>";
    html += "</ul>";
    $("#list_content").html(html);
    $("#list").show();
    $("#orderinfo").hide();
    $("#eta").hide();
}
function show_brand(){
    var html = "<ul>";
    var item = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
    var item_out = []; 
    html += "<li>";
    html += "<ul>"
    html += "<li><a href='javascript:hide_brand()'>后退</a></li>"; 
    html += "</ul>";
    html += "</li>";
    for(var k in item){
        k = item[k];
        if(!models[k]){
            continue;   
        }
        item_out.push(k);
        html += "<li id='"+k+"'><a name='"+k+"' class='title'>"+k+"</a>";
        html += "<ul>"
        for(var model in models[k]){
            html += "<li><a href='javascript:show_model(\""+model+"\", models[\""+k+"\"][\""+model+"\"])'>"+model+"</a></li>";   
        }
        html += "</ul>";
        html += "</li>";
    }
    html += "</ul>";
    /*
    html += '<div id="list-nav"><ul>';
    for(var i in item_out) {
        html += "<li><a alt='#"+item_out[i]+"'>"+item_out[i]+"</a></li>";
    }
    html += '</ul></div>';
    */
    $("#list_content").html(html);
    /*
	$('#slider-nav a').mouseover( function(event){
    
		var target = $(this).attr('alt');
		var cOffset = $('#slider-content').offset().top;
		var tOffset = $('#slider-content '+target).offset().top;
		var height = $('#slider-nav', slider).height();
		var pScroll = (tOffset - cOffset) - height/8;
		
		//$('.slider-content', slider).scrollTo(pScroll) ;
		$('#slider-content').scrollTo(pScroll) ;
		//$('.slider-content', slider).stop().animate({scrollTop: '+=' + pScroll + 'px'});
    });
    */
    $("#list").show();
    $("#orderinfo").hide();
    $("#eta").hide();
}
function init_models(){
	if($("#brand_model_show").attr("disabled")){
		return;	
	}
  if(models){
      show_brand();
  }else{
	
		slapi.car_models({success:function(data){
			models = {};
			for(var k in data.models){
				var fpinyin = data.models[k].pinyin.substr(0,1);
				if(!models[fpinyin]){
					models[fpinyin] = {};
				}
				models[fpinyin][data.models[k].name] = [];
				for(m in data.models[k].models){
					models[fpinyin][data.models[k].name].push(data.models[k].models[m].name);	
				}
			}
			show_brand();
    },
    error:function(xhr, type){
			showToast("网络错误，请重试");
    }});
  }
}
window.onload = function() {
    var pay = getUrlParam("pay");
    if(pay != null){
        var p = $.fn.cookie('param');
        if(p){
            location.search += "&"+p;
            $.fn.cookie('param', null);
        }
    }
    var comment = getUrlParam("comment");
    if(comment){
        $("#comment").val(ggdecode(comment));
    }
    var coupon = getUrlParam("coupon");
    if(coupon){
    	$("#coupon").val(coupon);	
    }
	user_profile();
}