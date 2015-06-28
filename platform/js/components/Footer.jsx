var React = require("react");

var T = require('react-intl');
var LocalStorageMixin = require('react-localstorage');
var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var padStyle = {
    paddingRight: "20px"
};


var Footer = React.createClass({
    mixins: [T.IntlMixin, LocalStorageMixin],
    getInitialState: function () {
        return {locale: "en-US"};
    },
    switchLocale: function (locale) {
        this.setState({locale: locale});
        location.reload();
    },
    setZhCn: function () {
        this.switchLocale("zh-CN");
    },
    setEnUs: function () {
        this.switchLocale("en-US");
    },
    render: function () {
        return (
            <div>
                <hr/>
                <footer>
                    <p className="pull-right">
                        <img onClick={this.setEnUs} src={enUS} style={padStyle}/>
                        <img onClick={this.setZhCn} src={zhCN} style={padStyle}/>
                        <a href='#'>
                            <T.FormattedMessage
                                message={this.getIntlMessage('links.back_to_top')}/>
                        </a>
                    </p>
                    <p>CHANGE ME</p>
                </footer>
            </div>
        );
    }
});

module.exports = Footer;