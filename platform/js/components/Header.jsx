"use strict";

var React = require("react");
var Reflux = require('reflux');
var T = require('react-intl');

var Router = require('react-router');
var Link = Router.Link;

var ReactBootstrap = require('react-bootstrap');
var Navbar = ReactBootstrap.Navbar;
var Nav = ReactBootstrap.Nav;
var NavItem = ReactBootstrap.NavItem;
var DropdownButton = ReactBootstrap.DropdownButton;
var MenuItem = ReactBootstrap.MenuItem;

var ReactRouterBootstrap = require('react-router-bootstrap');

var HttpMixin = require("../mixins/http");
var UserStore = require("../stores/auth");

var Header = React.createClass({
    mixins: [
        Router.Navigation,
        T.IntlMixin,
        Reflux.listenTo(UserStore, 'onSignInSuccess'),
        HttpMixin
    ],
    onSignInSuccess: function (user) {
        this.setState({user: user});
    },
    getInitialState: function () {
        return {
            user: UserStore.currentUser(),
            title: '',
            links: []
        };
    },
    componentDidMount: function () {
        this.get("/base/nav-bar", function (rs) {
            document.title = rs.title;
            if (this.isMounted()) {
                this.setState(rs);
            }
        }.bind(this));
    },
    handlePersonalBar(selectedKey) {
        //alert('selected ' + selectedKey);
        this.transitionTo(selectedKey);
    },
    render: function () {

        var pbt, pbl;
        if (this.state.user.auth) {
            pbt = "Welcome, " + this.state.user.name;
            pbl = [
                {
                    url: "auth.profile",
                    name: "profile"
                },
                {
                    url: "auth.sign-out",
                    name: "Sign out"
                }
            ]
        } else {
            pbt = "Sign In/Up";
            pbl = [
                {
                    url: "auth.sign-in",
                    name: "Sign in"
                },
                {
                    url: "auth.sign-up",
                    name: "Sign up"
                },
                {
                    url: "auth.reset-password.1",
                    name: "Forgot password"
                },
                {
                    url: "auth.confirm",
                    name: "Confirm"
                },
                {
                    url: "auth.unlock",
                    name: "Unlock"
                }
            ]
        }

        return (
            <Navbar brand={<Link to="home"> {this.state.title} </Link>} inverse fixedTop toggleNavKey={0}>
                <Nav right> {}
                    {this.state.links.map(function (object) {
                        return (<NavItem key={"nav-" + object.url} href={object.url}>{object.name}</NavItem>)
                    })}
                    <DropdownButton title={pbt} onSelect={this.handlePersonalBar}>
                        {pbl.map(function (object) {
                            return (<MenuItem eventKey={object.url} key={"nav-" + object.url}>{object.name}</MenuItem>)
                        })}
                    </DropdownButton>
                </Nav>
            </Navbar>
        );
    }
});


module.exports = Header;