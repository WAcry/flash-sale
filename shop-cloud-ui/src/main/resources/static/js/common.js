function showLoading() {
    var idx = layer.msg('processing...', {
        icon: 16,
        shade: [0.5, '#f5f5f5'],
        scrollbar: false,
        offset: '0px',
        time: 100000
    });
    return idx;
}

//salt
var passsword_salt = "1a2b3c4d";

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

function getUuid(userId) {
    var date = new Date();
    var uuid = "" + userId + date.getDay() + date.getHours() + date.getMinutes() + date.getSeconds() + date.getMilliseconds();
    return uuid;
}
