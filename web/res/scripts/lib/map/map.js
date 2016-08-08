getMyCurrentLocation();
function getMyCurrentLocation(){
	    $("#washAddr").val("地址获取中,请稍等。。。");
		// 百度地图API功能
		// 百度地图API功能
		var map = new BMap.Map("allmap");
		var point = new BMap.Point(116.816229,39.950035);
		map.centerAndZoom(point,12);
		    
		// 创建地址解析器实例
		var myGeo = new BMap.Geocoder();
		// 将地址解析结果显示在地图上,并调整地图视野
		myGeo.getPoint("燕郊开发区", function(point){
			if (point) {
				map.centerAndZoom(point, 16);
				map.addOverlay(new BMap.Marker(point));
			}
		}, "三河市");
		var lng;
		var lat;
		var geolocation = new BMap.Geolocation();
		geolocation.getCurrentPosition(function(r){
			if(this.getStatus() == BMAP_STATUS_SUCCESS){
				var mk = new BMap.Marker(r.point);
				map.addOverlay(mk);
				map.panTo(r.point);
				lng=r.point.lng;
				lat=r.point.lat;
		       var point = new BMap.Point(lng,lat);
			   map.centerAndZoom(point,12);
			   var mk = new BMap.Marker(point);
				map.addOverlay(mk);
				map.panTo(point);
				var geoc = new BMap.Geocoder();  
				
				geoc.getLocation(point, function(rs){
				var addComp = rs.addressComponents;
				//alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
				/**初始化定位赋值*/
				$("#maplocation").html(addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				$("#washAddr").val(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
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
				    $("#maplocation").html(addComp.province + "" + addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				    //$("#maplocation").html(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
				});        
			});





		}
		else {
			alert('failed'+this.getStatus());
		}        
	},{enableHighAccuracy: true})
	
}