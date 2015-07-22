"use strict";

var React = require('react');
import {Router, Route, DefaultRoute} from "react-router";

var Auth = require("./components/Auth");
var Site = require("./components/Site");
var Root = require("./components/Root");
var Message = require("./components/Message");

var Routes = (
    <Route handler={Root}>
        <DefaultRoute name="home" handler={ Site.Home }/>

        <Route name="about_us" path="/about_us" handler={ Site.AboutUs }/>

        <Route name="users.sign_in" path="/users/sign_in" handler={ Auth.SignIn }/>
        <Route name="users.sign_up" path="/users/sign_up" handler={ Auth.SignUp }/>
        <Route name="users.forgot_password" path="/users/forgot_password" handler={ Auth.ForgotPassword }/>
        <Route name="users.change_password" path="/users/change_password" handler={ Auth.ChangePassword }/>
        <Route name="users.confirm" path="/users/confirm" handler={ Auth.Confirm }/>
        <Route name="users.unlock" path="/users/unlock" handler={ Auth.Unlock }/>
        <Route name="users.profile" path="/users/profile" handler={ Auth.Profile }/>
        <Route name="message.show" path="/show" handler={ Message }/>
    </Route>
);

module.exports = Routes;