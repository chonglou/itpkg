"use strict";

var React = require("react");
var Reflux = require('reflux');
var T = require('react-intl');

import {Router,Link,Navigation} from 'react-router'
import {ReactBootstrap, Navbar, Nav, NavItem, DropdownButton, MenuItem} from "react-bootstrap"

var Auth = require("./Auth");
var SiteStore = require("../stores/Site");

var Header = React.createClass({
    mixins: [
        Navigation,
        Reflux.connect(SiteStore.NavBar, 'navBar')
    ],
    render: function () {
        var navBar = this.state.navBar;
        if (navBar) {
            return (
                <Navbar brand={<Link to="home"> {navBar.title} </Link>} inverse fixedTop toggleNavKey={0}>
                    <Nav right onSelect={this.transitionTo}> {}
                        {navBar.hot.map(function (obj) {
                            return (<NavItem key={"nav-" + obj.url} eventKey={obj.url}>{obj.name}</NavItem>)
                        })}
                        <DropdownButton title={navBar.barName}>
                            {navBar.barLinks.map(function (obj) {
                                return (<MenuItem eventKey={obj.url} key={"nav-" + obj.url}>{obj.name}</MenuItem>)
                            })}
                        </DropdownButton>
                    </Nav>
                </Navbar>
            );
        }
        return (<div/>)
    }
});


module.exports = Header;