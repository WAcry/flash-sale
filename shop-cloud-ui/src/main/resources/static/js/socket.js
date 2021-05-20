function createScoket(uuid) {
    var socket;
    if (typeof (WebSocket) == "undefined") {
        console.log("doesnt' support WebSocket");
    } else {
        socket = new WebSocket("ws://localhost:8095/" + uuid);
        socket.onopen = function () {
            console.log("Socket opened");
        };
        socket.onmessage = function (event) {
            var result = $.parseJSON(event.data);
            if (result.code === "00000") {
                layer.confirm("success get the deal", {btn: ["confirm", "cancel"]},
                    function () {
                        window.location.href = "/order_detail.html?orderNo=" + result.data;
                    },
                    function () {
                        layer.closeAll();
                    });
            } else {
                layer.msg(result.msg);
            }
        };
        socket.onclose = function () {
            console.log("Socket closed");
        };
        socket.onerror = function () {
            console.log("Socket error");
        }
        $(window).unload(function (event) {
            socket.close();
        });
    }
    return socket;
}