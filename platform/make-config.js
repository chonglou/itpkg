var path = require("path");
var webpack = require("webpack");
var StatsPlugin = require("stats-webpack-plugin");

module.exports = function (options) {
    var entry = {
        main: options.render ? "./config/render" : "./config/server"
    };

    var output = {
        publicPath: options.server ? "http://localhost:8080/_assets/" : "/_assets/",
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
        debug: options.debug
    };
};
