var $ = require("jquery");

var Jsonp = {
    get: function (url, success) {
        $.ajax({
            url: this.url(url),
            success: success,
            jsonpCallback: "cb",
            type: "GET",
            dataType: "jsonp"
        });
    },
    post: function () {

    },
    url: function (u) {
        return u;
    }
};

module.exports = Jsonp;