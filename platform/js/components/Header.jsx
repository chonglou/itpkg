"use strict";

var React = require("react");
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

var Header = React.createClass({
    mixins: [T.IntlMixin, HttpMixin],
    getInitialState: function () {
        return {
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
        var links = this.state.links.map(function (object) {
            return (<NavItem key={"nav-" + object.url} href={object.url}>{object.name}</NavItem>)
        });
        return (
            <Navbar brand={<Link to="home"> {this.state.title} </Link>} inverse fixedTop toggleNavKey={0}>
                <Nav right> {}
                    {links}
                    <DropdownButton title='Dropdown'>
                        <MenuItem>Action</MenuItem>
                        <MenuItem>Another action</MenuItem>
                        <MenuItem>Something else here</MenuItem>
                        <MenuItem divider />
                        <MenuItem>Separated link</MenuItem>
                    </DropdownButton>
                </Nav>
            </Navbar>
        );
    }
});


module.exports = Header;