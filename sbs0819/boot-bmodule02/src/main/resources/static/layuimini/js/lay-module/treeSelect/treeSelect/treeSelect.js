layui.define(['form', 'jquery', 'ztree'], function(exports) { //提示：模块也可以依赖其它模块，如：layui.define('layer', callback);
    var jQuery = layui.jquery,
        $ = jQuery,
        form = layui.form,
        _MOD = 'treeSelect',
        trss = {},
        TreeSelect = function() {
            this.v = '1.0.5';
        };
    
	layui.link((layui.cache.base ? layui.cache.base : "") + '/treeSelect/treeSelect/treeSelect.css', "treeSelectcss");
	
    TreeSelect.prototype.render = function(option) {
    	this.options = $.extend(true, {}, TreeSelect.defaults, option);
    	var $this = this, options = this.options;
    	
        // 唯一id
    	var tmp = new Date().getTime(),
            DATA = {},
            selected = 'layui-form-selected',
            TREE_OBJ = undefined,
            TREE_INPUT_ID = 'treeSelect-input-' + tmp,
            TREE_INPUT_CLASS = 'layui-treeselect',
            TREE_SELECT_ID = 'layui-treeSelect-' + tmp,
            TREE_SELECT_CLASS = 'layui-treeSelect',
            TREE_SELECT_TITLE_ID = 'layui-select-title-' + tmp,
            TREE_SELECT_TITLE_CLASS = 'layui-select-title',
            TREE_SELECT_SEARCH_CLASS = 'layui-treeSelect-search',
            TREE_SELECT_CONFIRM_CLASS = 'layui-treeSelect-confirm',
            TREE_SELECT_BODY_ID = 'layui-treeSelect-body-' + tmp,
            TREE_SELECT_BODY_CLASS = 'layui-treeSelect-body',
            TREE_SELECT_SEARCHED_CLASS = 'layui-treeSelect-search-ed';
        var a = {
            init: function() {
            	a.hideElem();
                $.ajax({
                    url: options.url,
                    type: options.type,
                    headers: options.headers,
                    dataType: 'json',
                    success: function(d) {
                        if (d && options.dataRoot && typeof d === 'object' && d[options.dataRoot]) {
                            d = d[options.dataRoot];
                        }
                        a.input().toggleSelect().preventEvent().registeredEvent();
                        a.checkDefaultValue(d);
                        DATA = d;
                        $.fn.zTree.init($('#' + TREE_SELECT_BODY_ID), a.setting(), DATA);
                        TREE_OBJ = $.fn.zTree.getZTreeObj(TREE_SELECT_BODY_ID);
                        if (options.search) {
                            a.searchParam();
                        }
                        if (options.success) {
                            options.success.call($this, TREE_SELECT_ID, d);
                        }
                    }
                });
                return a;
            },
            // 检查input是否有默认值
            checkDefaultValue: function(rows) {
            	var defaultVal = $(options.elem).val();
            	if(defaultVal != null){
            		var split = defaultVal.split(options.separator);
            		var key = [], value = [];
            		$.each(rows, function(_i, item){
            			var idKey = "sid", checkedKey = "checked", nameKey = "name";
            			
            			if(typeof options.setting.data.simpleData.idKey === "string"){
            				idKey = options.setting.data.simpleData.idKey;
            			}
            			if(typeof options.setting.data.key.checked === "string"){
            				checkedKey = options.setting.data.key.checked;
        				}
            			if(typeof options.setting.data.key.name === "string"){
            				nameKey = options.setting.data.key.name;
        				}
            			
            			var key = item[idKey];
            			if(split.indexOf(key) > -1){
            				value.push(item[nameKey]);
            				item[checkedKey] = true;
            			}
            		});
            		$("#"+TREE_INPUT_ID).val(value.join(options.separator));
            	}
            },
            setting: function() {
                var set = {
                    callback: {
                        onClick: a.onClick,
                        onExpand: a.onExpand,
                        onCollapse: a.onCollapse,
                    }
                };
                set = $.extend(true, {}, set, options.setting);
                return set;
            },
            onCollapse: function(event, treeId, treeNode) {
            	a.focusInput();
            	if(typeof options.onCollapse === 'function'){
        			options.onCollapse.call($this, event, treeId, treeNode);
        		}
            },
            onExpand: function(event, treeId, treeNode) {
            	a.focusInput();
            	if(typeof options.onExpand === 'function'){
        			options.onExpand.call($this, event, treeId, treeNode);
        		}
            },
            focusInput: function() {
                $('#' + TREE_INPUT_ID).focus();
            },
            onClick: function(event, treeId, treeNode) {
            	if(typeof options.onClick === 'function'){
        			options.onClick
        		}else if (options.setting.check.enable === false || options.setting.check.chkStyle === "radio") {
                    var name = treeNode[options.setting.data.key.name],
                    id = treeNode[options.setting.data.simpleData.idKey],
                    $input = $('#' + TREE_SELECT_TITLE_ID + ' input');
	                $input.val(name);
	                $input.attr("title", name)
	                var tagName = $(options.elem)[0].tagName;
	                if(tagName === "INPUT" || tagName === "SELECT" || tagName === "TEXTAREA"){
	                	$(options.elem).val(id);
	                }else{
	                	$(options.elem).attr("data-value", id);
	                }
	                $('#' + TREE_SELECT_ID).removeClass(selected);
	            }
                return a;
            },
            hideElem: function() {
            	$(options.elem).show();
            	options.elemWidth = $(options.elem).width();
                $(options.elem).hide();
                return a;
            },
            input: function() {
            	$(options.elem).next("."+TREE_SELECT_CLASS).remove();
                var selectHtml = [];
                selectHtml.push("<div class=\""+TREE_SELECT_CLASS+" layui-unselect layui-form-select\" id=\""+TREE_SELECT_ID+"\" style=\""+(options.elemWidth?"width:"+options.elemWidth+"px":"")+"\">");
                selectHtml.push("    <div class=\""+TREE_SELECT_TITLE_CLASS+"\" id=\""+TREE_SELECT_TITLE_ID+"\">");
                selectHtml.push("        <input type=\"text\" id=\""+TREE_INPUT_ID+"\" placeholder=\""+options.placeholder+"\" value=\"\" readonly class=\"layui-input layui-unselect\"><i class=\"layui-edge\"></i>");
                selectHtml.push("    </div>");
                
                selectHtml.push("    <div class=\"layui-anim layui-anim-upbit\"");
                if(options.width){
                	selectHtml.push(" style=\"width:"+options.width+"\"");
                }
                selectHtml.push(">");
                
                if(options.search || (options.setting.check.enable && options.setting.check.chkStyle === "checkbox")){
                	selectHtml.push("    <div class=\"layui-treeselect-header\">");
                	if(options.search){
                    	//搜索框
                		if(options.setting.check.enable && options.setting.check.chkStyle === "checkbox"){
                			selectHtml.push("    <div class=\""+TREE_SELECT_SEARCH_CLASS+"\">");
                		}else{
                			selectHtml.push("    <div>");
                		}
    	                selectHtml.push("        <input type=\"text\" placeholder=\""+options.searchPlaceholder+"\" value=\"\" class=\"layui-input\">");
    	                selectHtml.push("    </div>");
                    }
                    if(options.setting.check.enable && options.setting.check.chkStyle === "checkbox"){
    	                //多选
    	            	selectHtml.push("    <div class=\""+TREE_SELECT_CONFIRM_CLASS+"\">");
    	                selectHtml.push("        <span class=\"layui-btn layui-btn-primary layui-btn-xs\" lay-even=\"select-confirm\">确认</span>");
    	                selectHtml.push("    </div>");
                    }
                	selectHtml.push("    </div>");
                }
                selectHtml.push("        <div class=\""+TREE_SELECT_BODY_CLASS+" ztree\" id=\""+TREE_SELECT_BODY_ID+"\"></div>");
                selectHtml.push("    </div>");
                selectHtml.push("</div>");
                $(options.elem).parent().append(selectHtml.join(""));
                return a;
            },
            /**
             * 展开/折叠下拉框
             */
            toggleSelect: function() {
                var item = '#' + TREE_SELECT_TITLE_ID;
                a.event('click', item, function(e) {
                    var $select = $('#' + TREE_SELECT_ID);
                    if ($select.hasClass(selected)) {
                        $select.removeClass(selected);
                        $('#' + TREE_INPUT_ID).blur();
                    } else {
        	        	var $title = $select.find(".layui-select-title");
        	        	var $anim = $select.find(".layui-anim-upbit");
        	        	var w = $title.width();
        	        	var offset = $title.offset();
        	        	if(!$anim.attr("style")){
        	        		$anim.width(w);
        	        	}
        	        	if(offset){
        	        		$anim.css({"left":offset.left, "top": offset.top+40});
        	        	}
                        // 隐藏其他picker
                        $('.layui-form-select').removeClass(selected);
                        // 显示当前picker
                        $select.addClass(selected);
                    }
                    e.stopPropagation();
                });
                $(document).on("click", function() {
                    var $select = $('#' + TREE_SELECT_ID);
                    if ($select.hasClass(selected)) {
                        $select.removeClass(selected);
                        $('#' + TREE_INPUT_ID).blur();
                    }
                })
                $(window).on("resize", function() {
                    var $select = $('#' + TREE_SELECT_ID);
                    $select.removeClass(selected);
                });
                return a;
            },
            // 模糊查询
            searchParam: function() {
                if (!options.search) {
                    return;
                }
                var item = '#'+TREE_SELECT_ID+" .layui-treeselect-header input";
                a.fuzzySearch(item, null, true);
            },
            fuzzySearch: function(searchField, isHighLight, isExpand) {
                var zTreeObj = TREE_OBJ; //get the ztree object by ztree id
                if (!zTreeObj) {
                    return;
                }
                var nameKey = zTreeObj.setting.data.key.name; //get the key of the node name
                isHighLight = isHighLight === false ? false : true; //default true, only use false to disable highlight
                isExpand = isExpand ? true : false; // not to expand in default
                zTreeObj.setting.view.nameIsHTML = isHighLight; //allow use html in node name for highlight use
                var metaChar = '[\\[\\]\\\\\^\\$\\.\\|\\?\\*\\+\\(\\)]'; //js meta characters
                var rexMeta = new RegExp(metaChar, 'gi'); //regular expression to match meta characters
                // keywords filter function 
                function ztreeFilter(zTreeObj, _keywords, callBackFunc) {
                    if (!_keywords) {
                        _keywords = ''; //default blank for _keywords 
                    }
                    // function to find the matching node
                    function filterFunc(node) {
                        if (node && node.oldname && node.oldname.length > 0) {
                            node[nameKey] = node.oldname; //recover oldname of the node if exist
                        }
                        zTreeObj.updateNode(node); //update node to for modifications take effect
                        if (_keywords.length == 0) {
                            //return true to show all nodes if the keyword is blank
                            zTreeObj.showNode(node);
                            zTreeObj.expandNode(node, isExpand);
                            return true;
                        }
                        //transform node name and keywords to lowercase
                        if (node[nameKey] && node[nameKey].toLowerCase().indexOf(_keywords.toLowerCase()) != -1) {
                            zTreeObj.showNode(node); //show node with matching keywords
                            return true; //return true and show this node
                        }
                        zTreeObj.hideNode(node); // hide node that not matched
                        return false; //return false for node not matched
                    }
                    var nodesShow = zTreeObj.getNodesByFilter(filterFunc); //get all nodes that would be shown
                    processShowNodes(nodesShow, _keywords); //nodes should be reprocessed to show correctly
                }
                /**
                 * reprocess of nodes before showing
                 */
                function processShowNodes(nodesShow, _keywords) {
                    if (nodesShow && nodesShow.length > 0) {
                        //process the ancient nodes if _keywords is not blank
                        if (_keywords.length > 0) {
                            $.each(nodesShow, function(n, obj) {
                                var pathOfOne = obj.getPath(); //get all the ancient nodes including current node
                                if (pathOfOne && pathOfOne.length > 0) {
                                    //i < pathOfOne.length-1 process every node in path except self
                                    for (var i = 0; i < pathOfOne.length - 1; i++) {
                                        zTreeObj.showNode(pathOfOne[i]); //show node 
                                        zTreeObj.expandNode(pathOfOne[i], true); //expand node
                                    }
                                }
                            });
                        } else { //show all nodes when _keywords is blank and expand the root nodes
                            var rootNodes = zTreeObj.getNodesByParam('level', '0'); //get all root nodes
                            $.each(rootNodes, function(n, obj) {
                                zTreeObj.expandNode(obj, true); //expand all root nodes
                            });
                        }
                    }
                }
                //listen to change in input element
                $(searchField).bind('input propertychange', function() {
                    var _keywords = $(this).val();
                    searchNodeLazy(_keywords); //call lazy load
                });
                var timeoutId = null;
                // excute lazy load once after input change, the last pending task will be cancled  
                function searchNodeLazy(_keywords) {
                    if (timeoutId) {
                        //clear pending task
                        clearTimeout(timeoutId);
                    }
                    timeoutId = setTimeout(function() {
                        ztreeFilter(zTreeObj, _keywords); //lazy load ztreeFilter function 
                        $(searchField).focus(); //focus input field again after filtering
                    }, 500);
                }
            },
            // 阻止Layui的一些默认事件
            preventEvent: function() {
                var item = '#' + TREE_SELECT_ID + ' .layui-anim';
                a.event('click', item, function(e) {
                    e.stopPropagation();
                });
                return a;
            },
            registeredEvent: function() {
                var item = '#' + TREE_SELECT_ID + ' .layui-anim';
                $(item).on("click", ".layui-treeSelect-confirm", function(){
                	var nodes = TREE_OBJ.getCheckedNodes(true);
                	// 触发onSelect方法
                	if(typeof options.onSelect === "function"){
                		var flag = options.onSelect.call(TREE_OBJ, nodes, TREE_SELECT_ID);
                		if(flag != null && flag == false){
                			return false;
                		}
                	}else{
                		if(nodes != null && nodes.length>0){
                    		var key = [], value = [];
                    		$.each(nodes, function(_i, item){
                    			value.push(item[TREE_OBJ.setting.data.key.name]);
                    			key.push(item[TREE_OBJ.setting.data.simpleData.idKey]);
                    		});
                    		var tagName = $(options.elem)[0].tagName;
        	                if(tagName === "INPUT" || tagName === "SELECT" || tagName === "TEXTAREA"){
        	                	$(options.elem).val(key.join(","));
        	                }else{
        	                	$(options.elem).attr("data-value", key.join(","));
        	                }
                    		$("#"+TREE_INPUT_ID).val(value.join(",")).attr("title", value.join(","));
                    	}
                	}
                	
                	var $select = $('#' + TREE_SELECT_ID);
                    if ($select.hasClass(selected)) {
                        $select.removeClass(selected);
                        $('#' + TREE_INPUT_ID).blur();
                    }
                });
                return a;
            },
            event: function(evt, el, fn) {
                $('body').on(evt, el, fn);
            }
        };
        a.init();
        return new TreeSelect();
    };
    /**
     * 重新加载trerSelect
     * @param filter
     */
    TreeSelect.prototype.refresh = function(filter) {
        var treeObj = obj.treeObj(filter);
        treeObj.reAsyncChildNodes(null, "refresh");
    };
    /**
     * 选中节点，因为tree是异步加载，所以必须在success回调中调用checkNode函数，否则无法获取生成的DOM元素
     * @param filter lay-filter属性
     * @param id 选中的id
     */
    TreeSelect.prototype.checkNode = function(filter, id) {
        var o = obj.filter(filter),
            treeInput = o.find('.layui-select-title input'),
            treeObj = obj.treeObj(filter),
            node = treeObj.getNodeByParam("id", id, null),
            name = node.name;
        treeInput.val(name);
        o.find('a[treenode_a]').removeClass('curSelectedNode');
        obj.get(filter).val(id).attr('value', id);
        treeObj.selectNode(node);
    };
    /**
     * 撤销选中的节点
     * @param filter lay-filter属性
     * @param fn 回调函数
     */
    TreeSelect.prototype.revokeNode = function(filter, fn) {
        var o = obj.filter(filter);
        o.find('a[treenode_a]').removeClass('curSelectedNode');
        o.find('.layui-select-title input.layui-input').val('');
        obj.get(filter).attr('value', '').val('');
        obj.treeObj(filter).expandAll(false);
        if (fn) {
            fn({
                treeId: o.attr('id')
            });
        }
    }
    /**
     * 销毁组件
     */
    TreeSelect.prototype.destroy = function(filter) {
        var o = obj.filter(filter);
        o.remove();
        obj.get(filter).show();
    }
    /**
     * 获取zTree对象，可调用所有zTree函数
     * @param filter
     */
    TreeSelect.prototype.zTree = function(filter) {
        return obj.treeObj(filter);
    };
    var obj = {
        get: function(filter) {
            if (!filter) {
                layui.hint().error('filter 不能为空');
            }
            return $('*[lay-filter=' + filter + ']');
        },
        filter: function(filter) {
            var tf = obj.get(filter),
                o = tf.next();
            return o;
        },
        treeObj: function(filter) {
            var o = obj.filter(filter),
                treeId = o.find('.layui-treeSelect-body').attr('id'),
                tree = $.fn.zTree.getZTreeObj(treeId);
            return tree;
        }
    };
    
    /**
	 * 默认参数信息
	 */
    TreeSelect.defaults = {
		elem: '',
        type: 'get',
        search: false,
        searchPlaceholder: '搜索',
        placeholder: '请选择',
        separator: ",",
        setting: {
        	data: {
    			key: {
    				checked: "checked",
    				children: "children",
    				name: "name",
    				title: "",
    				url: "url",
    				iconSkin:"iconSkin",
    				isParent:"isParent"
    			},
    			simpleData: {
    				enable: false,
    				idKey: "sid",
    				pIdKey: "pid",
    				rootPId: null
    			},
    			keep: {
    				parent: false,
    				leaf: false
    			}
    		},
            // 节点配置
    		check: {
    			enable: false,
    			autoCheckTrigger: false,
    			chkStyle: "checkbox",
    			nocheckInherit: false,
    			chkDisabledInherit: false,
    			radioType: "level",
    			chkboxType: {
    				"Y": "ps",
    				"N": "ps"
    			}
    		}
        },
        onSelect: null // 多选时，点击确认按钮触发
	}
    //输出接口
    var mod = new TreeSelect();
    exports(_MOD, mod);
});
