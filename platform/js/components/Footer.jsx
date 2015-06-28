var React = require("react");

var T = require('react-intl');
var L = require("./locales");
var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var padStyle = {
    paddingRight: "20px"
};


var Footer = React.createClass({
    mixins: [T.IntlMixin],
    setZhCn: function () {
        var App = require("../app");
        App.Root.setProps(L.zhCN);
    },
    setEnUs: function () {
        var App = require("../app");
        App.Root.setProps(L.enUS);
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