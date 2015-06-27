var React = require("react");
var ReactIntl = require('react-intl');


var Header = require("./Header");
var Footer = require("./Footer");

var navPadStyle = {
    height: "50px"
};

var containerClassName = "container";

var App = React.createClass({
    mixins: [ReactIntl.IntlMixin],
    render: function () {
        return (
            <div>
                <Header />
                <div style={navPadStyle} />
                <div className={containerClassName}>
                    <h1>TEST111</h1>
                    <Footer />
                </div>
            </div>
        );
    }
});

module.exports = App;