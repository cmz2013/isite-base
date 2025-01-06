let authorization = {
    dev: "Basic ZGF0YS5mcm9udDpkYXRhLmZyb250",
    test: "Basic ZGF0YS5mcm9udDpkYXRhLmZyb250",
    pro: "Basic ZGF0YS5mcm9udDpkYXRhLmZyb250"
};

function showLoginBox() {
    $('#loginBox').fadeIn(1700);
}

function getAccessToken() {
    let username = $('#username').val();
    if (!username) {
        showToast('请输入用户名');
        return
    }
    let userPsw = $('#userPsw').val();
    if (!userPsw) {
        showToast('请输入密码');
        return
    }
    $.ajax({
        type: "POST",
        headers: {
            "Authorization": authorization[getProfiles()]
        },
        url: "/oauth/token",
        data: {
            "grant_type": "password",
            "username": username,
            "password": userPsw
        },
        success: function(data) {
            localStorage.setItem("token", data.accessToken);
            getUserResources();
        }
    });
}

function getUserResources() {
    $.ajax({
        type: "GET",
        url: "/oauth/resources",
        success: function(data){
            //localStorage只能存储字符串类型的对象
            localStorage.setItem("userResources", JSON.stringify(data));
            let resources = data.resources;
            if (resources && resources.length > 0) {
                window.location.href = resources[0].children[0].href;
            }
        }
    });
}