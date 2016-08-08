/* Loading显示 */
function showloading(){
    $( ".loading" ).show();
    setInterval( turnpic , 100 );
}
/* Loading隐藏 */
function hideloading(){
    $( ".loading" ).hide();
}
var arr = new Array();
arr[ 1 ] = "turnpic_01";
arr[ 2 ] = "turnpic_02";
arr[ 3 ] = "turnpic_03";
arr[ 4 ] = "turnpic_04";
arr[ 5 ] = "turnpic_05";
arr[ 6 ] = "turnpic_06";
arr[ 7 ] = "turnpic_07";
arr[ 8 ] = "turnpic_08";
arr[ 9 ] = "turnpic_09";
arr[ 10 ] = "turnpic_10";
arr[ 11 ] = "turnpic_11";
arr[ 12 ] = "turnpic_12";
var num = 0;
function turnpic(){
    if( num == arr.length - 1 )
        num = 1;
    else
        num += 1;
    $( "#turnPic" ).removeClass().addClass( arr[ num ] );
}

/* 倒计时 */
function settime( time ) {
    var btn = $(".sentcode");
    btn.attr("disabled", true);  //按钮禁止点击
    btn.attr( "style" , "background-color:#BDBDBD" );
    btn.val(time <= 0 ? "发送验证码" : ("" + (time) + "秒后可发送"));
    var hander = setInterval(function() {
        if (time <= 0) {
            clearInterval(hander); //清除倒计时
            btn.val("发送验证码");
            btn.attr( "style" , "background-color:#2d75db" )
            btn.attr("disabled", false);
            return false;
        }else {
            btn.val("" + (time--) + "秒后可发送");
            btn.attr( "style" , "background-color:#BDBDBD" );
            btn.disabled;
        }
    }, 1000);
}

function cancel_order (order_id,is_use_stock) {
    $(".tooltipwords").html("您是否取消该订单？");
    var str='<span class="btn_rectangle btn_white f_l" id="cancel" onclick="$(\'.tooltip\').hide()">取消</span><span class="btn_rectangle btn_orange f_r" id="confirm" onclick="cancel_order_confirm('+order_id+','+is_use_stock+')">确定</span>';
    $(".tooltipoper").html(str);
    $( ".tooltip" ).toggle();
}
function cancel_order_confirm (order_id,is_use_stock) {
    showloading();
    $.get(INTERFACE_URL+"Order.status.cancel&orderId="+order_id+"&is_stock="+is_use_stock, function(data) {
        if(data.data.result==1){
            alert("取消成功");
            window.location.reload();
        }else{
            alert("取消失败");
        }
        hideloading();
    })
}

function show_cal (cal) {
    $(".tooltipwords").html("师傅手机号："+cal);
    var str='<span class="btn_rectangle btn_orange" id="confirm" onclick="$(\'.tooltip\').hide()">确定</span>';
    $(".tooltipoper").html(str);
    $( ".tooltip" ).toggle();

}