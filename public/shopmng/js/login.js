/**
 * Created by fish on 2018/3/29.
 */
var container = new Vue({
    el: '#container',
    data: {
        userName: '',
        password:'',
        errorMsg:'',
        logining:false
    },
    methods:{
        login:function(event){
            var that = this
            that.logining = true
            console.log('username',that.userName,'password',that.password)
            $.ajax(
                {
                    url:loginURL,
                    type : "POST",
                    dataType:"json",
                    data : {
                        "userName" : that.userName,
                        "password": that.password
                    },
                    success:function(result){
                        if(result && result.code==1){
                            window.location.href='../html/index.html'
                        }else{
                            that.errorMsg = result.msg
                        }
                        that.logining = false
                    }
                });

        }
    }
    ,
    created:function(){
        console.log('created')
        //¼ì²âµÇÂ¼Ì¬
        common.checkLogin(function(result){
            if(result && result.code==1){
                console.log('µÇÂ¼Ì¬Ð£Ñé³É¹¦.')
                window.parent.location.href='../html/index.html'
            }else{

            }
        })
    },
})
