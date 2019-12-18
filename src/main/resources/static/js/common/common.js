
var BASIC_URL = "/category";
var App = new Vue({
    el: '.content-wrapper',
    data: function(){
        return{
            categories: [],
            name: ''
        }
    },
    created : function(){
        this.init();
    },
    mounted: function(){

    },
    methods: {
        init : function(){
            var vm = this;
            $.ajax({
                url: BASIC_URL + "/list",
                type: "GET",
                async: false,
                success : function (res) {
                    vm.categories = res.data || {};
                },
                error: function() {
                    console.log("error");
                }
            });
        },
        addCategory: function () {
            var vm = this;
            var name = vm.name;
            if (name === ''){
                alert("该项不能为空");
                return;
            }
            $.ajax({
                url: BASIC_URL + "/add",
                type: "POST",
                data: {"name": name},
                async: false,
                success : function (res) {
                    alert(res.msg);
                    window.location.reload();
                },
                error: function() {
                    console.log("error");
                }
            });
        }
    }
});
