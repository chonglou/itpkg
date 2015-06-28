require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');

var Body = require("./components/Root");
var L = require("./components/locales");
var U = require("./components/utils");


$(function () {
    var lang;
    switch (U.getParameterByName("locale")) {
        case "zh-CN":
            lang = L.zhCN;
            break;
        default:
            lang = L.enUS;

    }
    React.render(<Body {...lang}/>, $("div#content")[0]);
});



