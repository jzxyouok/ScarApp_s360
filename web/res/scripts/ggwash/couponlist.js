function show_couponlist(){
	slapi.coupon_list({success: function(data){
		if(data.code == 0){
			$("#couponnum").html("您有"+data.coupons.length+"张洗车券");
			if(data.coupons.length > 0){
				var html = "";
				for(var i = 0; i < data.coupons.length; i++){
					var coupon = data.coupons[i];
					var im = "";
					if(coupon.special == 0){
						if(coupon.isexpire){
							im = "coupon_normal_overdue.png";
						}else{
							im = "coupon_normal.png";	
						}
					}else if(coupon.special == 1){
						if(coupon.isexpire){
							im = "coupon_largess_overdue.png";
						}else{
							im = "coupon_largess.png";	
						}
					}else{
						continue;
					}
					html += "<div class='sub_coupon' style='position:relative;' id='coupon_"+coupon.id+"'>";
					if(!coupon.isexpire){
						html += "<a href='order.html?coupon="+coupon.id+"&channel="+slapi.channel+"'>";
					}
					html += "<img src='img/"+im+"' />";
					var expir = coupon.expiration;
					var t = expir.indexOf("T");
					if(t > 0){
						expir = expir.substr(0,t);
					}
					exp_day = "";
					if(coupon.exp_days <= 0){
						exp_day = "已过期";	
					}else{
						exp_day = coupon.exp_days + "天后过期";
					}
					
					html += "<div style='width:60%;left:8%;bottom:20%;position: absolute;font-size:12px;'><span style='float:left;'>"+exp_day+"</span> <span style='float:right;'>有效期至"+expir+"</span></div>";
					
					if(!coupon.isexpire){
						html += "</a>";
					}
					html += "</div>";
				}
				$("#couponlist").show();
				$("#couponlist").html(html);
				$("#nocoupon").hide();
			
			}else{
				$("#couponlist").hide();
				$("#nocoupon").show();
			}  
	  }else{
	  		if(data.code == 60001){
            login_show_login(show_couponlist); 
        }else if(data.prompt){
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
function bind_coupon(){
	var code = $("#couponcode").val();
	slapi.coupon_bind({success: function(data){
		if(data.code == 0){
			showToast('兑换成功');
			show_couponlist();
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
	}},code);
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
function show_couponbuy(){
	$("#couponlist").hide();
	couponbuy_show_couponbuy({cancel:function(){
			$("#couponlist").show();
		},save_param:function(){
		var param = location.search;
    if(param.indexOf("?")==0){
        param = param.substr(1);
    }
    param = DeleteUrlParam(param, "pay");
    $.fn.cookie('param', param);
	}});
}

function get_location(callback) {
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
                    callback(0,0,"定位连接超时。");
                    break;
                case error.PERMISSION_DENIED :
                    callback(-1,-1,"您拒绝了定位请求。");
                    break;
                case error.POSITION_UNAVAILABLE :
                    callback(0,0,"我们暂时无法获取您的位置。");
                    break;
            }
        }, {timeout:10000});    //设置十秒超时
    }
}

function refresh_location() {
    get_location(function(lat,lon,msg) {
        if(lat == -1 && lon == -1){
            alert("请关闭后重新打开页面，并允许获取位置");
        } else if(lat == 0 && lon == 0) {
            alert("定位失败。");
        } else {
            slapi.glob_lat = lat;
            slapi.glob_lon = lon;
            $("#refreshpos").attr("hidden",true);
        }

        show_couponlist();
    });
}

window.onload = function(){
    document.body.style.display = 'block';
    var pay = getUrlParam("pay");
    if(pay != null){
        var p = $.fn.cookie('param');
        if(p){
            location.search += "&"+p;
            $.fn.cookie('param', null);
        }
    }
    refresh_location();
}