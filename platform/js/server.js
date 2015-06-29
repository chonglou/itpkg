localStorage.debug = true;

BaseUrlMixin = require("./mixins/jsonp");
BaseUrlMixin.url = function (u) {
    return "http://localhost:3000" + u;
};

require("./app");

var $ = require("jquery");
$(function () {
    console.log("Development mode");
});