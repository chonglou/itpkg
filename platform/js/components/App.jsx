var React = require("react");
var Header = require("./Header");
var Footer = require("./Footer");

var App = React.createClass({
    render: function () {
        return (
            <div>
                <Header />
                <Footer />
            </div>
        );
    }
});

module.exports = App;