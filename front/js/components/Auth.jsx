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
                url: "auth.reset-password.1",
                title: this.getIntlMessage("auth.titles.reset_password_1"),
                name: this.getIntlMessage("auth.links.reset_password_1")
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

var NoLoginLinks = React.createClass({
    mixins: [
        T.IntlMixin,
        sharedLinks
    ],
    render: function () {
        return (
            <div className="row">
                <br/>
                <ul>
                {this.sharedLinks().map(function (object) {
                    return (<li key={"l-" + object.url}>
                        <Link to={object.url} >{object.title}</Link>
                    </li>)
                })}
                </ul>
            </div>
        );
    }
});

var EmailForm = React.createClass({
    mixins: [
        Router.Navigation,
        T.IntlMixin
    ],
    onSubmit: function (rs) {
        if (rs.ok) {
            this.transitionTo("auth.sign-in");
        }
    },
    render: function () {//todo
        return (
            <div className="row">
                <div className="col-md-offset-2 col-md-8">
                    <W.Form
                        action={this.props.url}
                        submit={this.onSubmit}
                        title={this.props.title}
                        fields={[
                            {
                                name: "action",
                                type: "hidden",
                                value: this.props.action
                            },
                            {
                                name: "email",
                                type: "email",
                                nil: false
                            }
                        ]}/>
                    <NoLoginLinks />
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
                <div className="row">
                    <div className="col-md-offset-2 col-md-8">
                        <W.Form
                            submit={this.onSubmit}
                            title="auth.titles.sign_in"
                            fields={[
                                {
                                    name: "email",
                                    type: "email",
                                    nil: false
                                },
                                {
                                    name: "password",
                                    type: "password",
                                    nil: false
                                }
                            ]}/>
                        <NoLoginLinks />
                    </div>
                </div>
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
                <div className="row">
                    <div className="col-md-offset-1 col-md-10">
                        <div id="dialog"/>
                        <W.Form
                            action="/users/sign_up"
                            submit={this.onSubmit}
                            title="auth.titles.sign_up"
                            fields={[
                                {
                                    name: "name",
                                    type: "text",
                                    size: 4,
                                    placeholder: "auth.placeholders.username",
                                    label: "auth.fields.username",
                                    nil: false
                                },
                                {
                                    name: "email",
                                    type: "email",
                                    nil: false
                                },
                                {
                                    name: "password",
                                    type: "password",
                                    nil: false
                                },
                                {
                                    name: "re_password",
                                    label: "fields.re_password",
                                    type: "password",
                                    placeholder: "placeholders.re_password",
                                    nil: false
                                }
                            ]}/>
                        <NoLoginLinks />
                    </div>
                </div>
            );
        }
    }),
    ResetPassword1: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (<EmailForm
                url="/users/reset_password/1"
                action="reset-password-1"
                title="auth.links.reset_password_1"/>);
        }
    }),
    ResetPassword2: React.createClass({//todo
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div className="row">
                    <h3>{this.getIntlMessage("auth.titles.reset_password_2")}</h3>
                    <hr/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    Confirm: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (<EmailForm url="/users/confirm"  action="confirm" title="auth.links.confirm"/>);
        }
    }),
    Unlock: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (<EmailForm url="/users/unlock" action="unlock" title="auth.links.unlock"/>);
        }
    }),
    Profile: React.createClass({//todo
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div  className="row">
                    <h2>Profile</h2>
                </div>
            );
        }
    })
};