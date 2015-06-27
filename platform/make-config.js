var path = require("path");
var webpack = require("webpack");
var StatsPlugin = require("stats-webpack-plugin");
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = function (options) {
    var port = 8080;
    var entry = {
        main: options.render ? "./config/render" : "./config/server"
    };

    var output = {
        publicPath: options.server ? "http://localhost:" + port + "/_assets/" : "/",
        filename: options.render ? "[name]-[chunkhash].js" : "[name].js?_v=[chunkhash]",
        path: path.join(__dirname, "build", options.render ? "assets" : "public")
    };

    var loaders = [
        {test: /\.jsx$/, loader: 'jsx-loader?insertPragma=React.DOM&harmony'},
        {test: /\.css$/, loader: "style-loader!css-loader?importLoaders=1"},
        {test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=8192'}
    ];

    var plugins = [
        new webpack.PrefetchPlugin("react"),
        new webpack.PrefetchPlugin("react/lib/ReactComponentBrowserEnvironment"),
        new StatsPlugin(path.join(__dirname, "build", "stats.json"), {
            chunkModules: true
        })
    ];
    if (options.render) {
        plugins.push(new webpack.optimize.LimitChunkCountPlugin({maxChunks: 1}));
    }
    if (options.minimize) {
        plugins.push(
            new webpack.optimize.UglifyJsPlugin(),
            new webpack.optimize.OccurenceOrderPlugin()
        );
        plugins.push(new HtmlWebpackPlugin({
            filename: "index.html",
            minify: {collapseWhitespace: true}
        }));
    }
    else {
        plugins.push(new HtmlWebpackPlugin({
            filename: "index.html"
        }));
    }

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
