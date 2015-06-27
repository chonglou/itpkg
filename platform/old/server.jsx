//console.log("Development Mode");
//
//import async from "async";
//import React from "react";
//import Router from "react-router";
//import withTimeout from "./withTimeout";
//import ReactUpdates from "react/lib/ReactUpdates";
//
//function renderApplication(routes, stores, options) {
//    var timeout = options.timeout || 600;
//
//    var initialRun = true;
//
//    Router.run(routes, Router.HistoryLocation, function(Application, state) {
//
//        if(!initialRun) {
//            Object.keys(stores).forEach(function(key) {
//                stores[key].outdate();
//            });
//        }
//        initialRun = false;
//
//        ReactUpdates.batchedUpdates(function() {
//            stores.Router.setItemData("transition", state);
//        });
//
//        withTimeout(async.forEach.bind(async, state.routes, function(route, callback) {
//            if(route.handler.chargeStores) {
//                route.handler.chargeStores(stores, state.params, callback);
//            } else {
//                callback();
//            }
//        }), timeout, function() {
//
//            ReactUpdates.batchedUpdates(function() {
//                stores.Router.setItemData("transition", null);
//            });
//
//            React.render(
//                <StoresWrapper Component={Application} stores={stores}/>,
//                document.getElementById("content")
//            );
//        });
//    });
//}