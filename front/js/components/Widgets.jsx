"use strict";

var React = require("react");
var T = require('react-intl');
import {ReactBootstrap, Input, Button, ButtonToolbar} from "react-bootstrap"
var $ = require("jquery");

var AjaxForm = React.createClass({
    getInitialState: function () {
        return {form: {fields: [], buttons: []}}
    },
    componentDidMount: function () {
        $.get(this.props.source, function (result) {
            if (this.isMounted()) {
                this.setState({form: result});
            }
        }.bind(this), "json");
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
                    return (<input ref={obj.id} value={obj.value} key={key} type="hidden"/>);
                case "text":
                case "email":
                case "password":
                    return (
                        <Input ref={obj.id} value={obj.value} key={key} type={obj.type} placeholder={obj.placeholder}
                               label={obj.name}
                               labelClassName={lblCss} wrapperClassName={fldCss}/>);
                default:
                    return (<input key={key}/>)
            }
        });
        var onSubmit = function (e) {
            e.preventDefault();
            var data = {};
            fm.fields.map(function (obj) {
                switch (obj.type) {
                    case "text":
                    case "hidden":
                    case "email":
                    case "password":
                        data[obj.id] = React.findDOMNode(this.refs[obj.id]).value.trim();
                        break;
                    default :
                        console.log("unknown type " + obj.type);
                }

            });
            console.log(data);
            $.post(
                fm.action,
                data,
                function (result) {
                    console.log(result);
                }
            );

        };
        var buttons = fm.buttons.map(function (obj) {
            var key = fm.id + "-" + obj.id;
            if (obj.id == "reset") {
                return (<input key={key} type='reset' className={"btn btn-"+obj.style} value={obj.name}/>);
            }
            if (obj.id == "submit") {
                return (
                    <Button onClick={onSubmit} id={key} key={key} bsStyle={obj.style}>
                        {obj.name}
                    </Button>
                )
            }
            return (
                <Button id={key} key={key} bsStyle={obj.style}>
                    {obj.name}
                </Button>
            );
        });
        return (
            <fieldset>
                <legend className="glyphicon glyphicon-list">
                    &nbsp;{fm.name}
                </legend>
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