/**
 * Created by Jasper on 1/11/2017.
 */
"use strict";

function define (name, value) {
    Object.defineProperty(exports, name, {
        value: value,
        enumerable: true
    });
};

define("WORKING_DIRECTORY_STRING", "/root/spigot/");
define("SPAWN_ARGUMENTS", {
    cwd: exports.WORKING_DIRECTORY_STRING
});
define("SERVER_JAR", "spigot-1.11.2.jar");
define("JAVA", "java");
define("ARGS", ["-Xmx8G", '-Xms1024M', '-jar', exports.SERVER_JAR, 'nogui']);
define("FORCE_KILL", "SIGINT");
define("PORT", 3000);