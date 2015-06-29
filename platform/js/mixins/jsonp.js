var $ = require("jquery");

var Jsonp = {
    get: function (url, success) {
        $.ajax({
            url: this.url(url),
            success: success,
            data: {locale: this.locale()},
            type: "GET",
            dataType: "jsonp"
        });
    },
    post: function (url, data, success) {
        data.locale = this.locale();
        $.ajax({
            url: this.url(url),
            success: success,
            data: data,
            type: "POST",
            dataType: "jsonp"
        });
    },
    locale: function () {
        return localStorage.locale;
    },
    url: function (u) {
        return u;
    }
};

module.exports = Jsonp;