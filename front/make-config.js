"use strict";

var path = require("path");
var webpack = require("webpack");
var StatsPlugin = require("stats-webpack-plugin");
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = function (options) {
    var port = 8088;
    var entry = {
        main: options.render ? "./js/render" : "./js/server",
        vendor: [
            "jquery",
            "react",
            "reflux",
            "react-bootstrap",
            "react-router",
            "react-router-bootstrap",
            "react-localstorage",
            "react-intl",
            "js-cookie",
            "js-base64",
            "jwt-decode"
        ]
    };

    var output = {
        // publicPath: options.render ? "/" : "http://localhost:" + port + "/",
        filename: options.render ? "[chunkhash].js" : "[name].js",
        path: path.join(__dirname, "build", options.render ? "assets" : "public")
    };

    var loaders = [
        //{test: /\.jsx$/, loader: 'jsx-loader?insertPragma=React.DOM&harmony'},
        {
            test: /\.jsx?$/,
            exclude: /(node_modules)/,
            loaders: options.hot ? ["react-hot-loader", "babel-loader?stage=0"] : ["babel-loader?stage=0", "strip-loader?strip[]=console.log"]
        },

        //{test: /\.css$/, loader: "style-loader!css-loader?importLoaders=1"},
        {test: /\.css$/, loader: ExtractTextPlugin.extract("style-loader", "css-loader")},

        //{test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=8192'}
        {
            test: /\.(jpe?g|png|gif|woff|woff2|eot|ttf|svg)$/i,
            loaders: [
                'file?hash=md5&digest=hex&name=[hash].[ext]',
                'image-webpack?bypassOnDebug&optimizationLevel=7&interlaced=false'
            ]
        }
    ];

    var plugins = [
        new ExtractTextPlugin(options.render ? "[chunkhash].css" : "[name].css", {allChunks: true}),
        new webpack.optimize.CommonsChunkPlugin("vendor", options.render ? "[chunkhash].js" : "[name].js"),
        new webpack.PrefetchPlugin("react"),
        new webpack.PrefetchPlugin("react/lib/ReactComponentBrowserEnvironment"),
        new StatsPlugin("stats.json", {
            chunkModules: true
        })
    ];
    if (options.render) {
        plugins.push(
            new webpack.optimize.LimitChunkCountPlugin({maxChunks: 15})
        );
    }

    var htmlOption = {
        template: "html/index.html",
        favicon: "./image/favicon.ico"
    };
    if (options.minimize) {
        plugins.push(
            new webpack.optimize.DedupePlugin(),
            new webpack.optimize.UglifyJsPlugin({
                compressor: {
                    warnings: false
                }
            }),
            new webpack.optimize.OccurenceOrderPlugin(),
            new webpack.NoErrorsPlugin()
        );
        htmlOption.minify = {collapseWhitespace: true};
    }
    plugins.push(new HtmlWebpackPlugin(htmlOption));

    if (options.hot) {
        plugins.push(new webpack.HotModuleReplacementPlugin());
    }

    return {
        entry: entry,
        output: output,
        plugins: plugins,
        module: {
            loaders: loaders
        },
        resolveLoader: {
            root: path.join(__dirname, "node_modules")
        },
        resolve: {
            extensions: ["", ".js", ".jsx"]
        },
        devServer: {
            contentBase: output.path,
            port: port,
            // noInfo: true,
            hot: options.hot,
            inline: true,
            proxy: {
                "*": "http://localhost:8080"
            }
        },
        debug: options.debug,
        devtool: options.devtool
    };
};
