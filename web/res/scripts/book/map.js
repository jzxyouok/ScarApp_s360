		var map = new BMap.Map("allmap");
		var point = new BMap.Point(116.331398,39.897445);
		map.centerAndZoom(point,12);
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
				var geoc = new BMap.Geocoder();    
				geoc.getLocation(point, function(rs){
				var addComp = rs.addressComponents;
				/**初始化定位赋值*/
				$("#washAddr").val(addComp.city + "" + addComp.district + "" + addComp.street + "" + addComp.streetNumber);
				
			});
			map.addEventListener("click", function(e){        
				var pt = e.point;
				geoc.getLocation(pt, function(rs){
					var addComp = rs.addressComponents;
					/**地址点击事件重新赋值*/
				    $("#maplocation").html(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street + ", " + addComp.streetNumber);
				});        
			});





		}
		else {
			alert('failed'+this.getStatus());
		}        
	},{enableHighAccuracy: true})




