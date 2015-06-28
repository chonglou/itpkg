var React = require("react");
var T = require('react-intl');

var Header = require("./Header");
var Footer = require("./Footer");

var navPadStyle = {
    height: "50px"
};


var Root = React.createClass({
    mixins: [T.IntlMixin],
    render: function () {
        return (
            <div>
                <Header />
                <div style={navPadStyle} />
                <div className="container">
                    <h1>TEST111</h1>

                    <Footer />
                </div>
            </div>
        );
    }
});

module.exports = Root;