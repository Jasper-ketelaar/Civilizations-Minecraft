"use strict";

let constants = require("./../data/constants");
let cp = require('child_process');

function Minecraft() {
    this.running = false;
}

Minecraft.prototype.load = function () {
    this.running = true;
    this.process = cp.spawn(constants.JAVA, constants.ARGS, constants.SPAWN_ARGUMENTS);
    this.input = this.process.stdin;
    this.input.setEncoding("utf8");
    this.process.unref();
    this.exitListener = this.process.on("exit", function (data) {
        this.exit(data);
        this.running = false;
    }.bind(this));
};

Minecraft.prototype.exit = function (data) {
    if (this.timeout !== undefined)
        clearTimeout(this.timeout);
    if (this.stop_callback !== undefined && this.stop_callback !== null) {
        this.stop_callback("unforced");
        this.stop_callback = null;
    }
};

Minecraft.prototype.stop = function (callback) {
    this.input.write("stop \n");
    this.timeout = setTimeout(function () {
        this.process.removeListener(this.exitListener);
        this.process.kill(constants.FORCE_KILL);
        callback("forced");
    }.bind(this), 5000);
    this.stop_callback = callback;
};

Minecraft.prototype.restart = function () {
    this.stop(function () {
        this.load();
    }.bind(this));
};

Minecraft.prototype.command = function (command) {
    if (command.startsWith("/"))
        command = command.replace("/", "");
    if (command === "restart") {
        this.restart();
    } else if (command === "stop") {
        this.stop();
    } else if (command === "start" || command === "load") {
        this.load();
    } else {
        this.input.write(command + "\n");
    }
};

exports.Minecraft = Minecraft;