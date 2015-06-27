var $ = require("jquery");
var React = require('react');

var PersonalBar = require("./base/PersonalBar");

$(function () {
    React.render(<PersonalBar />, $("div#content")[0]);
});




