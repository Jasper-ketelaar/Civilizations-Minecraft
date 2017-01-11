/**
 * Created by Jasper on 1/11/2017.
 */
"use strict";

let http = require("http");
let express = require('express');
let fs = require("fs");
let constants = require("./../data/constants");
let app = express();
app.use(express.static(__dirname + "/../public"));

app.get('/log', function (req, res) {
    let log = fs.readFileSync('../spigot/logs/latest.log', 'utf8');
    res.end(log);
});

app.get('/log2', function (req, res) {
    res.end("log");
});

function ControlPanel () {
    this.server = http.createServer(app);
    this.listen();

    this.socketManager = new SocketManager(this.server);
}

ControlPanel.prototype.listen = function () {
    this.server.listen(constants.PORT);
};

ControlPanel.prototype.stop = function (cb) {
    this.server.close(cb);
    this.socketManager.disconnect();
};

ControlPanel.prototype.reload = function () {
    if (this.server.listening) {
        this.stop(() => {
            this.server.listen(constants.PORT);
        });
    } else {
        this.server.listen(constants.PORT);
    }
};

function SocketManager (server) {
    this.server = server;
    this.sockets = {};

    let counter = 0;
    this.server.on("connection", function (socket) {
        let index = counter++;
        console.log('socket', index, 'connected');
        this.sockets[index] = socket;
        socket.on('close', function () {
            delete this.sockets[index];
            console.log('socket', index, 'closed');
        }.bind(this));
    }.bind(this));
}

SocketManager.prototype.disconnect = function () {
    Object.keys(this.sockets).forEach((key) => {
        this.sockets[key].destroy();
    });
};

exports.ControlPanel = ControlPanel;