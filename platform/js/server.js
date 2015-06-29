localStorage.debug = true;
require("./mixins/server");
require("./app");


var $ = require("jquery");
$(function () {
    console.log("Development mode");
});