// Dev-only: webpack dev server runs on :3000 and proxies /api to Spring Boot on :8080
if (config.devServer) {
    config.devServer.port = 3000;
    config.devServer.proxy = [
        {
            context: ["/api"],
            target: "http://localhost:8080",
            changeOrigin: true
        }
    ];
}
