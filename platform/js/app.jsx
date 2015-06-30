"use strict";

require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");

var React = require('react');

var Root = require("./components/Root");
var Footer = require("./components/Footer");
var L = require("./components/locales");

localStorage.jid = "";

$(function () {
    var lang;
    switch (localStorage.locale) {
        case "zh-CN":
            lang = L.zhCN;
            document.documentElement.lang = "zh";
            break;
        case "en-US":
            lang = L.enUS;
            document.documentElement.lang = "en";
            break;
        default:
            lang = L.enUS;
            localStorage.locale = "en-US";

    }
    React.render(<Root {...lang}/>, $("div#content")[0]);
});



