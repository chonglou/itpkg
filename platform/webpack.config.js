var webpack = require("webpack");
var HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    entry: "./main.jsx",
    output: {
        path: "public",
        filename: "[name]-[chunkhash].js"
    },
    plugins:[
        //new webpack.optimize.LimitChunkCountPlugin({maxChunks: 15}),
        new HtmlWebpackPlugin({
            template:"index.html",
            minify:{collapseWhitespace:true}
        }),
        new webpack.optimize.UglifyJsPlugin(),
        new webpack.optimize.OccurenceOrderPlugin()
    ],
    module: {
        loaders: [
            //{ test: /\.css$/, loader: "style!css" }
            { test: /\.jsx$/, loader: 'jsx-loader?insertPragma=React.DOM&harmony' },
            { test: /\.css$/, loader: "style-loader!css-loader?importLoaders=1" },
            { test: /\.(png|woff|woff2|eot|ttf|svg)$/, loader: 'url-loader?limit=8192' }
        ]
    },
    externals: {
        jquery: "jQuery",
        'react': 'React'
    },
    resolve: {
        extensions: ['', '.js', '.jsx']
    }
};