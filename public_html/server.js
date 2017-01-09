var exec = require('child_process').exec;
var spigot = function(callback) {
    var init = exec('(cd /root/spigot && exec java -jar spigot-1.11.2.jar > log.txt)');
    var list = [];

    init.stdout.setEncoding('utf8');

    spigot.stdout.on('data', function (data) {
        console.log(data + " - Jasper");
    });
}
var http = require('http');


/* minecraft.stdout.on('data', function (data) {
 console.log('stdout: ' + data);
 });

 minecraft.stderr.on('data', function (data) {
 console.log('stderr: ' + data);
 });
 minecraft.on('close', function (code) {
 console.log('closing code: ' + code);
 });*/
