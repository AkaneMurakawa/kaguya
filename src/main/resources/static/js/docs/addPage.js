var editor;

$(function () {
    // 更多设置请参考官网
    editor = editormd("editormd", {
        width			: "100%",
        height		    : 520,
        path			: "/editormd/lib/",
        theme			: "default",
        previewTheme	: "default",
        editorTheme 	: editormd.editorThemes['elegant'],
        codeFold		: true,
        watch			: true,
        syncScrolling   : true, // 同步
        saveHTMLToTextarea : true,
        searchReplace	: true,
        htmlDecode      : "style,script,iframe", // 开启html标签解析
        emoji           : true,
        taskList        : true,
        tocm			: true,
        tex             : true, // 开启科学公式TeX语言支持
        flowChart       : true, // 开启流程图支持
        sequenceDiagram : true, // 开启时序/序列图支持
        imageUpload		: true,
        imageFormats	: ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
        imageUploadURL	: "${APP_PATH}/user/uploadImgEditormd?uid=${loginUser.id}"
    });
});

var BASIC_URL = "/docs";

var App = new Vue({
    el: '.docs',
    data: function(){
        return{
            categories: [],
            parents: [],
            document: {
                title: '',
                content: '',
                contentHTML: '',
                categoryId: '',
                parentId: ''
            }
        }
    },
    created: function(){
        this.init();
    },
    mounted: function(){
    },
    watch: {
        'document.categoryId': function(value){
            if (value !== ''){
                var vm = this;
                $.ajax({
                    url: BASIC_URL + "/getParentsId/" + vm.document.categoryId,
                    type: "GET",
                    async: true,
                    success : function (res) {
                        vm.parents = res.data || {};
                    },
                    error: function() {
                        console.log("error");
                    },
                });
            }
        }
    },
    methods: {
        init : function(){
            var vm = this;
            $.ajax({
                url: "/category/list",
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
        submit : function(){
            this.validate()
        },
        validate: function () {
            var vm = this;
            $("#docs-form").validate({
                debug: true,//只验证不提交表单
                rules:{
                    title: {required: true},
                    categoryId: {required: true}
                },
                messages: {
                    title: '该项不能为空',
                    categoryId: '该项不能为空'
                },
                submitHandler: function() {
                    vm.save();
                }
            });
        },
        save: function () {
            var vm = this;
            vm.document.content = editor.getMarkdown();
            vm.document.contentHTML = editor.getHTML();
            $.ajax({
                url: BASIC_URL + "/add",
                type: "POST",
                data: JSON.stringify(vm.document),
                contentType: 'application/json;charset-UTF-8',
                cache: false,
                async: false,
                success : function (res) {
                    alert(res.msg);
                    window.location.reload();
                },
                error: function() {
                    console.log("error");
                },
            });
        }
    }
});
