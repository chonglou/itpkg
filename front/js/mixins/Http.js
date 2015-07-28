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
    post: function (url, data, success, error) {
        if (error == undefined) {
            error = function () {
            };
        }
        $.ajax({
            url: this.url(url),
            success: success.bind(this),
            error: error.bind(this),
            data: data,
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8"
        });
    },
    url: function (u) {
        return u;
    }
};

module.exports = Http;