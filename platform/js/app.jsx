require("bootstrap/dist/css/bootstrap.css");
require("../css/base.css");

var $ = require("jquery");
var React = require('react');

var App = require("./components/App");

$(function () {
    React.render(<App />, $("div#content")[0]);
});




