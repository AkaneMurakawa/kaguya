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
            formData: []
        }
    },
    created : function(){
    },
    mounted: function(){
    },
    methods: {
        submit : function(){
            var vm = this;
            alert("submit");
            vm.formData.contentHTML = editor.getHTML();
            $.ajax({
                url: BASIC_URL + "/add",
                type: "POST",
                dataType: "json",
                data: formData,
                async: true,
                success : function (res) {
                    console.log(res);
                    vm.categories = res.data || {};
                },
                error: function() {
                    console.log("error");
                },
            });
        },
        validate: function () {
            // editor.getMarkdown()); // 获取Markdown正常
            // editor.getHTML(); // 获取HTML正常，需要开启saveHTMLToTextarea : true,
            // 在前端直接传入的是HTMl，而不是markdown
        }
    }
});
