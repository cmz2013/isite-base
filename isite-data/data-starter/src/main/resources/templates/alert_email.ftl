<style>
    .table11_3 table {
        width:100%;
        margin:15px 0;
        border:0;
    }
    .table11_3 th {
        background-color:#FF5675;
        font-size:1.4em;
        color:#FFFFFF;
        text-align:center;
    }
    .table11_3,.table11_3 td {
        font-size:0.9em;
        padding:4px;
        border-collapse:collapse;
    }
    .table11_3 tr {
        border: 1px solid #ffffff;
    }
    .row_h {
        width: 80px;
        text-align: right;
        font-weight: bold;
    }
</style>
<table class=table11_3>
    <tr>
        <th colspan="2">${dataApi.appCode} 告警</th>
    </tr>
    <tr>
        <td class="row_h">数据接口</td><td>${dataApi.wsType.label}: ${dataApi.id}</td>
    </tr>
    <tr>
        <td class="row_h">实现类</td><td>${dataLog.apiClass}</td>
    </tr>
    <#if dataApi.wsType.code == 0>
        <tr>
            <td class="row_h">接口地址</td><td>${dataApi.serverUrl}</td>
        </tr>
    </#if>
    <tr>
        <td class="row_h">请求参数</td><td>${dataLog.reqData?default("")}</td>
    </tr>
    <tr>
        <td class="row_h">响应数据</td><td>${dataLog.repData?default("")}</td>
    </tr>
    <tr>
        <td class="row_h">执行结果</td><td>${dataLog.status?string("成功", "失败")}</td>
    </tr>
    <tr>
        <td class="row_h">备注</td><td>${dataLog.remark?default("")}</td>
    </tr>
</table>