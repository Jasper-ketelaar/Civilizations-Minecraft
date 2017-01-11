"use strict";

let reload = require('require-reload');
let Minecraft = require("./lib/mcprocess").Minecraft;
let ControlPanel = require("./lib/controlpanel").ControlPanel;

String.prototype.contains = function (str) {
    return this.indexOf(str) !== -1;
};

String.prototype.startsWith = function (str) {
    if (str.length > this.length) {
        return false;
    }

    if (str.length === this.length) {
        return str === this;
    }

    Array.from(str).forEach(function (char, index) {
        if (char !== str.charAt(index)) {
            return false;
        }
    });
    return true;
};

let main = function () {
    let mcserver = new Minecraft();
    let cp = new ControlPanel();
    cp.listen();
    mcserver.load();

    process.stdin.on('data', (data) => {
        let trimmed = data.toString("utf8").trim();
        if (trimmed.contains("webreload")) {
            console.log("Stopping...");
            cp.stop(() => {
                console.log("Stopped");
                ControlPanel = reload("./lib/controlpanel.js").ControlPanel;
                cp = new ControlPanel();
                cp.listen();
            });
        } else {
            mcserver.command(trimmed);
        }
    });

};

main();






