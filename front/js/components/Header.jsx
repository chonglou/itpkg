"use strict";

var React = require("react");
var Reflux = require('reflux');
var T = require('react-intl');

import {Router,Link,Navigation} from 'react-router'
import {ReactBootstrap, Navbar, Nav, NavItem, DropdownButton, MenuItem} from "react-bootstrap"

var ReactRouterBootstrap = require('react-router-bootstrap');

var UserStore = require("../stores/Auth");
var Auth = require("./Auth");
var SiteStore = require("../stores/Site");

var Header = React.createClass({
    mixins: [
        Navigation,
        Reflux.connect(SiteStore.NavBar, 'navBar')
    ],
    render: function () {
        var navBar = this.state.navBar;
        if(navBar){
            return (
                <Navbar brand={<Link to="home"> {navBar.title} </Link>} inverse fixedTop toggleNavKey={0}>
                    <Nav right> {}
                        {navBar.hot.map(function (obj) {
                            return (<NavItem key={"nav-" + obj.url} href={obj.url}>{obj.name}</NavItem>)
                        })}
                        <DropdownButton title={navBar.barName} onSelect={this.transitionTo}>
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