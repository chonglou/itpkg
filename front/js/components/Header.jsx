"use strict";

var React = require("react");
var Reflux = require('reflux');
var T = require('react-intl');
var HttpMixin = require("../mixins/Http");

import {Router,Link,Navigation} from 'react-router'
import {ReactBootstrap, Navbar, Nav, NavItem, DropdownButton, MenuItem} from "react-bootstrap"

var Auth = require("./Auth");

var Header = React.createClass({
    mixins: [
        Navigation,
        HttpMixin
    ],
    getInitialState: function () {
        return {
            navLinks: [],
            barLinks: []
        };
    },
    componentDidMount: function () {
        this.get("/users/nav", function (rs) {
            this.setState({
                navTitle: rs.data[0],
                navLinks: rs.data.slice(1)
            });
        }.bind(this));
        this.get("/users/bar", function (rs) {
            this.setState({
                barTitle: rs.data[0],
                barLinks: rs.data.slice(1)
            });
        }.bind(this));
    },
    render: function () {
        var navBar = this.state.navBar;
        return (
            <Navbar brand={<Link to="home"> {this.state.navTitle} </Link>} inverse fixedTop toggleNavKey={0}>
                <Nav right onSelect={this.transitionTo}> {}
                    {this.state.navLinks.map(function (obj) {
                        return (<NavItem key={"nav-" + obj.url} eventKey={obj.url}>{obj.name}</NavItem>)
                    })}
                    <DropdownButton title={this.state.barTitle}>
                        {this.state.barLinks.map(function (obj) {
                            return (<MenuItem eventKey={obj.url} key={"nav-" + obj.url}>{obj.name}</MenuItem>)
                        })}
                    </DropdownButton>
                </Nav>
            </Navbar>
        );

    }
});

module.exports = Header;