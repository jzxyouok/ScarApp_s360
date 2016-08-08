	var signature=$("#signature").val();
   $( document ).ready(function(e) {
	    $( "#select_history" ).live("touchstart",function(){
	        $( this ).css( "background-color" , "#e7f1ff" );
	        $( this ).siblings( "span" ).removeAttr( "style" );
	        //getAddrList();
	    });
    });
    function getAddrList () {
    var mobile=$("#mobile").val();
    var rootUrl=$("#rootUrl").val();
    $.ajax({
				type:"post",
				url: rootUrl+"/wxpay",
				async : false,
				success:function(data){
            var response=data.historyList;
            var htmls="";
            $.each(response, function(key, value) {
                var sp="";
                if(key==0)sp="<b id='firstAddr'>[位置]</b>";
                htmls+="<div class='autoaddr' washAddr='"+value.washAddr+"' mobile='"+mobile+"'><h1 class='clearfix'  washAddr='"+value.washAddr+"' mobile='"+
                    mobile+"'  washLng='"+
                    value.wash_lng+"' washLat='"+
                    value.wash_lat+"'><font class='address i_b v_t'>"+sp+value.washAddr+"</font></h1></div>";
            });
            $(".dropdownmodal").html(htmls);
            var length=response.length;
            if(length>0){
            	$(".dropdownmodal").show();
            }

        },error:function(){

        }
    })


}

$(".autoaddr").live('click',function(){
	var washAddr=$(this).attr('washAddr');
	//var mobile=$(this).attr('mobile');
	//alert(mobile);
	$("#washAddrShow").val(washAddr);
	//$("#washInfoShow").val(mobile);
	$(".dropdownmodal").hide();
	$(".autoaddr").attr("style","display:none");
})
    function sub_form () {
        var order_type=$('#orderType').val();
        if(order_type=='BOOK'){
            if($('#bookTimeArea').val()==null||$('#bookTimeArea').val()==''){
                alert("请选择预约时间");
                hideloading();
                return false;
            }
        }

        var carSeats=$("#carSeats").val();
        var carType=$("#carType").val();
        var carColor=$("#carColor").val();
        var carNo=$('#carNo').val();
        var washAddr=$('#washAddrShow').val();
        var mobile=$('#washInfoShow').val();
        $("#washAddr").val(washAddr);
        $('#mobile').val(mobile);
        if(carNo==""||carNo==null||(carNo.indexOf('鄂A'>0)&&carNo.length==2)){
            alert(signature+"\n请填写车牌号");
            hideloading();
            return false;
        }else if(carNo.length!=7){
        	alert(signature+"车牌号填写错误");
            hideloading();
            return false;
        }

        if(carColor==""||carColor==null){
            alert(signature+"请填写车颜色");
            hideloading();
            return false;
        }
        if(carType==""||carType==null){
            alert(signature+"请填写车型");
            hideloading();
            return false;
        }
        if(washAddr==""||washAddr==null){
            alert(signature+"请填写洗车地点");
            hideloading();
            return false;
        }
        if(mobile==""||mobile==null){
            alert(signature+"请填写手机号码");
            hideloading();
            return false;
        }
        var preTime=$("#preTime").val();
         if(preTime==""||preTime==null){
            alert(signature+"请填些预约时间");
            hideloading();
            return false;
        }
        //$("#myform").submit();
        $("#myform").hide();
        $("#serviceProjectframe").show();

}

