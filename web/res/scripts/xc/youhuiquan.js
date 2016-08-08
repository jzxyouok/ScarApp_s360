/**
 * Created by Administrator on 2016/5/20.
 */
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
function getCode() {
    var couponCode=$("#couponCode").val();
    if(""==couponCode||null==couponCode){
        Dialog.show("兑换码不能为空", 2, 1000);
        return ;
    }
    Dialog.show("正在处理请求", 3, -1);
    var url=$root+"/vc/tuihuan.html";
    $.ajax({
        type:"post",
        url: url,
        data:{"couponCode":couponCode},
        async : false,
        success:function(data){
            if(data.success=='-1'){
                Dialog.show("兑换码不正确", 2, 2000);
            }else if(data.success=='1'){
                Dialog.show("兑换码已经被使用", 2, 2000);
            }else if(data.success=='2'){
                Dialog.show("兑换码已经失效", 2, 2000);
            }else if(data.success="0"){
                Dialog.show("兑换成功", 1, 2000);
                location.reload();

            }else{
                Dialog.show("服务器出问题了", 2, 2000);
            }


        },error:function(data){
            alert("服务器duang了,你可以休息片刻再回来收藏");
        }
    });
    // e.stopPropagation();
}
//使用优惠券
function useCoupon(money,name,vipCouponId){
    $("#coupon_price",window.parent.document).html(money);
    $("#vipCouponName",window.parent.document).html(name);
    $("#vipCouponId",window.parent.document).val(vipCouponId);
    //隐藏此iframe
    $("#vipCouponIframe",window.parent.document).hide();
    $("#myform",window.parent.document).show();
    //显示主页
    window.parent.getTotalPrice();
}
