$(function() {// 初始化内容
    //分页插件
    $(".m-style").pagination({
        totalData: 100,
        showData: 48,
        coping: true,
        callback: function(api) {
            console.log(api.getCurrent()); //切换页码时执行一次回调
        }
    });
    $('body').click(function() {
        closeSelect()
    });
    queryData();
});

// 初始化数据
function queryData() {
    // 表格数据
    $.ajax({
        type: "GET",
        url: "/data/executors",
        data: {pageSize:10,pageNumber:1},
        success: function(list){
            let str='';
            if (list) {
                for (let i = 0; i < list.length; i++) {
                    let executor = list[i];
                    str+=`
				<div class="table-row1" >
					<div class="table-block1 width10">${executor.id}</div>
					<div class="table-block1 width20">${executor.appCode}</div>
					<div class="table-block1 width20">${executor.appName}</div>
					<div class="table-block1 width20">${executor.secret}</div>
					<div class="table-block1 width20 color-blue change-hand flex-row2">
						<div class="handle-block" id="tableState" onclick="showHost({appCode:'${executor.appCode}',appName:'${executor.appName}',})">查看</div>
					</div>
					<div class="table-block1 width10 color-blue change-hand flex-row2">
                        <div class="handle-block" id="tableState">编辑</div>
                        <div class="handle-block" id="tableState">删除</div>
					</div>
				</div>
				`;
                }
            }
            $("#table").html(str);
        }
    });
}

function showHost(obj) {
    $("#hostList").empty();
    $("#hostName").text(obj.appName);
    $('#tk2').show();
    $.ajax({
        type: "GET",
        url: "/data/server/" + obj.appCode,
        success: function(list){
            let str='';
            if (list) {
                for (let i = 0; i < list.length; i++) {
                    let server = list[i];
                    str+=`
                    <div class="flex-row1 height-32">${server.ip}:${server.port}</div>
				    `;
                }
            }
            $("#hostList").html(str);
        }
    });
}