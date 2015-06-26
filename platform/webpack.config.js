var webpack = require("webpack");
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: ["./3rd.js","./main.js"],
    output: {
        path: "public",
        filename: "[name]-[chunkhash].js"
    },
    plugins:[
        //new webpack.optimize.LimitChunkCountPlugin({maxChunks: 15}),
        new HtmlWebpackPlugin({
            //template:"index.html",
            minify:{collapseWhitespace:true}
        }),
        new webpack.optimize.UglifyJsPlugin(),
        new webpack.optimize.OccurenceOrderPlugin()
    ],
    module: {
        loaders: [
            { test: /\.css$/, loader: "style!css" }
        ]
    }
};