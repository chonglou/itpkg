"use strict";

var React = require('react');
var Router = require("react-router");
var Route = Router.Route;
var DefaultRoute = Router.DefaultRoute;

var Auth = require("./components/Auth");
var Site = require("./components/Site");
var Root = require("./components/Root");

var Routes = (
    <Route handler={Root}>
        <DefaultRoute name="home" handler={ Site.Home }/>
        <Route name="about-us" path="/about-us" handler={ Site.AboutUs }/>

        <Route name="auth.sign-in" path="/users/sign-in" handler={ Auth.SignIn }/>
        <Route name="auth.sign-up" path="/users/sign-up" handler={ Auth.SignUp }/>
        <Route name="auth.reset-password.1" path="/users/reset-password/1" handler={ Auth.ResetPassword1 }/>
        <Route name="auth.reset-password.2" path="/users/reset-password/2" handler={ Auth.ResetPassword2 }/>
        <Route name="auth.confirm" path="/users/confirm" handler={ Auth.Confirm }/>
        <Route name="auth.unlock" path="/users/unlock" handler={ Auth.Unlock }/>
        <Route name="auth.profile" path="/users/profile" handler={ Auth.Profile }/>

    </Route>
);

module.exports = Routes;