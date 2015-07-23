"use strict";

var React = require('react');
var Reflux = require('reflux');
import {Router,Link,Navigation} from 'react-router'

var W = require("./Widgets");
var Utils = require("../Utils");
var SiteStores = require("../stores/Site");

var NoSignInForm = React.createClass({
    mixins: [
        Navigation,
        Reflux.connect(SiteStores.NavBar, 'navBar')
    ],
    onSubmit: function (data) {
        var goto = this.props.goto;
        if (goto) {
            this.transitionTo(goto);
        }
    },
    render: function () {
        var navBar = this.state.navBar;
        return (
            <div className="row">
                <div className="col-md-offset-2 col-md-8">
                    <W.Form source={this.props.source} submit={this.onSubmit}/>

                    <div className="row">
                        <br/>
                        <ul>
                            {navBar.barLinks.map(function (obj) {
                                return (
                                    <li key={"l-" + obj.url}>
                                        <Link to={obj.url}>{obj.name}</Link>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
});


module.exports = {
    SignIn: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/sign_in" goto="home"/>
            );
        }
    }),
    SignUp: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/sign_up" goto="users.sign_in"/>
            );
        }
    }),
    ForgotPassword: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/forgot_password" goto="users.sign_in"/>
            );
        }
    }),
    ChangePassword: React.createClass({
        render: function () {
            var token = Utils.gup("code");
            if (token) {
                return (
                    <NoSignInForm source={"/users/change_password/"+token} goto="users.sign_in"/>
                );
            }
            return (<div/>)
        }
    }),
    Confirm: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/confirm" goto="users.sign_in"/>
            );
        }
    }),
    Unlock: React.createClass({
        render: function () {
            return (<NoSignInForm source="/users/unlock" goto="users.sign_in"/>);
        }
    }),
    Profile: React.createClass({//todo
        render: function () {
            return (
                <div className="row">
                    <h2>Profile</h2>
                </div>
            );
        }
    })
};