"use strict";

var React = require('react');
var Router = require('react-router');
var T = require('react-intl');

var ReactRouter = require('react-router');
var Link = ReactRouter.Link;

var ReactBootstrap = require('react-bootstrap');
var Input = ReactBootstrap.Input;
var ButtonInput = ReactBootstrap.ButtonInput;

var W = require("./Widgets");
var HttpMixin = require("../mixins/Http");

var sharedLinks = {
    sharedLinks: function () {
        return [
            {
                url: "auth.sign-in",
                title: this.getIntlMessage("auth.titles.sign_in"),
                name: this.getIntlMessage("auth.links.sign_in")
            },
            {
                url: "auth.sign-up",
                title: this.getIntlMessage("auth.titles.sign_up"),
                name: this.getIntlMessage("auth.links.sign_up")

            },
            {
                url: "auth.forgot-password",
                title: this.getIntlMessage("auth.titles.forgot_password"),
                name: this.getIntlMessage("auth.links.forgot_password")
            },
            {
                url: "auth.confirm",
                title: this.getIntlMessage("auth.titles.confirm"),
                name: this.getIntlMessage("auth.links.confirm")
            },
            {
                url: "auth.unlock",
                title: this.getIntlMessage("auth.titles.unlock"),
                name: this.getIntlMessage("auth.links.unlock")
            }
        ];
    }
};

var NoSignInForm = React.createClass({
    mixins: [
        T.IntlMixin,
        sharedLinks
    ],
    render: function () {
        return (
            <div className="row">
                <div className="col-md-offset-2 col-md-8">
                    <W.Form source={this.props.source}/>

                    <div className="row">
                        <br/>
                        <ul>
                            {this.sharedLinks().map(function (object) {
                                return (<li key={"l-" + object.url}>
                                    <Link to={object.url}>{object.title}</Link>
                                </li>)
                            })}
                        </ul>
                    </div>
                </div>
            </div>
        );
    }
});


module.exports = {
    SharedLinks: sharedLinks,
    SignIn: React.createClass({//todo
        mixins: [
            T.IntlMixin
        ],
        onSubmit: function (data, e) {
            console.log("submit");
            console.log(data);
        },
        render: function () {
            return (
                <NoSignInForm source="/users/sign_in"/>
            );
        }
    }),
    SignUp: React.createClass({
        mixins: [
            Router.Navigation,
            T.IntlMixin,
            HttpMixin
        ],
        onSubmit: function (rs) {
            if (rs.ok) {
                this.transitionTo("auth.sign-in");
            }
        },
        render: function () {
            return (
                <NoSignInForm source="/users/sign_up"/>
            );
        }
    }),
    ForgotPassword: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <NoSignInForm source="/users/forgot_password"/>
            );
        }
    }),
    ChangePassword: React.createClass({//todo
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <NoSignInForm source="/users/change_password"/>
            );
        }
    }),
    Confirm: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <NoSignInForm source="/users/confirm"/>
            );
        }
    }),
    Unlock: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (<NoSignInForm source="/users/unlock"/>);
        }
    }),
    Profile: React.createClass({//todo
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div className="row">
                    <h2>Profile</h2>
                </div>
            );
        }
    })
};