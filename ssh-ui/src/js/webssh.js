export default function WSSHClient() {
}

WSSHClient.prototype._generateEndpoint = function () {
    let protocol = ''
    if (window.location.protocol === 'https:') {
        protocol = 'wss://';
    } else {
        protocol = 'ws://';
    }
    return protocol + 'localhost:8000/webssh';
};

WSSHClient.prototype.connect = function (options) {
    var endpoint = this._generateEndpoint();
    // 状态 1：已经链接并且可以通讯；3：连接已关闭或者没有链接成功
    if (this._connection && this._connection.readyState && (this._connection.readyState === 0 || this._connection.readyState === 1)) {
        return;
    }
    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(endpoint);
    } else {
        //否则报错
        options.onError('当前浏览器不支持WebSocket功能，请更新浏览器');
        return;
    }

    this._connection.onopen = function () {
        options.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = evt.data.toString();
        //data = base64.decode(data);
        options.onData(data);
    };

    this._connection.onclose = function (evt) {
        options.onClose();
    };
};

WSSHClient.prototype.send = function (data) {
    // 发送数据
    this._connection.send(JSON.stringify(data));
};

WSSHClient.prototype.sendInitData = function (options) {
    // 发送连接参数
    this._connection.send(JSON.stringify(options));
}

WSSHClient.prototype.sendClientData = function (data) {
    // 状态 1：已经链接并且可以通讯；3：连接已关闭或者没有链接成功
    if (this._connection.readyState === 1) {
        //发送指令
        this._connection.send(JSON.stringify({"operate": "command", "command": data}))
    }

}

WSSHClient.prototype.close = function () {
    // 状态 1：已经链接并且可以通讯；3：连接已关闭或者没有链接成功
    if (this._connection.readyState === 0 || this._connection.readyState === 1) {
        // 1000 正常关闭
        this._connection.close(1000, '正常关闭');
    }
};
