var fs = require('fs');
var cp = require('child_process');
var http = require('http');
var express = require('express');
var nocache = require('connect-nocache');
var readline = require('readline');

const WORKING_DIRECTORY = '/root/spigot/'
const SERVER_JAR = 'spigot-1.11.2.jar';
const LOG_FILE = 'log.txt';

main();

function main() {
    var server = new Server();
}

function Server(handler) {
    this.handler = new DataHandler(this);
    this.running = true;
    this.process = cp.spawn('java', ['-Xmx8G', '-Xms1024M', '-jar', SERVER_JAR, 'nogui'], {
        cwd: WORKING_DIRECTORY
    });

    this.input = this.process.stdin;
    this.output = this.process.stdout;
    this.error = this.process.stderr;

    this.input.setEncoding('utf8');
    this.output.setEncoding('utf8');
    this.error.setEncoding('utf8');

    if (handler !== undefined) {
        this.output.on('data', function (data) {
            handler.output(data);
        });

        this.error.on('data', function (data) {
            handler.error(data);
        });
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
        server.input.write(command + '\n');
    });
}

function WebServer(port) {
    this.app = express();
    this.app.use(noCache);
    this.app.use(express.static(__dirname + '/public'));
    http.createServer(app).listen(port);
}

fs.watch(WORKING_DIRECTORY + LOG_FILE, function (change) {
    fs.readFile(file, function (err, data) {
        if (!err)
            console.log(data.toString('ascii'));
    })
});


