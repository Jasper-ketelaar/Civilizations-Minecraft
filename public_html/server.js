(function () {
    var fs = require('fs');
    var cp = require('child_process');
    var http = require('http');
    var express = require('express');
    var nocache = require('connect-nocache');
    var readline = require('readline');

    const WORKING_DIRECTORY_STRING = '/root/spigot/';
    const WORKING_DIRECTORY = {cwd: WORKING_DIRECTORY_STRING};
    const SERVER_JAR = 'spigot-1.11.2.jar';
    const LOG_FILE = 'log.txt';
    const JAVA = 'java';
    const ARGS = ['-Xmx8G', '-Xms1024M', '-jar', SERVER_JAR, 'nogui'];
    const FORCE_KILL = 'SIGINT';

    function Server() {
        this.handler = new DataHandler(this);
        this.load();
    }

    Server.prototype.load = function () {
        var _this = this;

        this.running = true;
        this.process = cp.spawn(JAVA, ARGS, WORKING_DIRECTORY);

        this.exitListener = this.process.on('exit', function (data) {
            this.exit(data);
        }.bind(this));

        this.input = this.process.stdin;
        this.output = this.process.stdout;
        this.error = this.process.stderr;

        this.input.setEncoding('utf8');
        this.output.setEncoding('utf8');
        this.error.setEncoding('utf8');

        if (this.handler !== undefined) {
            this.output.on('data', function (data) {
                _this.handler.output(data);
            });

            this.error.on('data', function (data) {
                _this.handler.error(data);
            });
        }
    }

    Server.prototype.exit = function (data) {
        console.log(data);
        if (this.timeout !== undefined)
            clearTimeout(this.timeout);
        if (this.stop_callback !== undefined && this.stop_callback !== null) {
            this.stop_callback("unforced");
            this.stop_callback = null;
        }
    }

    Server.prototype.stop = function (callback) {
        this.input.write("stop \n");
        this.timeout = setTimeout(function () {
            this.process.kill(FORCE_KILL);
            callback("forced");
        }.bind(this), 5000);
        this.stop_callback = callback;
    }

    Server.prototype.restart = function () {
        this.stop(function () {
            this.load();
        }.bind(this));
    }

    Server.prototype.command = function (command) {
        if (command === "restart") {
            this.restart();
        } else if (command === "stop") {
            this.stop();
        } else {
            this.input.write(command + "\n");
        }
    }


    function DataHandler(server) {
        this.output = function (data) {
            console.log(data.trim());
        }

        this.error = function (data) {
            console.log(data.trim());
        }

        process.openStdin().addListener('data', function (data) {
            var command = data.toString().trim();
            server.command(command);
        });
    }

    function WebServer(port) {
        this.app = express();
        this.app.use(noCache);
        this.app.use(express.static(__dirname + '/public'));
        http.createServer(app).listen(port);
    }

    String.prototype.contains = function (str) {
        return this.indexOf(str) !== -1;
    }

    function main() {
        this.server = new Server();
    }

    main();

})();





