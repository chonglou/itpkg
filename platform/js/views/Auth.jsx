"use strict";

var React = require('react');
var T = require('react-intl');

var ReactRouter = require('react-router');
var Link = ReactRouter.Link;

var ReactBootstrap = require('react-bootstrap');
var Input = ReactBootstrap.Input;

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
            <ul>
                {this.sharedLinks().map(function (object) {
                    return (<li key={"l-" + object.url}>
                        <Link to={object.url} >{object.title}</Link>
                    </li>)
                })}
            </ul>
        );
    }
});

var EmailForm = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        return (
            <form>
                <Input type="hidden" value={this.props.action}/>
                <Input type="email" label={this.getIntlMessage("forms.email")} placeholder="your_name@gmail.com"/>
            </form>
        );
    }
});

module.exports = {
    SharedLinks: sharedLinks,
    SignIn: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div>
                    <h3>{this.getIntlMessage("auth.titles.sign_in")}</h3>
                    <hr/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    SignUp: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div>
                    <h3>{this.getIntlMessage("auth.titles.sign_up")}</h3>
                    <hr/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    ResetPassword1: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div>
                    <h3>{this.getIntlMessage("auth.titles.reset_password_1")}</h3>
                    <hr/>
                    <EmailForm action="reset-password"/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    ResetPassword2: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div>
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
            return (
                <div>
                    <h3>{this.getIntlMessage("auth.titles.confirm")}</h3>
                    <hr/>
                    <EmailForm action="confirm"/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    Unlock: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <div>
                    <h3>{this.getIntlMessage("auth.titles.unlock")}</h3>
                    <hr/>
                    <EmailForm action="unlock"/>
                    <NoLoginLinks />
                </div>
            );
        }
    }),
    Profile: React.createClass({
        mixins: [
            T.IntlMixin
        ],
        render: function () {
            return (
                <h2>Unlock</h2>
            );
        }
    })
};