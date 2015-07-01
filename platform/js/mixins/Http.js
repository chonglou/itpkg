"use strict";

var $ = require("jquery");

$.ajaxSetup({
    beforeSend: function (xhr) {
        xhr.setRequestHeader("Accept-Language", localStorage.locale);
        xhr.setRequestHeader("Authorization", "Bearer " + localStorage.jid);
    }
});

var Http = {
    get: function (url, success) {
        $.ajax({
            url: this.url(url),
            success: success.bind(this),
            type: "GET",
            dataType: "json"
        });
    },
    post: function (url, data, success) {
        $.ajax({
            url: this.url(url),
            success: success.bind(this),
            data: data,
            type: "POST",
            dataType: "json"
        });
    },
    url: function (u) {
        return u;
    }
};

module.exports = Http;