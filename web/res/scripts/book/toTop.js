var scrollTimer=null;
var pageNo="${pageNo}";
alert(pageNo);
var currentUrl="/book/articlelistAjax.html";

$(window).scroll(function(){
    if($(document).scrollTop()>=$(document).height()-$(window).height()){
        if (scrollTimer) {
            clearTimeout(scrollTimer)
        }
        scrollTimer = setTimeout(function(){
            var div1tem = $('#content').height();
            pageNo=parseInt(pageNo)+1;
            alert(pageNo);
            var obj=$("#no_content").html();
            if(obj){//如果已经到底了一次，就不在请求ajax
                return;
            }
            $.ajax({
                type:"post",
                url:currentUrl,
                dataType:'json',
                data:{pageNo:pageNo},
                beforeSend:function(){
                    $('.background_div').show();
                    $('.load_div').show();
                },
                success:function(ret){
                    var list=ret.articles;
                    var html='';
                    alert(list.length);
                    for(var i=0;i<list.length;i++){
                        var article=list[i];
                       //<!--替换开始-->
                        html+='<div class="container-layout app_news">';
                        html+='<div class="line-middle">';
                        html+='<div class="x3 padding">';
                        html+='<a href="/detail/'+article.id+'.html">';
                        html+='[@imageList bid='+article.id+']';
                        html+=' [#if images?? && images?size>0]';
                        html+='[#list images as image]';
                        html+='[#if image_index==0]';
                        html+='<img src="/c/${image.filePath}" width="85px" height="85px"  class="img-border radius-small" />';
                        html+='[/#if]';
                        html+='[/#list]';
                        html+='[#else]';
                        html+='<img src="http://www.huiben.net/resize_85x85/Public/Atlas/2013/08/25/245699320275.jpg" width="85px" height="85px"  class="img-border radius-small" />';
                        html+='[/#if]';
                        html+='[/@imageList]';
                        html+='</a>';
                        html+='</div>';
                        html+='<div class="x6 padding" style="padding-left: 20px; padding-top: 15px">';
                        html+='<span>';
                        html+='<strong>';
                        html+='<a href="/detail/'+article.id+'.html">'+article.bname+'</a>';
                        html+='</strong>';
                        html+='</span>';
                        html+='<br />';
                       // html+='<span class="app_dq">${book.bremark!''}</span><br />';
                       // html+='<span class="app_bfont">主题：${book.typeThemeName!''}</span><br />';
                        //html+='<span class="app_bfont app_he">适读：${book.typeAgeName!''}</span>';

                        html+='<br />';
                        html+=' </div>';
                        html+='<div class="x3 padding" style="padding-left: 20px; padding-top: 15px">';
                        html+=' [@isExsits bid='+article.id+']';
                        html+='[#if count?? &&count==1]';
                      //  html+='<img src="../../../res/images/book/yichu.jpg" id="${book.id}" onclick="ajax('${book.id}','${user!}')"  bind_data="yes" width="80px"  class="packet margin-small-right"/>';
                        html+=' [#else]';
                      //  html+=' <img src="../../../res/images/book/frsb.jpg" id="${book.id}"  onclick="ajax('${book.id}','${user!}')"  bind_data="no" width="80px"  class="packet margin-small-right"/>';
                        html+='[/#if]';
                        html+='[/@isExsits]';

                        html+=' </div>';
                        html+=' </div>';
                        html+=' </div>';

                        //替换结束
                    }
                    if(list.length==0){
                        var ht="<div id='no_content' class='no_content'>亲,到底了!</div>";
                        var obj=$("#no_content").html();
                        if(!obj){
                            html=ht;
                        }
                    }
                    $(".after_div").before(html); //将ajxa请求的数据追加到内容里面
                    $('.ajax_loading').hide(); //请求成功,隐藏加载提示
                    setTimeout(function () {
                        $('.background_div').hide();
                        $('.load_div').hide();
                    }, 300);
                }
            })
        }, 400);

    }
})