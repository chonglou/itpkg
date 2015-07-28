"use strict";

require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');
var Router = require("react-router");
var cookie = require("js-cookie");

var Routes = require("./Routes");

var zhCN = require("./i18n/zh_CN");
var enUS = require("./i18n/en_US");

$(function () {

    $(document).ajaxSend(function (e, xhr, options) {
        xhr.setRequestHeader('Authorization', 'Bearer ' + sessionStorage.getItem("ticket"));
    });

    var lang;

    switch (cookie.get("LANG")) {
        case "zh_CN":
            lang = zhCN;
            document.documentElement.lang = "zh";
            break;
        case "en_US":
            lang = enUS;
            document.documentElement.lang = "en";
            break;
        default:
            lang = enUS;
            cookie.set("LANG", "en_US", {expires: 7});
    }

    Router.run(Routes, function (Handler) {
        React.render(<Handler {...lang}/>,
            document.getElementById('content'));
    });

    console.log("init.");
});





