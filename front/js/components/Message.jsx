"use strict";

var React = require("react");
var Base64 = require('js-base64').Base64;
var T = require('react-intl');
import {Router,Link} from 'react-router'
import {ReactBootstrap, Alert, Button} from "react-bootstrap"

var Utils = require("../Utils");


var Message = React.createClass({
    mixins: [T.IntlMixin],
    render: function () {
        var msg = JSON.parse(Base64.decode(Utils.gup("msg")));
        if (!msg) {
            msg = {
                style: "danger",
                subject: "ttt",
                body: "bbb"
            }
        }
        var lnk = (<span/>);
        if (msg.link) {
            lnk = (
                <Link to={msg.link.url} className={"btn btn-"+msg.style}>
                    {msg.link.name}
                </Link>
            )
        }
        return (
            <Alert bsStyle={msg.style}>
                <strong>{msg.subject}:</strong>

                <p>{msg.body}</p>

                <p>
                    {lnk}
                    &nbsp;
                    <Link to="home" className="btn btn-default">{this.getIntlMessage('links.back_to_home')}</Link>
                </p>
            </Alert>
        )
    }
});


module.exports = Message;