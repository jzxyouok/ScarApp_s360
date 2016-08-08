var $root=getRootPath();
var lng;
var lat;
var map, geolocation;
var washAddr=$("#washAddr").val();
initLocation();
function getMyCurrentLocation(){
	$("#washAddr").val("定位中，请稍后......");
	 map = new AMap.Map('allmap',{
		resizeEnable: true
	});

	map.plugin('AMap.Geolocation', function() {
		geolocation = new AMap.Geolocation({
			enableHighAccuracy: true,//是否使用高精度定位，默认:true
			timeout: 10000,          //超过10秒后停止定位，默认：无穷大
			buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
			zoomToAccuracy: true,      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
			buttonPosition:'RB'
		});
		map.addControl(geolocation);
		geolocation.getCurrentPosition();
		AMap.event.addListener(geolocation, 'complete', onComplete);//返回定位信息
		AMap.event.addListener(geolocation, 'error', onError);      //返回定位出错信息
	});
}
//解析定位结果
function onComplete(data) {
	lng=data.position.getLng();
	lat=data.position.getLat();
	queryAreaLogin(lng,lat);
	$("#lng").val(lng);
	$("#lat").val(lat);
	var geocoder=null;
	AMap.plugin('AMap.Geocoder',function(){
		 geocoder = new AMap.Geocoder({
			city: "010"//城市，默认：“全国”
		});
		var marker = new AMap.Marker({
			map:map,
			bubble:true
		})
		map.on('click',function(e){
			var strs=e.lnglat;
			lng=strs.lng;
			lat=strs.lat;
			$("#lng").val(lng);
			$("#lat").val(lat);
			marker.setPosition(e.lnglat);
			geocoder.getAddress(e.lnglat,function(status,result){
				if(status=='complete'){
					$("#washAddr").val(result.regeocode.formattedAddress);
					$("#maplocation").val($("#washAddr").val());
				}
			})
		})
	});
	var lnglatXY=[lng, lat];//地图上所标点的坐标
	geocoder.getAddress(lnglatXY, function(status, result) {
		if (status === 'complete' && result.info === 'OK') {
			//获得了有效的地址信息:
			$("#washAddr").val(result.regeocode.formattedAddress);
			$("#maplocation").val($("#washAddr").val());
		}else{
			//获取地址失败
		}
	});


}
//解析定位错误信息
function onError(data) {
	document.getElementById('tip').innerHTML = '定位失败';
}
function initLocation(){

	//if(''==washAddr||null==washAddr||"定位中，请稍后......"==washAddr) {
		loadMapData();
	//}

}

/**
 * 根据当前位置的经纬度，查询当前位置是否在后台配置的有效洗车距离范围内容
 */
function queryAreaLogin(lng,lat){
	var url=$root+"/getAreaRegion.html";
	$.ajax({
		type: "POST",
		url: url,
		async:false,
		data: {lng:lng,lat:lat},
		dataType: "json",
		beforeSend:function(){
			//禁用下单按钮
			$("#order").unbind("onclick");
			$('#order').attr('disabled',"true");//添加disabled属性
		},
		complete:function(){

		},
		success: function(data){
			if(data.success){
				//可以下单
				$("#order").bind("onclick","save_order()");
				$('#order').removeAttr("disabled"); //移除disabled属性
				$('#order').removeAttr("readonly"); //移除disabled属性
				$('#order').removeClass("gray"); //移除disabled属性
				$('#order').addClass("green"); //移除disabled属性
			}else{
				if(!confirm("您的位置不在我们的服务范围内，暂时不受理您的订单，请不要下单")){
					WeixinJSBridge.call('closeWindow');
				}else{
					WeixinJSBridge.call('closeWindow');
				}
				$("#order").unbind("onclick");
				$('#order').attr('disabled',"true");//添加disabled属性
			}
		},error:function(){
			alert("服务器异常");
		}
	})
}
//点击位置时加载地图信息
function loadMapData(){
	$("#maplocation").val($("#washAddr").val());
	var map = new AMap.Map('allmap',{
		resizeEnable: true,
		zoom: 13,
		center: [116.39,39.9]
	});
	AMap.plugin('AMap.Geocoder',function(){
		var geocoder = new AMap.Geocoder({
			city: "010"//城市，默认：“全国”
		});
		var marker = new AMap.Marker({
			map:map,
			bubble:true
		})
		map.on('click',function(e){
			var strs=e.lnglat;
			lng=strs.lng;
			lat=strs.lat;
			$("#lng").val(lng);
			$("#lat").val(lat);
			marker.setPosition(e.lnglat);
			geocoder.getAddress(e.lnglat,function(status,result){
				if(status=='complete'){
					$("#washAddr").val(result.regeocode.formattedAddress);
					$("#maplocation").val($("#washAddr").val());
				}
			})
		})

	});
	autoSearch();
}
function autoSearch(){
	    AMap.plugin(['AMap.Autocomplete','AMap.PlaceSearch'],function(){
		var autoOptions = {
			city: "北京", //城市，默认全国
			input: "maplocation"//使用联想输入的input的id
		};
		autocomplete= new AMap.Autocomplete(autoOptions);
		var placeSearch = new AMap.PlaceSearch({
			city:'北京',
			map:mapObj
		})
		AMap.event.addListener(autocomplete, "select", function(e){
			//TODO 针对选中的poi实现自己的功能
			placeSearch.search(e.poi.name)
		});
	});
}
function getRootPath(){
	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath=window.document.location.href;
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName=window.document.location.pathname;
	var pos=curWwwPath.indexOf(pathName);
	//获取主机地址，如： http://localhost:8083
	var localhostPaht=curWwwPath.substring(0,pos);
	//获取带"/"的项目名，如：/uimcardprj
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
	return(localhostPaht+projectName+"/");
}
