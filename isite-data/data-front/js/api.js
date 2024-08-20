$(function() {
    //分页插件
    $(".m-style").pagination({
        totalData: 100,
        showData: 48,
        coping: true,
        callback: function(api) {
            api.getCurrent(); //切换页码时执行一次回调
        }
    });
    $('body').click(function() {
        closeSelect();
    });
    queryData();
});

// 初始化数据
function queryData() {
    let wsType = '远程回调接口' == $("#wsType").val() ? 0 : 1;
    if (1 == wsType) {
        $("#accessProtocol").attr("disabled","disabled");
    } else {
        $("#accessProtocol").removeAttr("disabled");
    }
    // 表格数据
    $.ajax({
        type: "GET",
        url: "/data/ifcs",
        data: {
            wsType: wsType,
            accessProtocol: 1 == wsType ? "" : $("#accessProtocol").val(),
            appCode: $("#appCode").val(),
            pageSize:10,
            pageNumber:1
        },
        success: function(list) {
            let str='';
            if (list) {
                for (let i = 0; i < list.length; i++) {
                    let dataApi = list[i];
                    str+=`
				<div class="table-row1" >
					<div class="table-block1 width4">${dataApi.id}</div>
					<div class="table-block1 width11"><div class="padding-lr-8 change-point" title=${dataApi.appCode}>${dataApi.appCode}</div></div>
					<div class="table-block1 width15"><div class="padding-lr-8 change-point" title=${dataApi.serverUrl}>${dataApi.serverUrl}</div></div>
					<div class="table-block1 width10">${dataApi.method}</div>
					<div class="table-block1 width10">${dataApi.args}</div>
					<div class="table-block1 width6">${dataApi.wsNameSpace}</div>
					<div class="table-block1 width6">${dataApi.wsPointName}</div>
					<div class="table-block1 width6">${null == dataApi.timeout ? '' : dataApi.timeout}</div>
					<div class="table-block1 width15"><div class="padding-lr-8 change-point" title=${dataApi.emails}>${dataApi.emails}</div></div>
					<div class="table-block1 width7">
                        <div class="switch">
                            <input class="switch-checkbox" id="onoff-switch-${dataApi.id}" type="checkbox" ${dataApi.status=='1'?'checked':''}>
                            <label class="switch-label" for="onoff-switch-${dataApi.id}" onclick="clickSwitch('${dataApi.id}')">
                                <span class="switch-inner" data-on="启用" data-off="禁用"></span>
                                <span class="switch-switch"></span>
                            </label>
                        </div>
                    </div>
					<div class="table-block1 width10 color-blue change-hand flex-row2"> 
						<div class="handle-block" onclick="">编辑</div>
						<div class="handle-block" onclick="showDelInfo('${dataApi.id}')">删除</div>
					</div>
				</div>
				`;
                }
            }
            $("#table").html(str);
        }
    });
}

function clickSwitch(id) {
    let status = !$("#onoff-switch-"+id).is(':checked');
    $.ajax({
        type: "PUT",
        url: "/data/ifc/"+id+"/status/"+(status ? 1 : 0),
        failure: function () {
            $("#onoff-switch-"+id).attr('checked', !status);
        },
        error: function () {
            $("#onoff-switch-"+id).attr('checked', !status);
        }
    });
}

//显示删除确认弹框
function showDelInfo(id) {
    $("#delInfo").text('你确定要删除接口['+id+']吗？');
    $("#tk1").show();
}