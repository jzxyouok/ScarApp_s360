var $root=getRootPath();
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

$( document ).ready(function(e) {
    /**预约时间列表*/
    $(".nopre").click(function(){
        $(this).addClass("avtion").siblings().removeClass("avtion");
        var value=$(this).find('a').attr("data");
        $("#preTime").val(value);
        $("#book_time_stock").html(value);
    })
    /**预约时间确定*/
    $("#selectTimeBtn").click(function(){
        $("#imgBox").hide();
        $("#easyDialogBox").hide();
        $("#overlay").hide();
        //$("#book_time_stock").html($("#select_date").val());
    })
    /**选择优惠券**/
    $("#selectVipCouponClickBtn").click(function(){
    	//信息填写完成之后，才可以选择优惠券
    	            var carSeats=$("#carSeats").is(":checked");
			        var carType=$("#carType").val();
			        var carColor=$("#carColor").val();
			        var carNo=$('#carNo').val();
			        var washAddr=$('#washAddr').val();
			        var mobile=$('#mobile').val();
			        var name=$("#name").val();
			         if(mobile==""||mobile==null){
			            alert("请填写手机号码");
			            return false;
			        }
			          /** if(name==""||name==null){
			            alert("请填写称呼");
			            return false;
			        }**/
			        if(carNo==""||carNo==null){
			            alert("请填写车牌号");
			            return false;
			        }else if(carNo.length!=7){
			        	alert("车牌号填写错误");
			            return false;
			        }

			        /*if(carColor==""||carColor==null){
			            alert("请填写车颜色");
			            return false;
			        }*/
			        if(carType==""||carType==null){
			            alert("请填写车型");
			            return false;
			        }
			        if(washAddr==""||washAddr==null){
			            alert("请填写洗车地点");
			            return false;
			        }
                    var projectName=$("#projectName").val();
                    if(projectName==''||projectName==null){
                        alert("请选择服务项目");
                        return false;
                    }
        /**var preTime=$("#preTime").val();
         if(preTime==""||preTime==null){
        alert("请选择预约时间");
        return false;
    }**/
               // var url="/order/mycoupon.html";
               // url=timestamp(url);
               // url=url.replace(" ","%20").replace(":","%3A");
               // $("#myform").attr('method',"get");
               // $("#myform").attr('action',encodeURI(url));
               // $("#myform").submit();
                //隐藏本页面,显示优惠券iframe页面
                $("#myform").hide();
                $("#vipCouponIframe").show();
    })
    /**选择洗车券**/
    $("#selectCouponClickBtn").click(function(){
        //信息填写完成之后，才可以选择优惠券
        var carSeats=$("#carSeats").is(":checked");
        var carType=$("#carType").val();
        var carColor=$("#carColor").val();
        var carNo=$('#carNo').val();
        var washAddr=$('#washAddr').val();
        var mobile=$('#mobile').val();
        var name=$("#name").val();
        if(mobile==""||mobile==null){
            alert("请填写手机号码");
            // hideloading();
            return false;
        }
        /**if(name==""||name==null){
            alert("请填写称呼");
            // hideloading();
            return false;
        }**/
        if(carNo==""||carNo==null){
            alert("请填写车牌号");
            // hideloading();
            return false;
        }else if(carNo.length!=7){
            alert("车牌号填写错误");
            //hideloading();
            return false;
        }

        if(carColor==""||carColor==null){
            alert("请填写车颜色");
            // hideloading();
            return false;
        }
        if(carType==""||carType==null){
            alert("请填写车型");
            // hideloading();
            return false;
        }
        if(washAddr==""||washAddr==null){
            alert("请填写洗车地点");
            // hideloading();
            return false;
        }
       /* var preTime=$("#preTime").val();
        if(preTime==""||preTime==null){
            alert("请选择预约时间");
            return false;
        }*/
        var url=$root+"mycoupon.html";
        // url=timestamp(url);
        //url=url.replace(" ","%20").replace(":","%3A");
        $("#myform").attr('method',"get");
       // $("#myform").attr('action',encodeURI(url));
        $("#myform").attr('action',url);
        $("#myform").submit();
    })

    /* 车颜色选择 */
    $( "#new_color_sel" ).change(function(){
        var colorvalue = $( this ).find( "option:selected" ).attr('data');
        var textwords = $( this ).find( "option:selected" ).text();
        $( this ).siblings( ".colorlump" ).css( "background-color" , colorvalue );
        $( this ).siblings( ".textwords" ).html( textwords );
        $('#carColor').val(textwords);
        $('#carColorId').val($(this).val());
    });
    //解决浏览器缓存
    function timestamp(url){
        var getTimestamp=new Date().getTime();
        if(url.indexOf('?')>-1){
            url=url+'&timestamp='+getTimestamp
        }else{
            url=url+'?timestamp='+getTimestamp
        }
        return url;
    }
    /* 车品牌选择 */
    $( "#new_type_sel" ).change(function(){
        var textwords = $( this ).find( "option:selected" ).text();
        $( this ).siblings( ".textwords" ).html( textwords );
        $('#carType').val(textwords);
        $('#carTypeId').val($(this).val());
    });

    /* 车型选择 */
    $( "#new_seats_sel" ).change(function(){
        var textwords = $( this ).find( "option:selected" ).text();
        $( this ).siblings( ".textwords" ).html( textwords );
        $('#carSeats').val(textwords);
        $('#carSeatsId').val($(this).val());
    });

    /* 单选按钮 */
    $( ".theradio" ).click( function(){
        if( $( this ).hasClass( "on" ) ){
            $( this ).removeClass( "on" );
            $( this ).attr( "src","../res/css/xc/img/radio_unsel.png" );
            $( this ).closest( ".radioGroup" ).siblings().find( ".theradio" ).removeClass( "on" );
            $('#carSeatsId').val('');
            $('#carSeats').val('');
            $('#carColor').val('');
            $('#carColorId').val('');
            $('#carType').val('');
            $('#carTypeId').val('');
            $('#userCarId').val('');
            $('#carNo').val('');
        } else {
            $( ".inputcarinfo" ).hide();
            $( this ).addClass( "on" );
            $( this ).attr( "src","../res/css/xc/img/radio_sel.png" );
            $( this ).closest( ".radioGroup" ).siblings().find( ".theradio" ).removeClass( "on" );
            $( this ).closest( ".radioGroup" ).siblings().find( ".theradio" ).attr( "src","../res/css/xc/img/radio_unsel.png" );
            $('#carSeatsId').val($(this).attr('carSeatsId'));
            $('#carSeats').val($(this).attr('carSeats'));
            $('#carColor').val($(this).attr('carColor'));
            $('#carColorId').val($(this).attr('carColorId'));
            $('#carType').val($(this).attr('carType'));
            $('#carTypeId').val($(this).attr('carTypeId'));
            $('#userCarId').val($(this).attr('userCarId'));
            $('#carNo').val($(this).attr('carNo'));
        }


    });

    // 选择某一地址
    $( ".dropdownmodal h1" ).on( "touchstart" , function(){
        event.preventDefault();
        if( !$( this ).hasClass( "on" ) ){
            $( this ).addClass( "on" )
            $( this ).append( "<img class='selected i_b v_m' src='../res/css/xc/img/selected.png'>" );
            $( this ).closest( "h1" ).siblings( "h1" ).find( "img" ).remove();
            $( this ).closest( "h1" ).siblings( "h1" ).removeClass( "on" );
            var washAddr=$(this).attr('washAddr');
        	var mobile=$(this).attr('mobile');
        	$("#washAddrShow").val(washAddr);
        	$("#washInfoShow").val(mobile);
            $(".dropdownmodal").hide();
            $(".autoaddr").attr("style","display:none");
            $(".dropdownmodal").attr("style","display:none");
            $("#firstAddr").hide();

            //select_addr($(this).attr('washAddr'));

        }
    });
    /* 新增车辆 */
    $( ".addcar" ).click( function(){
        $( ".inputcarinfo" ).show();
        $( this ).closest( "section" ).siblings().find( ".theradio" ).removeClass( "on" ).attr( "src","../res/css/xc/img/radio_unsel.png" );
        $('#carSeatsId').val('');
        $('#carSeats').val('');
        $('#carColor').val('');
        $('#carColorId').val('');
        $('#carType').val('');
        $('#carTypeId').val('');
        $('#userCarId').val('');
        $('#carNo').val('冀N');
    });

    /*选择库存*/
    $("#book_time_div_new").click(function(){
         easyDialog.open({
                            container : 'imgBox',
                            fixed : false
                        });
    });

    $("#cancel_sub").click(function(){
        $(".time1").css('color','#000000');
        easyDialog.close();
    });

   // getAddrList ();
    if(document.getElementById("book_time_div")!=null){
        getBookTime ();
    }


});





function getBookTime () {
    $.get(INTERFACE_URL+"Order.order.getBookTime", function(data) {
        var response=data.data;
        var htmls='<option value="" >请选择</option>';
        $.each(response, function(key, value) {
            htmls+='<option value="'+value+'" >'+value+'</option>';
        });
        $('#book_time_sel').show();
        $('#book_time_sel').html(htmls);
    })
}


//生成时间选择框
function createtimeSelect( data )
{
    var html = "";
    $("#book_time_area1").empty();
    $(".time1").css('color','#000000');
    for(var i=0;i<data.length;i++)
    {
        if(data[i].is_use=='0')
        {
            html+="<li class='avtion'>"+data[i].time_str+"<span class='is_use'>【已排满】</span></li>";
        }
        else
        {
            var str = "'"+data[i].time_str+"'";
            html+='<li><a href="javascript:void(0);" class="time1" data='+str+'>'+data[i].time_str+'</a></li>';
        }
    }
    $("#book_time_area1").append(html);

    /*选择时间段*/
    $("#book_time_area1 li a").click(function(){
        var washLat = $("#washLat").val();
        var washLng = $("#washLng").val();
        var time_str = $(this).attr('data');
        $(this).addClass("red");
        var liobj=$("#book_time_area1 li a");
        liobj.each(function(){
            $(this).click(function(){
                liobj.removeClass("red")
                $(this).addClass("red");
            });
        });

        $.post(select_time_url,{time_str:time_str,washLat:washLat,washLng:washLng},function(data){
            if(data.status=="ok")
            {
                $("#select_date").val(data.book_time);
                return true;
            }
            else
            {
                alert('该时间段已排满，请重新选择！');
                $("#book_time_area1").html('');
                createtimeSelect( data.data );
                return false;
            }

        },'json');
    });
}


/* 确定选择 */
function select_time1()
{
    var time_str = $("#select_date").val();
    $.post(select_time_url,{time_str:time_str},function(data){

        if(data.status=="ok")
        {
            $("#book_time_stock").html(data.book_time);
            $("#bookTimeArea").val(data.book_time);
            easyDialog.close();
            return true;
        }
        else
        {
            $("#book_time_area1").html('');
            createtimeSelect( data.data );
            return false;
        }

    },'json');

}
function ajax(url,data){
		$.ajax({
					url:url,
					data:data,
					type:"post",
					dataType:'json',
					async:false,
					beforeSend:function(XMLHttpRequest){
					},
					success:function(msg){
						if(msg.success){
							$("#couponId").val('');
							$("#couponName").html('');
							var url=msg.url;
							window.location.href=url;
						}else{
							alert("下单异常");
						}

					},error:function(){

						return;
					}
				});
	}

