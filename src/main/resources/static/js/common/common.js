
var BASIC_URL = "/category";
var App = new Vue({
    el: '.content-wrapper',
    data: function(){
        return{
            categories: []
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
                async: true,
                success : function (res) {
                    vm.categories = res.data || {};
                },
                error: function() {
                    console.log("error");
                },
            });
        },
    }
});
