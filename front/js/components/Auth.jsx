"use strict";

var React = require('react');
var Reflux = require('reflux');
import {Router,Link,Navigation} from 'react-router'

var W = require("./Widgets");
var SiteStores = require("../stores/Site");

var NoSignInForm = React.createClass({
    mixins: [
        Navigation,
        Reflux.connect(SiteStores.NavBar, 'navBar')
    ],
    render: function () {
        var navBar = this.state.navBar;
        return (
            <div className="row">
                <div className="col-md-offset-2 col-md-8">
                    <W.Form source={this.props.source}/>

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
    SignIn: React.createClass({//todo
        render: function () {
            return (
                <NoSignInForm source="/users/sign_in"/>
            );
        }
    }),
    SignUp: React.createClass({ //todo
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
    ChangePassword: React.createClass({//todo
        render: function () {
            return (
                <NoSignInForm source="/users/change_password"/>
            );
        }
    }),
    Confirm: React.createClass({//todo
        render: function () {
            return (
                <NoSignInForm source="/users/confirm"/>
            );
        }
    }),
    Unlock: React.createClass({//todo
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