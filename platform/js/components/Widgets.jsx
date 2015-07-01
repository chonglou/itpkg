"use strict";

var React = require("react");
var T = require('react-intl');

var Input = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        return (
            <div className="form-group">
                <label className={"col-md-3 control-label" + (this.props.nil ? "" : " required")}>
                                {this.getIntlMessage(this.props.label)}
                </label>
                <div className={"col-md-" + this.props.size }>
                    <input type={this.props.type}
                        className="form-control"
                        placeholder={this.props.placeholder}/>
                </div>
            </div>
        );
    }
});

var Buttons = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        return (
            <div className="form-group">
                <div className="col-sm-offset-3 col-sm-9">
                    <button type="submit" className="btn btn-primary">{this.getIntlMessage("buttons.submit")}</button>
                &nbsp; &nbsp;
                    <input type="reset" className="btn btn-default" value={this.getIntlMessage("buttons.reset")}/>
                </div>
            </div>
        );
    }
});

var Form = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        var fields = this.props.fields.map(function (object) {
            var key = "fm-f-" + object.name;
            switch (object.type) {
                case "hidden":
                    return <input key={key} type="hidden" value={object.value}/>;
                case "text":
                    return <Input key={key} type="text" size={object.size || 6} nil={object.nil} label={object.label} placeholder={object.placeholder || ""}/>;
                case "password":
                    return <Input key={key} type="password" size={object.size || 8} nil={object.nil} label={object.label || "fields.password"} placeholder={object.placeholder || ""}/>;
                case "email":
                    return <Input key={key} type="email" size={object.size || 8} nil={object.nil} label={object.label || "fields.email"} placeholder={object.placeholder || "change-me@gmail.com"}/>;
                default:
                    return <label>{"Unknown field type " + object.type}</label>
            }
        });
        return (
            <fieldset>
                <legend>
                    <span className="glyphicon glyphicon-list"/>
                &nbsp;
                    {this.getIntlMessage(this.props.title)}
                </legend>
                <form className="form-horizontal">
                    {fields}
                    <Buttons/>
                </form>
            </fieldset>
        );
    }
});

module.exports = {
    Input: Input,
    Buttons: Buttons,
    Form: Form
};