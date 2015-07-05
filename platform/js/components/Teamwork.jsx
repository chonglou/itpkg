"use strict";

var W = require("./Widgets");
var React = require('react');

module.exports = {
  ProjectList: React.createClass({
    getInitialState: function () {
      return {
        projects: [
          {id: 1, name: 'Donuts'},
          {id: 2, name: 'Ice Cream'},
          {id: 3, name: 'Hot Dog'}
        ]
      };
    },

    render: function () {
      return (
        <div className="row">
          <h2>Project List</h2>
          <W.Table klass="table-striped table-bordered"
                   columns={["Id", "Name"]}
                   rows={this.state.projects}></W.Table>
        </div>
      );
    }
  }),

  ProjectTable: React.createClass({
    render: function () {
      return (
        <div className="row">
          <h2>Project Table</h2>
        </div>
      );
    }
  })
};
