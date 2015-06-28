var React = require("react");
var T = require('react-intl');

var padStyle = {
    paddingRight: "20px"
};

var zhCN = require("famfamfam-flags/dist/png/cn.png");
var enUS = require("famfamfam-flags/dist/png/us.png");

var Footer = React.createClass({
    mixins: [T.IntlMixin],
    render: function () {
        return (
            <div>
                <hr/>
                <footer>
                    <p className="pull-right">
                        <img src={enUS} style={padStyle}/>
                        <img src={zhCN} style={padStyle}/>
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