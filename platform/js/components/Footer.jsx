var React = require("react");

var T = require('react-intl');
var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var padStyle = {
    paddingRight: "20px"
};


var Footer = React.createClass({
    mixins: [T.IntlMixin],
    render: function () {
        return (
            <div>
                <hr/>
                <footer>
                    <p className="pull-right">
                        <a target="_blank" href="/index.html?locale=en-US">
                            <img src={enUS} style={padStyle}/>
                        </a>
                        <a target="_blank" href="/index.html?locale=zh-CN">
                            <img src={zhCN} style={padStyle}/>
                        </a>
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