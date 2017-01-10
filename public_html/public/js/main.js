const PAGES = ['dashboard', 'console', 'players', 'file manager', 'backup manager', 'server settings'];

$(function () {
    Console();

    $.ajaxSetup({ cache: false });

    $('li').on('click', 'a', function () {
        reload($(this).parent().index());
    });

    var reload = function (state) {
        $.get('/dashboard/' + PAGES[state] + ".html", function (data) {
            $("#dashboard-section").find(".panel-body").html(data);
        });
    };

    function Console() {

        this.logs = 0;

        this.load = function () {
            var ul = document.getElementById('console-log');
            if (ul !== undefined && ul !== null) {
                $.get("/log", function (data) {
                    var lines = data.split('\n');
                    $.each(lines, function (index, string) {
                        if (string !== "" && index + 2 > this.logs) {
                            this.logs += 1;
                            $(ul).append($("<li>").html(string));
                            console.log(string);
                        }
                    }.bind(this));

                }.bind(this));
            }
        }.bind(this);


        this.interval = setInterval(this.load, 2000);
    }

});
