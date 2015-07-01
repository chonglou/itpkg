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
                <div className={"col-md-" + (this.props.size || 8)}>
                    <input type={this.props.type}
                        value={this.props.value}
                        onChange={this.props.change}
                        className="form-control"
                        placeholder={this.getIntlMessage(this.props.placeholder)}/>
                </div>
            </div>
        )
    }
});

var Email = React.createClass({
    render: function () {
        return (<Input type="email" {...this.props}/>)
    }
});

var Text = React.createClass({
    render: function () {
        return (<Input type="text" {...this.props}/>)
    }
});

var Hidden = React.createClass({
    render: function () {
        return (<input type="hidden" {...this.props}/>)
    }
});


var Password = React.createClass({
    render: function () {
        return (<Input type="password" {...this.props}/>)
    }
});

var Button = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        return (
            <button className={"btn btn-" + (this.props.style || "default")} onClick={this.props.click}>
            {this.getIntlMessage(this.props.label || "buttons.submit")}
            </button>
        )
    }
});

var Submit = React.createClass({
    render: function () {
        return (<Button style="primary" {...this.props}/>)
    }
});

var Reset = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    render: function () {
        return (<input className="btn btn-default" type="reset" value={this.getIntlMessage("buttons.reset")} />)
    }
});

var Form = React.createClass({
    mixins: [
        T.IntlMixin
    ],
    onSubmit: function (e) {
        e.preventDefault();
        this.props.submit(this.state, e);
    },
    getInitialState: function () {
        var obj = {};
        this.props.fields.map(function (object) {
            obj[object.name] = object.value || "";
        });
        return obj;
    },
    handleChange: function (field, event) {
        var obj = {};
        obj[field] = event.target.value;
        this.setState(obj);
    },
    render: function () {
        var change = this.handleChange;
        var state = this.state;
        var fields = this.props.fields.map(function (object) {
            var key = "fm-f-" + object.name;

            switch (object.type) {
                case "password":
                    return (
                        <Password
                            key={key}
                            size="6"
                            label={object.label || "fields.password" }
                            change={change.bind(null, object.name)}
                            value={state[object.name]}
                            placeholder={object.placeholder || "placeholders.password"}
                        />);
                case "email":
                    return (
                        <Email
                            key={key}
                            size="6"
                            label={object.label || "fields.email" }
                            change={change.bind(null, object.name)}
                            value={state[object.name]}
                            placeholder={object.placeholder || "placeholders.email"}
                        />);
                case "hidden":
                    return (<Hidden key={key} value={state[object.name]} />);
                default:
                    return (<div key={key}>{"Unknown field type " + object.type}</div>);
            }
        });
        return (
            <fieldset>
                <legend className="glyphicon glyphicon-list">
                &nbsp;{this.getIntlMessage(this.props.title)}
                </legend>
                <form className="form-horizontal">
                    {fields}
                    <div className="form-group">
                        <div className="col-sm-offset-3 col-sm-9">
                            <Submit click={this.onSubmit} />
                        &nbsp; &nbsp;
                            <Reset />
                        </div>
                    </div>
                </form>
            </fieldset>
        );
    }
});

module.exports = {
    Form: Form
};