var path = require("path");
var webpack = require("webpack");

module.exports = function (options) {
    var entry = {
        main: options.prerender ? "./config/render" : "./config/app"
    };

    var loaders = {
        "jsx": options.hotComponents ? ["react-hot-loader", "babel-loader?stage=0"] : "babel-loader?stage=0",
        "js": {
            loader: "babel-loader?stage=0",
            include: path.join(__dirname, "app")
        },
        "json": "json-loader",
        "coffee": "coffee-redux-loader",
        "json5": "json5-loader",
        "txt": "raw-loader",
        "png|jpg|jpeg|gif|svg": "url-loader?limit=10000",
        "woff|woff2": "url-loader?limit=100000",
        "ttf|eot": "file-loader",
        "wav|mp3": "file-loader",
        "html": "html-loader",
        "md|markdown": ["html-loader", "markdown-loader"]
    };
    var cssLoader = options.minimize ? "css-loader?module" : "css-loader?module&localIdentName=[path][name]---[local]---[hash:base64:5]";
    var stylesheetLoaders = {
        //"less": [cssLoader, "less-loader"],
        //"styl": [cssLoader, "stylus-loader"],
        //"scss|sass": [cssLoader, "sass-loader"],
        "css": cssLoader
    };

    var additionalLoaders = [];
    var alias = {};
    var aliasLoader = {};
    var externals = [];

    var modulesDirectories = ["web_modules", "node_modules"];
    var extensions = ["", ".js", ".jsx"];
    var root = path.join(__dirname, "app");
    var publicPath = options.devServer ? "http://localhost:8080/_assets/" : "/_assets/";
    var output = {
        path: path.join(__dirname, "build", options.prerender ? "prerender" : "public"),
        publicPath: publicPath,
        filename: options.prerender ? "[id]-[chunkhash].js" : "[name].js?[chunkhash]",
        chunkFilename: options.prerender ? "[id]-[chunkhash].js" : "[name].js?[chunkhash]",
        sourceMapFilename: "debugging/[file].map",
        libraryTarget: options.prerender ? "commonjs2" : undefined,
        pathinfo: options.debug || options.prerender
    };
    var excludeFromStats = [
        /node_modules[\\\/]react(-router)?[\\\/]/,
        /node_modules[\\\/]items-store[\\\/]/
    ];
    var plugins = [
        new webpack.PrefetchPlugin("react"),
        new webpack.PrefetchPlugin("react/lib/ReactComponentBrowserEnvironment")
    ];
    if (options.prerender) {
        plugins.push(new StatsPlugin(path.join(__dirname, "build", "stats.prerender.json"), {
            chunkModules: true,
            exclude: excludeFromStats
        }));
        aliasLoader["react-proxy$"] = "react-proxy/unavailable";
        aliasLoader["react-proxy-loader$"] = "react-proxy-loader/unavailable";
        externals.push(
            /^react(\/.*)?$/,
            /^reflux(\/.*)?$/,
            "superagent",
            "async"
        );
        plugins.push(new webpack.optimize.LimitChunkCountPlugin({maxChunks: 1}));
    } else {
        plugins.push(new StatsPlugin(path.join(__dirname, "build", "stats.json"), {
            chunkModules: true,
            exclude: excludeFromStats
        }));
    }

    if (options.minimize) {
        plugins.push(
            new webpack.DefinePlugin({
                "process.env": {
                    NODE_ENV: JSON.stringify("production")
                }
            }),

            new webpack.optimize.UglifyJsPlugin(),
            new webpack.optimize.OccurenceOrderPlugin(),

            new webpack.NoErrorsPlugin()
        );
    }


    return {
        entry: entry,
        output: output,
        target: options.prerender ? "node" : "web",
        module: {
            loaders: [asyncLoader].concat(loadersByExtension(loaders)).concat(loadersByExtension(stylesheetLoaders)).concat(additionalLoaders)
        },
        devtool: options.devtool,
        debug: options.debug,
        resolveLoader: {
            root: path.join(__dirname, "node_modules"),
            alias: aliasLoader
        },
        externals: externals,
        resolve: {
            root: root,
            modulesDirectories: modulesDirectories,
            extensions: extensions,
            alias: alias
        },
        plugins: plugins,
        devServer: {
            stats: {
                cached: false,
                exclude: excludeFromStats
            }
        }
    };
};