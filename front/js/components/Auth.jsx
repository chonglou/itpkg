"use strict";

var React = require('react');
var T = require('react-intl');
var Reflux = require('reflux');
import {Router,Link,Navigation} from 'react-router'

var W = require("./Widgets");
var Utils = require("../Utils");
var HttpMixin = require("../mixins/Http");
var AuthStore = require("../stores/Auth");
var AuthActions = require("../actions/Auth");

var NoSignInForm = React.createClass({
    mixins: [
        T.IntlMixin,
        HttpMixin
    ],
    getInitialState: function () {
        return {links: []}
    },
    componentDidMount: function () {
        this.get(
            "/users/bar",
            function (result) {
                this.setState({links: result.data.slice(1)});
            }.bind(this));

    },
    render: function () {
        return (
            <div className="row">
                <div className="col-md-offset-2 col-md-8">
                    <W.Form source={this.props.source} submit={this.props.submit}/>

                    <div className="row">
                        <br/>
                        <ul>
                            { this.state.links.map(function (obj) {
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
        mixins: [
            Navigation,
            Reflux.connect(AuthStore, 'ticket')
        ],
        onSubmit: function (data) {
            AuthActions.signIn(data[0]);
            this.transitionTo("home");
        },
        render: function () {
            return (
                <NoSignInForm source="/users/sign_in" submit={this.onSubmit}/>
            );
        }
    }),
    SignUp: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/sign_up"/>
            );
        }
    }),
    ForgotPassword: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/forgot_password"/>
            );
        }
    }),
    ChangePassword: React.createClass({
        render: function () {
            var token = Utils.gup("code");
            if (token) {
                return (
                    <NoSignInForm source={"/users/change_password/"+token}/>
                );
            }
            return (<div/>)
        }
    }),
    Confirm: React.createClass({
        render: function () {
            return (
                <NoSignInForm source="/users/confirm"/>
            );
        }
    }),
    Unlock: React.createClass({
        render: function () {
            return (<NoSignInForm source="/users/unlock"/>);
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