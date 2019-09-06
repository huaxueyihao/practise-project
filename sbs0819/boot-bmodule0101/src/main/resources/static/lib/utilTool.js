layui.define(['layer'], function (exports) {
    "use strict";
    var $ = layui.jquery,
        layer = layui.layer;
    var utilTool = {

        /**
         * 弹出层表单提交
         * {}
         *
         */
        formSubmit: function (index,layero, btnId, url, success, error) {
            var iframeWin = window[layero.find('iframe')[0]['name']];
            var submitBtn = layero.find('iframe').contents().find('#'+btnId);

            //保存按钮单击事件
            iframeWin.layui.form.on('submit(' + btnId + ')', function (data) {
                var field = data.field; //获取表单数据
                console.log(field);
                $.ajax({
                    url: url,
                    method: 'post',
                    data: JSON.stringify(data.field),
                    contentType: 'application/json',
                    dataType: 'JSON',
                    success: success,
                    error: error
                });
            });
            submitBtn.click();

        },
    }

    exports('utilTool', utilTool);
})
