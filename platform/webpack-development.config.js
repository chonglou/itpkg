module.exports = [
    require("./make-web-config")({
        devServer: true,
        hotComponents: true,
        debug: true
    })
];