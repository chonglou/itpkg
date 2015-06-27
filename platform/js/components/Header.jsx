var React = require("react");

var Router = require('react-router');
var RB = require('react-bootstrap');
var RRB = require('react-router-bootstrap');


var Header = React.createClass({
    render: function () {
        return (
            <RB.Navbar brand='IT-PACKAGE' inverse fixedTop toggleNavKey={0}>
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