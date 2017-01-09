var fs = require('fs');
var exec = require('child_process').exec;

var spigot = exec('(cd /root/spigot && exec java -jar spigot-1.11.2.jar > log.txt)');

var file = "../spigot/log.txt";
fs.watch(file, function (change) {
    fs.readFile(file, function (err, data) {
        if (!err)
            console.log(data.toString('ascii'));
    })
});

var http = require('http');
var express = require('express');
var app = express();

app.use(express.static(__dirname + '/public'));
http.createServer(app).listen(3000);

