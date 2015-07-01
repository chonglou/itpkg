"use strict";

var Reflux = require('reflux');
var jwt_decode = require('jwt-decode');

var Actions = require('../actions/Auth');

var UserStore = Reflux.createStore({
    listenables: Actions,
    init: function () {
        var jwt = localStorage.getItem('jid');
        if (jwt) {
            this.user = jwt_decode(jwt);
        } else {
            this.user = {};
        }
    },

    currentUser: function () {
        return this.user;
    },
    updateJid: function (jid) {
        localStorage.setItem('jid', jid);
        this.user = jwt_decode(jid);
        this.trigger(this.user);
    },
    signOut: function () {
        localStorage.removeItem('jid');
        this.user = {};
        this.trigger(this.user);
    }

});

module.exports = UserStore;