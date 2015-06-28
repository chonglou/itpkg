var path = require("path");
var webpack = require("webpack");
var StatsPlugin = require("stats-webpack-plugin");
var HtmlWebpackPlugin = require('html-webpack-plugin');
var ExtractTextPlugin = require("extract-text-webpack-plugin");

module.exports = function (options) {
    var port = 8080;
    var entry = {
        main: options.render ? "./js/render" : "./js/server",
        vendor: [
            "jquery",
            "react",
            "flux",
            "react-bootstrap",
            "react-router",
            "react-router-bootstrap",
            "react-intl"
        ]
    };

    var output = {
       // publicPath: options.render ? "/" : "http://localhost:" + port + "/",
        filename: options.render ? "[id]-[chunkhash].js" : "[name].js",
        path: path.join(__dirname, "build", options.render ? "assets" : "public")
    };

    var loaders = [
        {test: /\.jsx$/, loader: 'jsx-loader?insertPragma=React.DOM&harmony'},

        //{test: /\.css$/, loader: "style-loader!css-loader?importLoaders=1"},
        {test: /\.css$/, loader: ExtractTextPlugin.extract("style-loader", "css-loader")},

        {test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=8192'}
    ];

    var plugins = [
        new ExtractTextPlugin("[name].css", {allChunks: true}),
        new webpack.optimize.CommonsChunkPlugin("vendor", "3rd.js"),
        new webpack.PrefetchPlugin("react"),
        new webpack.PrefetchPlugin("react/lib/ReactComponentBrowserEnvironment"),
        new StatsPlugin(path.join(__dirname, "build", "stats.json"), {
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
        favicon:"./image/favicon.ico"
    };
    if (options.minimize) {
        plugins.push(
            new webpack.optimize.UglifyJsPlugin(),
            new webpack.optimize.OccurenceOrderPlugin()
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
            inline: true
        },
        debug: options.debug
    };
};
