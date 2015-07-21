"use strict";

var $ = require("jquery");
var Reflux = require('reflux');

var Actions = Reflux.createActions({
    signIn: {},
    signUp: {},
    signOut: {},
    resetPassword: {},
    confirm: {},
    unlock: {}
});

Actions.signIn.listen(function (data) {
    console.log(data);
});

module.exports = Actions;