layui.use(['element', 'jquery'], function () {
    var element = layui.element,
        $ = layui.jquery;


    element.on('nav(demo)', function (elem) {
        //console.log(elem.getAttribute("meun_id")); //得到当前点击的DOM对象
        var id = $(this).data("menuid");
        var menuName = $(this).data("menuname");
        var url = $(this).data("href");
        console.log('menu_id=' + id);
        if (typeof (id) != "undefined" ) {
            console.log('进入方法');
            element.tabAdd('demo1', {
                title: menuName //用于演示
                ,
                content: '<iframe src="'+url+'" style="width:100%;height:491px;" scrolling="auto" frameborder="0"></iframe>'
                ,
                id: id//实际使用一般是规定好的id，这里以时间戳模拟下z
            })
            element.tabChange('demo1', id);
            // element.tab
        }
    });

    $("#content").load('/dashboard');

    // 动态加载菜单
    //获取所有的菜单
    $.ajax({
        type: "GET",
        url: "/indexMenuTree",
        dataType: "json",
        success: function (res) {
            //先添加所有的主材单
            var data = res;
            var content = '';
            $.each(data, function (i, obj) {
                content += '<li class="layui-nav-item">';
                content += '<a href="javascript:;" data-menuname="' + obj.meunTitle + '" ';
                if (obj.leafAble) {
                    content += ' data-href="' + obj.meunUrl + '"  data-menuid= "' + obj.id + '" ';
                }
                content += '>' + obj.meunTitle + '</a>';
                //这里是添加所有的子菜单
                content += loadchild(obj);
                content += '</li>';

            });
            $(".layui-nav-tree").html(content);
            element.init();
        },
        error: function (jqXHR) {
            // alert("发生错误："+ jqXHR.status);
            console.log("发生错误：" + jqXHR.status);

        }
    });

    //组装子菜单的方法
    function loadchild(obj) {
        if (obj == null) {
            return;
        }
        var content = '';
        if (obj.childrenList != null && obj.childrenList.length > 0) {
            content += '<dl class="layui-nav-child">';
        } else {
            content += '<dl>';
        }

        if (obj.childrenList != null && obj.childrenList.length > 0) {
            $.each(obj.childrenList, function (i, note) {
                content += '<dd>';
                content += '<a href="javascript:;" data-menuname="' + note.meunTitle + '" ';
                if (note.leafAble) {
                    content += ' data-href="' + note.meunUrl + '" data-menuid= "' + note.id + '" ';
                }
                content += '>' + note.meunTitle + '</a>';
                if (note.childrenList == null) {
                    return;
                }
                content += loadchild(note);
                content += '</dd>';
            });
            content += '</dl>';
        }
        return content;
    }


});
