$(function() {// 初始化内容
    //分页插件
    $(".m-style").pagination({
        totalData: 100,
        showData: 48,
        coping: true
    });

    $('body').click(function() {
        closeSelect()
    })
    queryData();
});

function queryData() {
// 表格数据
    $.ajax({
        type: "GET",
        url: "/data/users",
        data: {pageSize:10,pageNumber:1},
        success: function(result){
            let str='';
            if (result.data) {
                for (let i = 0; i < result.data.length; i++) {
                    let user = result.data[i];
                    str+=`
                <div class="table-row1">
                     <div class="table-block1 width25">${user.userName}</div>
                     <div class="table-block1 width25">查看权限：项目管理系统</div>
                     <div class="table-block1 width25">管理员</div>
                     <div class="table-block1 width25 color-blue change-hand flex-row2"> 
                          <div class="handle-block" onclick="edit()">编辑</div>
                          <div class="handle-block" onclick="">删除</div>
                     </div>
                </div>
                `;
                }
            }
            $("#table").html(str);
        }
    });
}

function edit() {
}