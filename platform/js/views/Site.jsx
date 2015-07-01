"use strict";

var React = require('react');

module.exports = {
    AboutUs: React.createClass({
        render: function () {
            return (
                <div className="row">
                    <h2>About Us</h2>
                </div>
            );
        }
    }),
    Home: React.createClass({
        render: function () {
            return (
                <div className="row">
                    <h2>Home</h2>
                </div>
            );
        }
    })
};