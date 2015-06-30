"use strict";

require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');
var Router = require("react-router");

var Routes = require("./Routes");

var zhCN = require("./i18n/zh-CN");
var enUS = require("./i18n/en-US");

$(function () {
    var lang;
    switch (localStorage.locale) {
        case "zh-CN":
            lang = zhCN;
            document.documentElement.lang = "zh";
            break;
        case "en-US":
            lang = enUS;
            document.documentElement.lang = "en";
            break;
        default:
            lang = enUS;
            localStorage.locale = "en-US";

    }

    Router.run(Routes, function (Handler) {
        React.render(<Handler {...lang}/>,
            document.getElementById('content'));
    });

});





