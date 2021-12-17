/*eslint no-undef: "error"*/
/*eslint-env node*/

const { CleanWebpackPlugin } = require('clean-webpack-plugin');
var HtmlWebpackPlugin = require('html-webpack-plugin');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');

var path = require('path');

module.exports = {
    mode: 'development',
    resolve: {
        extensions: ['.js', '.jsx'],
        alias: {
            '@src': path.resolve(__dirname, 'src/'),
            '@view': path.resolve(__dirname, 'src/view/'),
            '@store': path.resolve(__dirname, 'src/store/'),
            '@core': path.resolve(__dirname, 'src/core/'),
        }
    },
    module: {
        rules: [
            {
                test: /\.jsx?$/,
                loader: 'babel-loader'
            },
            {
                test: /\.css$/,
                use:[
                    MiniCssExtractPlugin.loader,
                    "css-loader",
                ]
            },
            {
                test: /\.styl$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    'stylus-loader'
                ]
            },
            {
                test: /\.(png|jpg|gif|jpeg|svg)$/,
                use: [
                    {
                        loader: 'url-loader',
                        options: {
                            limit: 1024,
                            name: '[name].[ext]'
                        }
                    }
                ]
            },
        ]
    },
    plugins: [new HtmlWebpackPlugin({
        template: './src/index.html'
    }),
        new CleanWebpackPlugin(),
        new MiniCssExtractPlugin({
            filename:"[name].css",
            chunkFilename: "[id].css"
        })
    ],
    devServer: {
        historyApiFallback: true
    },
    externals: {
        // global app config object
        config: JSON.stringify({
            apiUrl: 'http://172.20.111.171:10014/mb_client',
            wsUrl: 'ws://172.20.111.171:10014/mb_client/websocket',
        })
    }
}