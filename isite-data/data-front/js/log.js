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
        closeSelect();
    })
    queryData();
});

// 初始化数据
function queryData() {
    $.ajax({
        type: "GET",
        url: "/data/logs",
        /**
         * JQuery Ajax传递PageRequest对象:
         * {pageSize:10,pageNumber:1,orders:[{property:'create_time', direction:'DESC'}]}
         * JQuery Ajax会将orders数组参数映射成这样：
         * orders[0][property]=create_time
         * orders[0][direction]=DESC
         *
         * 而Spring MVC需要这种的参数格式：
         * orders[0].property=create_time
         * orders[0].direction=DESC
         *
         * 所以，JQuery Ajax使用以下方式传递orders：
         * {pageSize:10,pageNumber:1,'orders[0].property':'create_time', 'orders[0].direction':'DESC'}
         */
        data: {pageSize:10,pageNumber:1,'orders[0].property':'create_time', 'orders[0].direction':'DESC'},
        success: function(list){
            let str='';
            if (list) {
                for (let i = 0; i < list.length; i++) {
                    let log = list[i];
                    str+=`<div class="table-row1">${toLogHtml(log)}</div>`;
                }
            }
            $("#table").html(str);
        }
    });
}

function toLogHtml(log) {
    return `
        <div class="table-block1 width4">${log.id}</div>
        <div class="table-block1 width15">${log.appCode}</div>
        <div class="table-block1 width20">
            <div class="padding-lr-8 change-point" title=${log.reqData}>${log.reqData}</div>
        </div>
        <div class="table-block1 width20">
            <div class="padding-lr-8 change-point" title=${log.repData}>${log.repData}</div>
        </div>
        <div class="table-block1 width6">${log.status ? '成功' : '失败'}</div>
        <div class="table-block1 width5">
            <div class="padding-lr-8 change-point" title=${log.remark}>${log.remark}</div>
        </div>
        <div class="table-block1 width10">${log.createTime}</div>
        <div class="table-block1 width10">${log.updateTime}</div>
        <div class="table-block1 width10 color-blue change-hand flex-row2">
            ${log.status ? `` : `<div class="handle-block" id="tableState" onclick="compensate(this,{logId:'${log.id}'})">数据补偿</div>`}
        </div>`;
}

// 点击搜索
function clickSearch() {
    // body...
}

// 数据补偿
function compensate(_this, obj) {
    $.ajax({
        type: "PUT",
        url: "/data/log/" + obj.logId + "/compensate",
        dataType: "json",
        success: function(data) {
            let log = data.dataLog;
            showToast(log.status ? '数据补偿成功！' : '数据补偿失败！');
            if (!data.storage) {
                $(_this).parent().parent().remove();
            } else {
                $(_this).parent().parent().html(`${toLogHtml(log)}`);
            }
        }
    })
}