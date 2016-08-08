var lng;
var lat;
var washAddr=$("#washAddr").val();
initLocation();
function getMyCurrentLocation(){
	$("#washAddr").val("定位中，请稍后......");
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
	if(this.getStatus() == BMAP_STATUS_SUCCESS){
		lng=r.point.lng;
		lat=r.point.lat;
		$("#lng").val(lng);
		$("#lat").val(lat);
		queryAreaLogin(lng, lat);
		//getLocation(lng, lat);
		var point = new BMap.Point(lng,lat);
		var map = new BMap.Map("allmap");

		map.centerAndZoom(point,12);
		var mk = new BMap.Marker(point);
		map.addOverlay(mk);
		map.panTo(point);
		var geoc = new BMap.Geocoder();

		geoc.getLocation(point, function(rs) {
			var addComp = rs.addressComponents;
			//alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
			/**初始化定位赋值*/
			$("#maplocation").val(addComp.district + "" + addComp.street + "" + addComp.streetNumber);
			$("#washAddr").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
			//获取地址    end
		});
		map.addEventListener("click", function(e){
			var pt = e.point;
			var lng=e.point.lng;
			var lat=e.point.lat;
			//var map = new BMap.Map("allmap");
			var point = new BMap.Point(lng,lat);
			map.centerAndZoom(point,11);
			geoc.getLocation(pt, function(rs){
				var addComp = rs.addressComponents;
				/**地址点击事件重新赋值*/
				alert("dianj");
				alert(addComp.province);
				$("#maplocation").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				//$("#maplocation").html(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
			});
		});
	}
	else {
		alert('failed'+this.getStatus());
	}
},{enableHighAccuracy: true})
}
function initLocation(){
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			lng=r.point.lng;
			lat=r.point.lat;
			$("#lng").val(lng);
			$("#lat").val(lat);
			if(''==washAddr||null==washAddr||"定位中，请稍后......"==washAddr) {
				$("#washAddr").val("定位中，请稍后......");
				queryAreaLogin(lng, lat);//getLocation(lng, lat);
				//获取地址开始    start
				var point = new BMap.Point(lng,lat);
				var map = new BMap.Map("allmap");

			map.centerAndZoom(point,12);
				var mk = new BMap.Marker(point);
				map.addOverlay(mk);
				map.panTo(point);
				var geoc = new BMap.Geocoder();

				geoc.getLocation(point, function(rs) {
					var addComp = rs.addressComponents;
					//alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
					/**初始化定位赋值*/
					$("#maplocation").val(addComp.district + "" + addComp.street + "" + addComp.streetNumber);
					$("#washAddr").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
					//获取地址    end
				})
			}
		}
		else {
			alert('failed'+this.getStatus());
		}
	},{enableHighAccuracy: true})
}
//根据坐标获取详细位置信息
function getLocation(lo, la) {
	//clearSmCount();
	$.ajax({
		url: 'http://api.map.baidu.com/geocoder/v2/?ak=SM2bEb855uP3Eh2AUtxgHuxC&coordtype=wgs84ll&callback=getLocationSuccess&location=' + la + "," + lo + "&output=json&pois=1"
		, type: 'get'
		, dataType: 'jsonp'
		, jsonpCallback: 'getLocationSuccess'
		, success: function (data) {
			try {
				if (data && data.status == 0) {
					var result = data.result;
					var address = result.formatted_address;
					$("#washAddr").val(result.formatted_address);
				}

			} catch (err) {
				alert(err);
			}
		}
	})
}
/**
 * 根据当前位置的经纬度，查询当前位置是否在后台配置的有效洗车距离范围内容
 */
function queryAreaLogin(lng,lat){
	var url="${base}/getAreaRegion.html";
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
				if(!confirm("您的位置不在我们的服务范围内，您的订单可能会被延迟处理，是否继续下单？")){
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
	var map = new BMap.Map("allmap");
	var point = new BMap.Point(121.531453,38.963268);
	var mk;//当前描点
	var preMarker;//上一个描点
	map.centerAndZoom(point,16);
	//$("#washAddr").val("定位中，请稍后......");
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
		if(this.getStatus() == BMAP_STATUS_SUCCESS){
			lng=r.point.lng;
			lat=r.point.lat;
			$("#lng").val(lng);
			$("#lat").val(lat);
			var point = new BMap.Point(lng,lat);
			var map = new BMap.Map("allmap");
			map.centerAndZoom(point,16);
			mk = new BMap.Marker(point);
			map.addOverlay(mk);
			map.panTo(point);
			var geoc = new BMap.Geocoder();
			/*geoc.getLocation(point, function(rs) {
				var addComp = rs.addressComponents;
				//alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
				*//**初始化定位赋值*//*
				//$("#maplocation").html(addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				//$("#washAddr").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				//获取地址    end
			});*/
			map.addEventListener("click", function(e){
				var pt = e.point;
				var lng=e.point.lng;
				var lat=e.point.lat;
				$("#lat").val(lat);
				$("#lng").val(lng);
				point = new BMap.Point(lng,lat);
				map.centerAndZoom(point,16);
				map.removeOverlay(mk);
				mk = new BMap.Marker(point);
				map.removeOverlay(preMarker);

				map.addOverlay(mk);
				preMarker=mk;
				map.addOverlay(mk);
				map.panTo(point);
				geoc.getLocation(pt, function(rs){
					var addComp = rs.addressComponents;
					/**地址点击事件重新赋值*/
					$("#maplocation").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
					//$("#maplocation").html(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
				});
			});
		}
		else {
			alert('failed'+this.getStatus());
		}
	},{enableHighAccuracy: true})
}
