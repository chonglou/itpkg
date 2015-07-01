"use strict";

var React = require("react");
var T = require('react-intl');

var ReactBootstrap = require('react-bootstrap');

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

var HttpMixin = require("../mixins/Http");
var $ = require("jquery");

var Form = React.createClass({
    mixins: [
        T.IntlMixin,
        HttpMixin
    ],
    onSubmit: function (e) {
        e.preventDefault();

        this.post(this.props.action, this.state.form, function (rs) {
            var msg = rs.ok ? rs.data : rs.errors;
            this.setState({
                result: rs,
                modal: {
                    show: true,
                    style: rs.ok ? "success" : "danger",
                    title: this.getIntlMessage(rs.ok ? "messages.success" : "messages.fail"),
                    body: msg ? msg.join("<br/>") : ""
                }
            });
        });
    },
    getInitialState: function () {
        var obj = {
            modal: {show: false},
            form: {}
        };
        this.props.fields.map(function (object) {
            obj.form[object.name] = object.value || "";
        });
        return obj;
    },
    handleChange: function (field, event) {
        var form = this.state.form;
        form[field] = event.target.value;
        this.setState({form: form});
    },
    close: function () {
        this.setState({modal: {show: false}}, function () {
            if (this.props.submit) {
                this.props.submit(this.state.result);
            }
        });
    },
    render: function () {
        var change = this.handleChange;
        var state = this.state.form;
        var fields = this.props.fields.map(function (object) {
            var key = "fm-f-" + object.name;

            switch (object.type) {
                case "text":
                    return (
                        <Text
                            key={key}
                            size={object.size || "9" }
                            label={object.label }
                            change={change.bind(null, object.name)}
                            value={state[object.name]}
                            placeholder={object.placeholder || "placeholders.empty_text"}
                        />);
                case "password":
                    return (
                        <Password
                            key={key}
                            size="5"
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
            <div className="row">
                <ReactBootstrap.Modal
                    backdrop={false}
                    bsStyle={this.state.modal.style || "default"}
                    show={this.state.modal.show} onHide={this.close}>
                    <ReactBootstrap.Modal.Header closeButton>
                        <ReactBootstrap.Modal.Title>
                        {this.state.modal.title}
                        </ReactBootstrap.Modal.Title>
                    </ReactBootstrap.Modal.Header>
                    <ReactBootstrap.Modal.Body>
                        {this.state.modal.body}
                    </ReactBootstrap.Modal.Body>
                    <ReactBootstrap.Modal.Footer>
                        <ReactBootstrap.Button onClick={this.close}>
                        {this.getIntlMessage("buttons.close")}
                        </ReactBootstrap.Button>
                    </ReactBootstrap.Modal.Footer>
                </ReactBootstrap.Modal>
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
            </div>
        );
    }
});

//
//var Dialog = React.createClass({
//    getInitialState() {
//        return {
//            visible: true
//        };
//    },
//    close: function () {
//        console.log("aaa");
//        this.state.visible = false;
//    },
//    render: function () {
//        if (this.state.visible) {
//            return (
//                <div className={"alert alert-" + (this.props.style || "warning") + " alert-dismissible"} role="alert">
//                    <button type="button" className="close" data-dismiss="alert" aria-label="Close">
//                        <span aria-hidden="true" onClick={this.close}>&times;</span>
//                    </button>
//                    <strong>{this.props.title}</strong>
//                    <p>{this.props.messages}</p>
//                </div>
//            );
//        }
//
//        return (<br/>)
//
//    }
//});


module.exports = {
    Form: Form
};