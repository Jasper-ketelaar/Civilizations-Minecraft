(function () {
    var http = require('http');
    var express = require('express');
    var fs = require('fs');
    var app = express();

    const PORT = 3000;
    http.createServer(app).listen(PORT);

    app.use(express.static(__dirname + "/public"));

    app.get('/log', function (req, res) {
        var log = fs.readFileSync('../spigot/logs/latest.log', 'utf8');
        res.end(log);
    });
})();