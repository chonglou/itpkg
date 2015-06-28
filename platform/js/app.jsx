require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var zhCN = require("./locales/zh-CN");
var enUS = require("./locales/en-US");


var $ = require("jquery");
var React = require('react');

var Root = require("./components/Root");

$(function () {
    React.render(<Root {...enUS}/>, $("div#content")[0]);
});




