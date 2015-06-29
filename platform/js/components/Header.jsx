var React = require("react");
var T = require('react-intl');

var Router = require('react-router');
var RB = require('react-bootstrap');
var RRB = require('react-router-bootstrap');

var HttpMixin = require("../mixins/http");

var Header = React.createClass({
    getInitialState: function () {
        return {
            title: ''
        };
    },
    componentDidMount: function () {
        this.get("/base/nav-bar", function (rs) {
            document.title = rs.title;
            if (this.isMounted()) {
                this.setState({
                    title: rs.title
                });
            }
        }.bind(this));
    },
    mixins: [T.IntlMixin, HttpMixin],
    render: function () {
        return (
            <RB.Navbar brand={this.state.title} inverse fixedTop toggleNavKey={0}>
                <RB.Nav right eventKey={0}> {}
                    <RB.NavItem eventKey={1} href='#'>Link1</RB.NavItem>
                    <RB.NavItem eventKey={2} href='#'>Link2</RB.NavItem>
                    <RB.DropdownButton eventKey={3} title='Dropdown'>
                        <RB.MenuItem eventKey='1'>Action</RB.MenuItem>
                        <RB.MenuItem eventKey='2'>Another action</RB.MenuItem>
                        <RB.MenuItem eventKey='3'>Something else here</RB.MenuItem>
                        <RB.MenuItem divider />
                        <RB.MenuItem eventKey='4'>Separated link</RB.MenuItem>
                    </RB.DropdownButton>
                </RB.Nav>
            </RB.Navbar>
        );
    }
});


module.exports = Header;