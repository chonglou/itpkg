"use strict";

var Reflux = require('reflux');

var Actions = Reflux.createActions([
    'signIn',
    'signOut'
]);

module.exports = Actions;