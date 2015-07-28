"use strict";

var React = require("react");
var T = require('react-intl');
import {ReactBootstrap, Input, Button, ButtonToolbar, Alert} from "react-bootstrap"
var HttpMixin = require("../mixins/Http");

var AjaxForm = React.createClass({
    mixins: [T.IntlMixin, HttpMixin],
    getInitialState: function () {
        return {form: {fields: [], buttons: []}, data: {}}
    },
    componentDidMount: function () {
        this.get(
            this.props.source,
            function (result) {
                if (this.isMounted()) {
                    this.setState({form: result});
                }
            },
            function (httpObj) {
                this.setState({message: {style: 'danger', items: [httpObj.statusText]}});
            }
        );

    },
    handleSubmit: function (e) {
        e.preventDefault();
        var data = this.state.data;
        var fm = this.state.form;
        fm.fields.map(function (obj) {
            switch (obj.type) {
                case "hidden":
                    data[obj.id] = obj.value;
                    break;
            }
        });
        switch (fm.method) {
            case "POST":
                this.post(
                    fm.action,
                    JSON.stringify(data),
                    function (result) {
                        if (result.ok) {
                            var clk = this.props.submit;
                            if (clk) {
                                clk(result.data);
                            } else {
                                this.setState({
                                    message: {
                                        title: this.getIntlMessage('labels.success'),
                                        style: "success",
                                        items: result.data
                                    }
                                });
                            }
                        } else {
                            this.setState({
                                message: {
                                    title: this.getIntlMessage('labels.failed'),
                                    style: "danger",
                                    items: result.errors
                                }
                            });
                        }
                    },
                    function (httpObj) {
                        this.setState({message: {style: 'danger', items: [httpObj.statusText]}});
                    }
                );
                break;
            default:
                console.log("unsupported http method " + fm.method);
        }
    },
    handleChange: function (e) {
        var item = e.target;
        var data = this.state.data;
        data[item.id] = item.value;
        this.setState({data: data});
    },
    render: function () {
        var fm = this.state.form;
        var fields = fm.fields.map(function (obj) {
            var key = fm.id + "-" + obj.id;
            var lblCss = "col-sm-3";
            if (obj.required) {
                lblCss += " required";
            }
            var fldCss = "col-sm-" + obj.size;
            switch (obj.type) {
                case "hidden":
                    return (<input value={obj.value} key={key} type="hidden"/>);
                case "text":
                case "email":
                case "password":
                    return (
                        <Input id={obj.id} value={obj.value} key={key} type={obj.type} placeholder={obj.placeholder}
                               label={obj.name}
                               onChange={this.handleChange}
                               labelClassName={lblCss} wrapperClassName={fldCss}/>);
                default:
                    return (<input key={key}/>)
            }
        }.bind(this));

        var buttons = fm.buttons.map(function (obj) {
            var key = fm.id + "-" + obj.id;
            if (obj.id == "reset") {
                return (<input key={key} type='reset' className={"btn btn-"+obj.style} value={obj.name}/>);
            }
            if (obj.id == "submit") {
                return (
                    <Button onClick={this.handleSubmit} id={key} key={key} bsStyle={obj.style}>
                        {obj.name}
                    </Button>
                )
            }
            return (
                <Button id={key} key={key} bsStyle={obj.style}>
                    {obj.name}
                </Button>
            );
        }.bind(this));

        var message = (<div/>);
        if (this.state.message) {
            var msg = this.state.message;
            message = (
                <Alert bsStyle={msg.style}>
                    <strong>{msg.title}</strong>

                    <p>
                        <ul>
                            {msg.items.map(function (obj, i) {
                                return (<li key={"err-"+i}>{obj}</li>)
                            })}
                        </ul>
                    </p>
                </Alert>
            );
        }

        return (
            <fieldset>
                <legend className="glyphicon glyphicon-list">
                    &nbsp;{fm.name}
                </legend>
                {message}
                <form className='form-horizontal'>
                    {fields}
                    <div className="form-group">
                        <div className="col-sm-offset-3 col-sm-9">
                            <ButtonToolbar>
                                {buttons}
                            </ButtonToolbar>
                        </div>
                    </div>
                </form>
            </fieldset>
        )


    }
});


module.exports = {
    Form: AjaxForm
};