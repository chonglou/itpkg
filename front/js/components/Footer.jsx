"use strict";

var React = require("react");
var T = require('react-intl');

var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var padStyle = {
    paddingRight: "20px"
};

var HttpMixin = require("../mixins/Http");
var cookie = require("js-cookie");

var Footer = React.createClass({
    mixins: [T.IntlMixin, HttpMixin],
    switchLocale: function (locale) {
        cookie.set("LANG", locale, {expires: 7});
        location.reload();
    },
    getInitialState: function () {
        return {
            copyright: ''
        };
    },
    componentDidMount: function () {
        this.get("/info", function (rs) {
            document.title = rs.title;
            this.setState({
                copyright: rs.copyright
            });
        });
    },
    render: function () {
        return (
            <div className="row">
                <hr/>
                <footer>
                    <p className="pull-right">
                        <img onClick={this.switchLocale.bind(this, "en_US")} src={enUS} style={padStyle}/>
                        <img onClick={this.switchLocale.bind(this, "zh_CN")} src={zhCN} style={padStyle}/>
                        <a href='#'>
                            <T.FormattedMessage
                                message={this.getIntlMessage('links.back_to_top')}/>
                        </a>
                    </p>

                    <p>{this.state.copyright}</p>
                </footer>
            </div>
        );
    }
});

module.exports = Footer;