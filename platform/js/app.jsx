require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');

var Body = require("./components/Root");
var L = require("./components/locales");

var Root = React.render(<Body {...L.enUS}/>, document.getElementById("content"));


//$(function () {
//    React.render(<Root {...L.enUS}/>, $("div#content")[0]);
//});
module.exports = {Root: Root};



