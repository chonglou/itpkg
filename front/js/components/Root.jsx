"use strict";

var React = require("react");
var T = require('react-intl');
var Router = require("react-router");
var RouteHandler = Router.RouteHandler;

var Header = require("./Header");
var Footer = require("./Footer");


var navPadStyle = {
    height: "70px"
};

var Root = React.createClass({
    mixins: [
        Router.Navigation,
        T.IntlMixin
    ],
    render: function () {
        return (
            <div>
                <Header/>

                <div style={navPadStyle}/>
                <div className="container">
                    <RouteHandler />
                    <Footer />
                </div>
            </div>
        );
    }
});

module.exports = Root;