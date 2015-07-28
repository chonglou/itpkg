"use strict";

var $ = require("jquery");

var Http = {
    get: function (url, success, error) {
        if (error == undefined) {
            error = function () {
            };
        }
        $.ajax({
            url: this.url(url),
            success: success.bind(this),
            error: error.bind(this),
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