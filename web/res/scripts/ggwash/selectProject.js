/**
 * Created by Administrator on 2016/4/24.
 */
var sum=0;
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
$(document).ready(function(e) {
    /* 多选按钮 */
    $( ".radiooper").click( function(){
        var sum_pirce=0;
        var selectCount=0;
        if($(this).hasClass("on")){
            $( this ).removeClass( "on" )
            $( this ).attr( "src","../res/css/xc/img/radio_unsel.png" );
        }else{
            $( this ).addClass( "on" )
            $( this ).attr( "src","../res/css/xc/img/radio_sel.png" );
        }
        $(".radiooper").each(function(){
            if($(this).hasClass("on")){
                selectCount++;
            }
        });
        if(0==selectCount){
            $("#youhuiquanTr").hide();
            $("#qbpayTr").hide();
            $("#wxpayTr").hide();
            $("#total").html(0);
        }else{
            getTotalPrice();
        }



    });
    //单选按钮
    $( ".radio").click( function(){
        var current_click=$(this).attr('id');
        var index=$(this).attr('ids');
        var sum_price=$("#default_"+index).html();
        var name=$(this).attr('fname');
        $("#projectName").val(name);
        $(".radio").each(function(){
            var id=$(this).attr('id');
            if(current_click==id){
                $( this ).addClass( "on" )
                $( this ).attr( "src","../res/css/xc/img/radio_sel.png" );
            }else{
                $( this ).removeClass( "on" )
                $( this ).attr( "src","../res/css/xc/img/radio_unsel.png" );
            }
        });

        $("#total").html(parseFloat(sum_price).toFixed(2));
    });
    //余额付款
    $(".yuepay").click( function(){
            var projectName=$("#projectName").val();
            var isShare=$("#isShare").val();
            if(''==projectName||null==projectName){
                alert("请选择服务项目");
                return ;
            }
            if(1==isShare){
                alert("首单优惠不可与钱包同时使用");
                return ;
            }
            var flg=$(this).hasClass('on');
            if(flg){
                $( this ).removeClass( "on" )
                $( this ).attr( "src","../res/css/xc/img/radio_unsel.png" );
            }else{
                $( this ).addClass( "on" )
                $( this ).attr( "src","../res/css/xc/img/radio_sel.png" );
                $("#qbpayTr").hide();
                $("#wxpayTr").show();
            }
        getTotalPrice();

    });
});
//计算总价
function getTotalPrice(){
    //获取服务项目总价
    var sumPrice=0;
    var names='';
    $(".radiooper").each(function(){
        if($(this).hasClass('on')){
            names+=$(this).attr('fname')+",";
            var id=$(this).attr('id');
            var value=$("#default_"+id).attr('value');
            sumPrice=parseFloat(sumPrice)+parseFloat(value);

        }
    });
    //原价
    $("#orignPrice").html(parseFloat(sumPrice).toFixed(2));
    if(sumPrice>0){//选择了服务项目后才有下面的算法
        if(''!==names&&null!=names){
            //去掉最后一个多余逗号,
            names=names.trim().substr(0,names.length-1);
        }
        $("#projectName").val(names);
        //获取优惠券抵扣金额
        var youhui=parseFloat($("#coupon_price").html()).toFixed(2);
        //计算总价减去优惠券抵扣后的价格
        sumPrice=parseFloat(sumPrice-youhui).toFixed(2);
        //获取钱包余额 前提是选择了使用余额付款
        var flg=$(".yuepay").hasClass('on');
        if(flg){
            var qianbao=parseFloat($("#yuemoney").html()).toFixed(2);//钱包余额
            var f=qianbao-sumPrice;
            if(f>0){//钱包余额充足，就没必要使用微信支付了，直接下单扣除钱包余额就可以了
                $("#qbdk").html(sumPrice);//余额抵扣
                $("#sum").html(sumPrice);
                $("#total").html(0);
                $("#qbpayTr").show();
                $("#wxpayTr").hide();
                $("#youhuiquanTr").hide();
            }else{
                $("#qbdk").html(qianbao);//余额抵扣
                sumPrice=sumPrice-qianbao;
                $("#total").html(sumPrice);
                $("#qbpayTr").hide();
                $("#youhuiquanTr").hide();
                $("#wxpayTr").show();
            }
        }else{
            //不使用钱包余额付款
            chongzhiQianBao(sumPrice);
        }
    }else{
        //重置钱包余额
        chongzhiQianBao(sumPrice);
    }


}
//不使用余额付款
function chongzhiQianBao(sumPrice){
    var s=parseFloat(sumPrice).toFixed(2);
    if(s<=0){
        $("#total").html(0);//总额
        $("#qbpayTr").hide();
        $("#wxpayTr").hide();
        $("#youhuiquanTr").show();

    }else {
        $("#total").html(sumPrice);//总额
        $("#youhuiquanTr").hide();
        $("#sum").html(sumPrice);
        $("#wxpayTr").show();
    }
    $("#qbdk").html(0);//余额抵扣

}
//选择服务项目,需要把服务项目名称和总金额传递过去
function weixinPay(){
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
    /**if(name==""||name==null){
        alert("请填写称呼");
        return false;
    }{
        if (!/^[\u4e00-\u9fa5]+$/gi.test(name.trim())) {
            alert("称呼只能输入中文");
            return false;
        }
    }**/
    if(carNo==""||carNo==null){
        alert("请填写车牌号");
        return false;
    }else if(carNo.length!=7){
        alert("车牌号填写错误");
        return false;
    }
/*
    if(carColor==""||carColor==null){
        alert("请填写车颜色");
        return false;
    }*/
    if(carType==""||carType==null){
        alert("请填写车型");
        // hideloading();
        return false;
    }
    if(washAddr==""||washAddr==null||"定位中，请稍后......"==washAddr){
        alert("请填写洗车地点");
        return false;
    }
   /**var preTime=$("#preTime").val();
    if(preTime==""||preTime==null){
        alert("请选择预约时间");
        return false;
    }**/
    var money=$("#total").html();//总金额
    var projectName=$("#projectName").val();//服务项目
    var ids=getServicesProjectIds();
    if('首单外洗6元'==projectName||'整车洗16元'==projectName){

    }else{
        if(''==ids||null==ids){
            alert("请选择服务项目");
            return;
        }
    }

    $("#totalPrice").val($("#total").html());
    //跳转到微信支付订单页面
    //下单换成ajax方式
    var qbdk=$("#qbdk").html();
    $("#qianbao").val(qbdk);
    var url=$root+"/order/wxpay.html";
    $("#myform").attr('action',url);
    var data=$('#myform').serialize();
    $("#myform").submit();

}
//钱包充足，使用钱包直接扣款，不走微信支付
function qbpay(){
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
    /**if(name==""||name==null){
        alert("请填写称呼");
        return false;
    }{
        if (!/^[\u4e00-\u9fa5]+$/gi.test(name.trim())) {
            alert("称呼只能输入中文");
            return false;
        }
    }**/
    if(carNo==""||carNo==null){
        alert("请填写车牌号");
        return false;
    }else if(carNo.length!=7){
        alert("车牌号填写错误");
        return false;
    }
/*
    if(carColor==""||carColor==null){
        alert("请填写车颜色");
        return false;
    }*/
    if(carType==""||carType==null){
        alert("请填写车型");
        // hideloading();
        return false;
    }
    if(washAddr==""||washAddr==null||"定位中，请稍后......"==washAddr){
        alert("请填写洗车地点");
        return false;
    }
    /**var preTime=$("#preTime").val();
     if(preTime==""||preTime==null){
        alert("请选择预约时间");
        return false;
    }**/
    var money=$("#total").html();//总金额
    var projectName=$("#projectName").val();//服务项目
    if('首单外洗6元'==projectName||'整车洗16元'==projectName){

    }else{
        var ids=getServicesProjectIds();
        if(''==ids||null==ids){
            alert("请选择服务项目");
            return;
        }
    }

    $("#totalPrice").val($("#total").html());
    //跳转到微信支付订单页面
    //下单换成ajax方式
    var qbdk=$("#qbdk").html();
    $("#qianbao").val(qbdk);
    var url=$root+"/order/qbpay.html";
    $("#myform").attr('action',url);
   // var data=$('#myform').serialize();
    $("#myform").submit();
}
//优惠券充足，直接用优惠券抵扣
function yhqPay(){
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
    /**if(name==""||name==null){
        alert("请填写称呼");
        return false;
    }{
        if (!/^[\u4e00-\u9fa5]+$/gi.test(name.trim())) {
            alert("称呼只能输入中文");
            return false;
        }
    }**/
    if(carNo==""||carNo==null){
        alert("请填写车牌号");
        return false;
    }else if(carNo.length!=7){
        alert("车牌号填写错误");
        return false;
    }
    /*
     if(carColor==""||carColor==null){
     alert("请填写车颜色");
     return false;
     }*/
    if(carType==""||carType==null){
        alert("请填写车型");
        // hideloading();
        return false;
    }
    if(washAddr==""||washAddr==null||"定位中，请稍后......"==washAddr){
        alert("请填写洗车地点");
        return false;
    }
    /*var preTime=$("#preTime").val();
    if(preTime==""||preTime==null){
        alert("请选择预约时间");
        return false;
    }*/
    var money=$("#total").html();//总金额
    var projectName=$("#projectName").val();//服务项目
    if('首单外洗6元'==projectName||'整车洗16元'==projectName){

    }else{
        var ids=getServicesProjectIds();
        if(''==ids||null==ids){
            alert("请选择服务项目");
            return;
        }
    }
    $("#totalPrice").val($("#total").html());
    var url=$root+"order/yhqPay.html";
    $("#myform").attr('action',url);
    $("#myform").submit();
}
/*获取所有选中的服务*/
function getServicesProjectIds(){
    var ids="";
    $("img").each(function(){
        var src=$(this).attr("src");
        if(src.indexOf("radio_sel.png")>0){
            ids+=$(this).attr("ids")+",";
        }
    })
    ids=ids.substring(0,ids.length-1);
    return ids;
}
