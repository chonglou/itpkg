"use strict";

var React = require("react");
var T = require('react-intl');
import {Router,Link} from 'react-router'
import {ReactBootstrap, Alert, Button} from "react-bootstrap"


var Console = React.createClass({
    mixins: [T.IntlMixin],
    render: function () {
        if (this.state.show) {
            return (
                <Alert bsStyle={this.state.style}>
                    <strong>
                        {this.state.ok ? this.getIntlMessage('labels.success') : this.getIntlMessage('labels.failed')}
                    </strong>

                    <p>
                        <ul>
                            {this.state.data.map(function (obj, i) {
                                return (<li key={"i-"+i}>{obj}</li>)
                            })}
                        </ul>
                    </p>
                </Alert>
            )
        }
        return (<div />);

    }
});


module.exports = Console;