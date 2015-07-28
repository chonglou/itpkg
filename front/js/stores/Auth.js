"use strict";

var Reflux = require('reflux');
var $ = require("jquery");

var Actions = require('../actions/Auth');

var _key = "ticket";

var userStore = Reflux.createStore({
    listenables: Actions,
    getInitialState: function () {
        var val = sessionStorage.getItem(_key);
        if (val) {
            this.ticket = val;
        }
        return this.ticket;
    },
    onSignIn: function (ticket) {
        sessionStorage.setItem(_key, ticket);
        this.ticket = ticket;
        this.trigger(this.ticket);
    },
    onSignOut: function () {
        $.get(
            "/users/sign_out",
            function () {
                sessionStorage.removeItem(_key);
                this.ticket = undefined;
                this.trigger(this.ticket);
            }.bind(this),
            "json"
        );
    }
});

module.exports = userStore;