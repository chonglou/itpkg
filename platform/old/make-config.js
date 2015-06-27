var path = require("path");
var webpack = require("webpack");
var StatsPlugin = require("stats-webpack-plugin");

module.exports = function (options) {
    var entry = {
        main: options.render ? "./config/render" : "./config/server"
    };

    var output = {
        publicPath: options.server ? "http://localhost:8080/_assets/" : "/_assets/",
        path: path.join(__dirname, "build", options.render ? "assets" : "public")
    };
    return {
        resolve: {
            extensions: ["", ".js", ".jsx"]
        },
        entry: entry,
        output: output,
        debug: options.debug
    };
};

var loadersByExtension = require("./config/loadersByExtension");

module.exports = function (options) {

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

    var root = path.join(__dirname, "app");

    var output = {

        publicPath: publicPath,
        filename: options.prerender ? "[id]-[chunkhash].js" : "[name].js?_v=[chunkhash]",
        chunkFilename: options.prerender ? "[id]-[chunkhash].js" : "[name].js?_v=[chunkhash]",
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
        plugins.push(new StatsPlugin(path.join(__dirname, "build", "stats.assets.json"), {
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
    var asyncLoader = {
        test: require("./app/route-handlers/async").map(function(name) {
            return path.join(__dirname, "app", "route-handlers", name);
        }),
        loader: options.prerender ? "react-proxy-loader/unavailable" : "react-proxy-loader"
    };
    Object.keys(stylesheetLoaders).forEach(function(ext) {
        var stylesheetLoader = stylesheetLoaders[ext];
        if(Array.isArray(stylesheetLoader)) stylesheetLoader = stylesheetLoader.join("!");
        if(options.prerender) {
            stylesheetLoaders[ext] = stylesheetLoader.replace(/^css-loader/, "css-loader/locals");
        } else {
            stylesheetLoaders[ext] = "style-loader!" + stylesheetLoader;
        }
    });

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
        target: options.prerender ? "node" : "web",
        module: {
            loaders: [asyncLoader].concat(loadersByExtension(loaders)).concat(loadersByExtension(stylesheetLoaders)).concat(additionalLoaders)
        },
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