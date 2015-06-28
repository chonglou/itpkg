require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');

var Root = require("./components/Root");
var Footer = require("./components/Footer");
var L = require("./components/locales");


$(function () {
    var lang;
    console.log(Footer);
    switch (lang) {
        case "zh-CN":
            lang = L.zhCN;
            break;
        default:
            lang = L.enUS;

    }
    React.render(<Root {...lang}/>, $("div#content")[0]);
});



