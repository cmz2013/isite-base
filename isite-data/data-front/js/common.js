let apiGateway = {
	dev: "http://isite-dev.com:7010",
	test: "http://isite-test.com:7010",
	pro: "http://isite.com:7010"
};

$(function () {
	//完成页面初始化
	pageSetup();
	//完成$.ajax()的默认设置
	ajaxSetup();
});

function pageSetup() {
	let pageRight = $(".page-right").length > 0;
	let target = pageRight ? $(".page-right") : $("body");
	<!-- 提示信息 -->
	target.prepend('<div class="m-fixed-toast"></div>');
	<!-- loading -->
	target.prepend('<div class="loading">' +
		'<div class="bounce1"></div>' +
		'<div class="bounce2"></div>' +
		'<div class="bounce3"></div>' +
		'</div>');
}

function ajaxSetup() {
	$.ajaxSetup({
		dataType: "json",
		cache: false,

		//发送请求之前拦截处理
		beforeSend: function(xhr, options) {
			//options.async为true时，发送请求之前显示loading。ajax如果设置async为false，因为同步操作会导致loading在请求完成后才显示，然后立即消失。
			showLoading();
			options.url = apiGateway[getProfiles()] + options.url;
			if (localStorage.getItem("token") && (!options.headers || !options.headers.Authorization)) {
				xhr.setRequestHeader("Authorization", "Bearer " + localStorage.getItem("token"));
			}

			let success = options.success;
			let error = options.error;
			let failure = options.failure;

			// http status code 200-299
			options.success = function(result, textStatus, xhr) {
				removeLoading();
				if (result.code >= 200 && result.code <= 299) {
					if (success) {
						arguments[0] = result.data;
						success.apply(this, arguments);
					}
					if (result.message) {
						showToast(result.message);
					}
				} else if (401 == result.code) {
					//无效token 401，重定向到登录页面(登录接口失败 407)
					parent.location.href = '/login.html';
				} else {
					if (failure) {
						arguments[0] = result.data;
						failure.apply(this, arguments);
					}
					if (result.message) {
						showToast(result.message);
					}
				}
			};
			options.error = function(request, textStatus, thrown) {
				removeLoading();
				if (error) {
					error.apply(this, arguments);
				} else {
					showToast('服务器维护中');
				}
			};
		}
	});
}

function showLoading(){
	let obj = $(".loading");
	if(obj.data("mask")) {
		return;
	}
	// 打开遮罩
	obj.before('<div class="divMask" style="position: absolute; width: 100%; height: 100%; z-index:997; background: #EEEEEE;opacity: 0.3; filter: alpha(opacity=30);"></div>');
	obj.data("mask",true);
	obj.show();
}

function removeLoading(){
	let obj = $(".loading");
	if(obj.data("mask")) {
		obj.hide(0);
		// 取消遮罩
		obj.parent().find(".divMask").remove();
		obj.data("mask",false);
	}
}

// 关闭弹框
function closeFixed() {
	$('.m-fixed').hide();
}

//关闭下拉
function closeSelect() {
	event.stopPropagation();
	$('.contain-option-box').slideUp();
	$('.contain-select-btn').removeClass('contain-select-btn-active');
}

// toast提示
function showToast(objContent) {
    $(".m-fixed-toast").text(objContent).show().delay(3000).hide(0);
}

//显示弹框
function showFixed(id) {
	$('#'+id).show();
}

// 单选选中
function clickRadio(obj,selector,val) {
	$(selector+" .db-radio-bg").removeClass("db-radio-choose");
	$(obj).children().addClass("db-radio-choose");
	return val;
}

// 点击下拉
function clickSelect(obj) {
	event.stopPropagation();
	$(obj).find('.contain-option-box').slideDown();
	$(obj).find('.contain-select-btn').addClass('contain-select-btn-active');
}

// 点击选项
function clickOption(_this) {
	$(_this).parent().siblings('input').val($(_this).text());
	closeSelect();
	queryData();
}

//主机域名格式约定：xxx{-dev|-test}.xxx
function getProfiles() {
	let profiles = "pro";
	let hostname = window.location.hostname;
	let index = hostname.indexOf("-");
	if (index > 0) {
		profiles = hostname.substring(index + 1, hostname.indexOf("."));
	}
	return profiles;
}

