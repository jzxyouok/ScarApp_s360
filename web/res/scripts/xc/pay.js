var jsParam=${jsParam};
function wxpay(){
	WeixinJSBridge.invoke(
		'getBrandWCPayRequest'
		,jsParam
		,callback
	);
}

function callback(res){ 
	//  返回 res.err_msg,取值 
	// get_brand_wcpay_request:cancel   用户取消 
    // get_brand_wcpay_request:fail  发送失败 
    // get_brand_wcpay_request:ok 发送成功 
     WeixinJSBridge.log(res.err_msg); 	     	     				 
     if(res.err_msg=='get_brand_wcpay_request:ok')
     {
     	showPayResult();
     }
	else
	{
		var msg='支付失败,请重新支付.';
		alert(msg);
		 alert(res.err_code+" err_desc="+res.err_desc+" err_msg="+res.err_msg); 	
	}
};