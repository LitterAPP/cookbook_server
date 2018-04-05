/**
 * Created by fish on 2018/4/3.
 */
var uploadImgURL='/upload/uploadImageOfBase64'
var loginURL='/shopmng/loginMng'
var checkSessionURL='/shopmng/checkSession'
var quitLoginURL='/shopmng/quitLogin'
var getShopIndexConfigURL='/shop/getShopIndexConfig'
var saveShopConfigURL='/shopmng/saveShopIndex'
var listCategroyURL='/shopmng/categoryALL'
var saveProductURL='/shopmng/saveProductInfo'
var getOneProductURL='/shopmng/getOneProduct'
var listProduct='/shop/listProduct'
var operatedProduct='/shopmng/operatedProduct'

var urlTools = {
    //获取RUL参数值
    getUrlParam: function(name) {               /*?videoId=identification  */
        var params = decodeURI(window.location.search);        /* 截取？号后面的部分    index.html?act=doctor,截取后的字符串就是?act=doctor  */
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = params.substr(1).match(reg);
        if (r!=null) return unescape(r[2]); return null;
    }
}
var common = {
    listCategroy: function (handler,func) {
        handler.mask=true
        handler.maskText='请稍后,商品分类加载中...'
        $.ajax(
            {
                url:listCategroyURL,
                type : "POST",
                dataType:"json",
                data : {},
                success:function(result){
                    if(result && result.code==1){
                        handler.mask = false
                        handler.pCategory = result.data
                        console.log(handler.pCategory, handler.mask)
                    }else{
                        handler.maskText='商品分类加载中失败:'+result.msg
                        setTimeout(function(){
                            handler.mask = false
                        },3000)
                    }
                    //需要回调做特殊处理的情况
                    if(func && typeof(func)=='function' ){
                        func(result)
                    }
                }
            });
    }
    ,
    checkLogin:function(func){
        $.ajax(
            {
                url:checkSessionURL,
                type : "POST",
                dataType:"json",
                data : {},
                success:function(result){
                    func(result)
                }
            });
    }

}