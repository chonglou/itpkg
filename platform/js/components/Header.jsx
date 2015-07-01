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

var HttpMixin = require("../mixins/Http");
var UserStore = require("../stores/Auth");
var Auth = require("./Auth");

var Header = React.createClass({
    mixins: [
        Router.Navigation,
        T.IntlMixin,
        Reflux.listenTo(UserStore, 'onSignInSuccess'),
        HttpMixin,
        Auth.SharedLinks
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
    render: function () {

        var pbt, pbl;
        if (this.state.user.auth) {
            pbt = this.getIntlMessage("auth.links.welcome", this.state.user.name);
            pbl = [
                {
                    url: "auth.profile",
                    name: "auth.links.profile"
                },
                {
                    url: "auth.sign-out",
                    name: "auth.links.sign_out"
                }
            ]
        } else {
            pbt = this.getIntlMessage("auth.links.sign_in_or_up");
            pbl = this.sharedLinks();
        }


        return (
            <Navbar brand={<Link to="home"> {this.state.title} </Link>} inverse fixedTop toggleNavKey={0}>
                <Nav right> {}
                    {this.state.links.map(function (object) {
                        return (<NavItem key={"nav-" + object.url} href={object.url}>{object.name}</NavItem>)
                    })}
                    <DropdownButton title={pbt} onSelect={this.transitionTo}>
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