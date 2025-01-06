$(function() {
    queryData();
});

// 初始化数据
function queryData() {
    // 表格数据
    $.ajax({
        type: "GET",
        url: "/data/report",
        success: function(data){
            $("#apiNumber").html(data.apiNumber);
            $("#executorNumber").html(data.executorNumber);
            $("#callLatestNumber").html(data.callLatestNumber);
            initZheXian(data.callTimes, data.callSuccessDetails, data.callFailureDetails);
            initBing(data.callLatestNumber, data.callFailureNumber);
        }
    });
}

// 初始化数据
function initZheXian(callTimes, callSuccessDetails, callFailureDetails) {
    echarts.init(document.getElementById('zhexian')).setOption({
        title: {
            text: '时间分部图'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data: ['成功', '失败']
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        grid: {
            left: '3%',
            right: '7%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: callTimes
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: '失败',
                type: 'line',
                areaStyle: {},
                data: callFailureDetails
            },
            {
                name: '成功',
                type: 'line',
                label: {
                    normal: {
                        show: true,
                        position: 'top'
                    }
                },
                areaStyle: {},
                data: callSuccessDetails
            }
        ]
    })
}

function initBing(callLatestNumber, callFailureNumber) {
    // 表格数据
    echarts.init(document.getElementById('bing')).setOption({
        title: {
            text: '成功比例图',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: ['失败', '成功']
        },
        series: [
            {
                name: '接口调用',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: [
                    {value: callFailureNumber, name: '失败'},
                    {value: (callLatestNumber - callFailureNumber), name: '成功'}
                ],
                emphasis: {
                    itemStyle: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    })
}