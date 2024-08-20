$(function () {
	let userDetails = localStorage.getItem("userResources");
	if (userDetails) {
		initMenu(JSON.parse(userDetails));
	} else {
		parent.location.href = '/login.html';
	}
});

//设置菜单栏
function initMenu(userDetails) {
	$(".page-right").prepend(
		'<div class="page-right-top">'+
		'   <div></div>'+
		'   <div class="page-right-top-right">欢迎 '+userDetails.username+'</div>'+
		'</div>');

	let resources = userDetails.resources;
	let leftStr = '<a class="page-left-top">数据集成</a>';
	if (resources && resources.length > 0) {
		resources = resources[0].children;
		for (let i = 0; i < resources.length; i++) {
			if (window.location.href.indexOf(resources[i].href)>-1) {
				leftStr+=`
				<a href="${resources[i].href}" class="page-left-block page-left-block-active">
					<div class="page-left-ring" style="border-color:${resources[i].icon}"></div>${resources[i].resourceName}
				</a>
				`;
			}else{
				leftStr+=`
				<a href="${resources[i].href}" class="page-left-block">
					<<div class="page-left-ring" style="border-color:${resources[i].icon}"></div>${resources[i].resourceName}
				</a>
				`;
			}
		}
	}
	$(".page-left").prepend(leftStr);
}