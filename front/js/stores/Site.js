"use strict";

var Reflux = require('reflux');
var $ = require("jquery");
var Actions = require('../actions/Site');

var _key = "nav-bar";

var navBarStore = Reflux.createStore({
    listenables: [Actions],
    getInitialState: function () {
        var val = localStorage.getItem(_key);
        if (val) {
            this.navBar = JSON.parse(val);
        } else {
            this.navBar = {
                hotLinks: [],
                barLinks: []
            };
        }
        return this.navBar;
    },
    onFetchNavBar: function () {
        $.get(
            "/nav_bar",
            function (result) {
                localStorage.setItem(_key, JSON.stringify(result));
                this.navBar = result;
                this.trigger(this.navBar);
            }.bind(this),
            "json"
        );
    }
});

module.exports = {
    NavBar: navBarStore
};