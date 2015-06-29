var React = require("react");
var $ = require("jquery");

var T = require('react-intl');

var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var padStyle = {
    paddingRight: "20px"
};


var Footer = React.createClass({
    mixins: [T.IntlMixin, BaseUrlMixin],
    switchLocale: function (locale) {
        localStorage.locale = locale;
        location.reload();
    },
    getInitialState: function () {
        return {
            copyright: ''
        };
    },
    componentDidMount: function () {
        $.ajax({
            url: this.url("/base/copyright"),
            success: function (rs) {
                if (this.isMounted()) {
                    this.setState({
                        copyright: rs.copyright
                    });
                }
            }.bind(this),
            jsonpCallback: "cb",
            type: "GET",
            dataType: "jsonp"
        });
        //$.get(this.url("/base/copyright"), function(rs) {
        //    if (this.isMounted()) {
        //        this.setState({
        //            copyright: rs.copyright
        //        });
        //    }
        //}.bind(this), "jsonp");
    },
    render: function () {
        return (
            <div>
                <hr/>
                <footer>
                    <p className="pull-right">
                        <img onClick={this.switchLocale.bind(this, "en-US")} src={enUS} style={padStyle}/>
                        <img onClick={this.switchLocale.bind(this, "zh-CN")} src={zhCN} style={padStyle}/>
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