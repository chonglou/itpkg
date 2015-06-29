localStorage.debug = true;

HttpMixin = require("./mixins/jsonp");
HttpMixin.url = function (u) {
    return "http://localhost:3000" + u;
};

require("./app");

var $ = require("jquery");
$(function () {
    console.log("Development mode");
});