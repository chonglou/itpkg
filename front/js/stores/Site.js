"use strict";

var Reflux = require('reflux');
var $ = require("jquery");
var Actions = require('../actions/Site');

var navBarStore = Reflux.createStore({
    listenables: [Actions],
    navBar: {},
    init: function () {
        this.fetchNavBar();
    },
    fetchNavBar: function () {
        $.ajax({
            url: "/nav_bar",
            context: this,
            success: function (result) {
                this.navBar = result;
                this.trigger(this.navBar);
            },
            dataType: "json"
        });
    }
});

module.exports = {
    NavBar: navBarStore
};