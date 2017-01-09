var exec = require('child_process').exec;
var mc = exec('(cd /root/spigot && exec java -jar spigot-1.11.2.jar > log.txt)',
    function (error, stdout, stderr) {
        console.log('stdout: ' + stdout);
        console.log('stderr: ' + stderr);
        if (error !== null) {
            console.log('exec error: ' + error);
        }
    }
);

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
