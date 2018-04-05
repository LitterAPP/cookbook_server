/**
 * Created by fish on 2018/3/29.
 */
var nav = new Vue({
    el: '#nav',
    data: {
       list:[
           {title:'商铺首页管理',url:'../html/func/shopMng.html',activite:false},
           {title:'商品管理',url:'../html/func/productMng.html',activite:false},
           {title:'订单管理',url:'../html/func/orderMng.html',activite:false},
           {title:'发货管理',url:'../html/func/deliverMng.html',activite:false},
       ]
    },
    methods:{
        navigation:function(event){
            var idx = event.target.dataset.idx
            for(var i in this.list){
                this.list[i].activite = false
            }
            var currentItem = this.list[idx]
            currentItem.activite=true
            $("#contentFrame",parent.document).attr('src',currentItem.url)
        }
    },
    created:function(){
        console.log('created')
        //检测登录态
        common.checkLogin(function(result){
            if(result && result.code==1){
                console.log('登录态校验成功.')
            }else{
                window.parent.location.href='../../html/login.html'
            }
        })
    },
})