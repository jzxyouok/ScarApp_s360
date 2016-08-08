/**
 * Created by Administrator on 2015/7/30.
 */
/**解决自定义菜单 Oauth获取openId，保存到session中，用户登陆action获取不到保存的openid session**/
function autoOpenId(openId) {
    if(''!=openId&&null!=openId){
        $.ajax({
            url : fts.baseURI + '/auto/save/'+openId+'.html',
            type : 'POST',
            async : false,
            dataType : 'json',
            success : function(responseText) {
            }
        });
    }
}

