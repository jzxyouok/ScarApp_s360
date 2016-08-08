var $root=getRootPath();
function search(){
	var bname=$("#bname").val();
	var typeAge=$("#typeAge").val();
	var typeTheme=$("#typeTheme").val();
	var url="?bname="+bname;
	if(typeAge!=0){
		url+="&typeAge="+typeAge;
	}

    url+="&typeTheme="+typeTheme;

	window.location.href=url;
}
function searchName(){
	var bname=$("#bname").val();
	var url="?bname="+bname+"&typeTheme=0";
	window.location.href=url;
}
/**
 * 账号登陆
 * @return
 */
function login(){
	var mobile=$("#mobile").val();
	var userPassword=$("#userPassword").val();
	if(''==mobile||null==mobile){
		 Dialog.show("账号不能为空", 2, 2000);
		 return;
	}
	if(''==userPassword ||null==userPassword){
		 Dialog.show("密码不能为空", 2, 2000);
		 return;
	}
	var url="/user/login.html";
    var data=$("#form1").serialize();
	 Dialog.show("正在登陆,请稍后。。。", 3, -1);
	  $.ajax({
           type:"post",
           url: url,
           data:data,
           dataType:'json',
           success:function(data){
               if(data.success){
                   Dialog.show("登陆成功", 1, 2000);
                   setTimeout(function() {
                	    var page=data.page;
              			window.location.href=page;
                   },2000);
               }else{
                   Dialog.show("账号或密码错误", 2, 2000);
               }
           },error:function(data){
               	alert("服务器duang了,你可以休息片刻再回来");
           }
       });

}
function delCollection(id){
	var $this=$("#"+id);
	var flag=$this.attr('bind_data');
	var url="";
	url="/delBook/"+id+".html";
	 Dialog.show("正在处理请求", 3, -1);
	  $.ajax({
            type:"post",
            url: url,
            async : false,
            success:function(data){
                if(data.success){
                	var sum=parseInt($("#total").html());
                	$("#total").html(sum-1);
                	$("#total2").html(sum-1);
              	 	Dialog.show("移除书包成功", 1, 2000);
                	 $this.remove();

                }else{
                    alert("您的人品也太不好了吧,收藏 失败了");
                }
            },error:function(data){
                	alert("服务器duang了,你可以休息片刻再回来收藏");
            }
        });
}
/*添加地址*/
function addAddress(){
	var userName=$("#name").val();
	var mobile=$("#mobile").val();
	var address=$("#address").val();
	if(userName==''){
		Dialog.show("姓名不能为空", 2, 2000);
		return false;
	}
	if(mobile==''||null==mobile){
		Dialog.show("手机号码不能为空", 2, 2000);
		return;
	}else{
		var myreg = /^1[3458]\d{9}$/;
		if (!myreg.exec(mobile)){
			errMsg='请输入正确的手机号';
			Dialog.show(errMsg, 2, 2000);
			return;
		}
	}
	if(address==''||null==address){
		Dialog.show("地址不能为空", 2, 2000);
		return ;
	}
	 Dialog.show("正在添加地址...", 3, -1);
	 var url="/addAddress.html";
	 var data=$("#form1").serialize();
	 $.ajax({
         type:"post",
         url: url,
         data:data,
         async : false,
         success:function(data){
             if(data.success=="nologin"){
                 Dialog.show("请先登录", 2, 2000);
                 window.location.href="/book/login.html";
             }else if(data.success){
            	 Dialog.show("添加成功", 1, 2000);
            	 setTimeout(function() {
			            window.location.href="/select/loveaddress.html";
			        },2000);
             }else{
            	 Dialog.show("添加失败", 2, 2000);
             }
         },error:function(data){
             	alert("服务器duang了,你可以休息片刻再回来收藏");
         }
     });
}
/*提交订单*/
function submitOrder(){
	var total2=$("#total2").html();
	if(total2==0){
		 Dialog.show("书包不能为空！", 0, 2000);
         return false;
	}
	var address=$("#addressId").val();
	if(typeof(address) == "undefined"){
		Dialog.show("请先添加地址", 2, 2000);
        return false;
	}else{
		/*获取book...id*/
		var bids="";
		$(".bnames").each(function(){
			bids+=$(this).attr('value')+",";
		})
		bids=bids.substring(0, bids.length-1);
		var url="/order/create";
		 Dialog.show("正在提交订单...", 3, -1);
		 $.ajax({
	            type:"post",
	            url: url,
	            data:{bids:bids,addressId:address},
	            async : false,
	            success:function(data){
	                if(data.success){
	                    Dialog.show("订单提交成功", 2, 2000);
	                    window.location.href="/book/order.html";

	                }else{
	                    alert("您的人品也太不好了吧,收藏 失败了");
	                }
	            },error:function(data){
	                	alert("服务器duang了,你可以休息片刻再回来收藏");
	            }
	        });
	}

}
function preOrder(){
	var total2=$("#total2").html();
	if(total2==0){
		 Dialog.show("书包不能为空！", 0, 2000);
         return false;
	}
	var ids="";
	$(".app_news").each(function(){
		var id=$(this).attr('id')+",";
		ids+=id;
	})
	ids=ids.substring(0,ids.length-1);

	Dialog.show("正在提交订单...", 3, -1);
	var url="/order/precreate?cid="+ids;
	window.location.href=url;

}
function ajax(id,user){
	//判断用户是否登陆
	 Dialog.show("正在处理请求", 3, -1);
	 var login_url="/user/getUser.html";
	  $.ajax({
           type:"post",
           url: login_url,
           async : false,
           success:function(data){
			  if(null==data.user){
			        Dialog.show("您还未登陆", 1, 2000);
			        setTimeout(function() {
			            window.location.href="/book/login.html";
			        },3000);
			        return;
				}else{
					ajaxSubmit(id);
				}

		  },error:function(){

		  }
		  });
	}
function ajaxSubmit(id){
    var $this=$("#"+id);
	var flag=$this.attr('bind_data');
	var url="";
	if(flag=="no")
	{
		url="/addbook/"+id+".html";
	}else{
		url="/delBook/"+id+".html";
	}
	 Dialog.show("正在处理请求", 3, -1);
	  $.ajax({
            type:"post",
            url: url,
            async : false,
            success:function(data){
				  if(data.success=="max"){
		          	Dialog.show("亲，一次最多只能借8本呦", 2, 3000);
		          }else if(data.success=="min"){
					  Dialog.show("亲，此书借光啦，请选择其他绘本", 2, 3000);
				  }
				  else{
		        	  if(data.success){
		                    if(flag=="yes"){
		                      	Dialog.show("移除书包成功", 1, 2000);
		                         $this.attr('src',"../../../res/images/book/frsb.jpg");
		                         $this.attr("bind_data","no");
		                    }else{
		                   		 Dialog.show("加入书包成功", 1, 2000);
		                         $this.attr('src',"../../../res/images/book/yichu.jpg");
		                         $this.attr("bind_data","yes");
		                    }
		                }else{
		                	 Dialog.show("您的人品也太不好了吧,加入失败了", 2, 2000);
		                }
		          }

            },error:function(data){
                	alert("服务器duang了,你可以休息片刻再回来收藏");
            }
        });
}
var Dialog = new function () {
    this.show = function (b, l, f) {
        this.hide();
        var g = l || 0;
        if (g > 3) {
            g = 0;
        }
        var m = f || 5000;
        var e = new Array();
        e[0] = $root+"res/images/book/info.png";
        e[1] = $root+"res/images/book/success.png";
        e[2] = $root+"res/images/book/error.png";
        e[3] = $root+"res/images/book/ajax-loader.gif";
        var a = document.createElement("div");
        a.id = "div_tip_1";
        a.className = "dialog-div-box";
        var d =
            '<div class="dialog-div"><div class="u-guodu-box"><div><table width="100%" cellpadding="0" cellspacing="0" border="0" ><tr><td  align="center"><img src="' +
            e[g] + '"></td></tr><tr><td align="center" style="font-size:14px;font-weight: blod;line-height:30px">' +
            b + "</td></tr></table></div></div></div>";
        a.innerHTML = d;
        document.body.appendChild(a);
        var c = document.documentElement.scrollTop;
        var k = $(window).width();
        var i = $(window).height();
        $(".dialog-div").css("display", "block");
        $(".dialog-div").css("top", c + "px");
        var h = $(".dialog-div").width();
        var j = $(".dialog-div").height();
        $(".dialog-div").css("left", (k / 2 - h / 2) + "px");
        $(".dialog-div").css("top", (i / 2 - j / 2) + "px");
        if (m > 0) {
            setTimeout(function () {
                a.parentNode.removeChild(a)
            }, m);
        }
        return a;
    };
    this.hide = function () {
        var a = document.getElementById("div_tip_1");
        if (a !== null || typeof (boj) !== "undefined") {
            a.parentNode.removeChild(a);
        }
    }
};
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
