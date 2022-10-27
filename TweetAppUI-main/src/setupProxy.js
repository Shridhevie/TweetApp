const { createProxyMiddleware } = require('http-proxy-middleware');
 
module.exports = function (app) {
  app.use(
    createProxyMiddleware('/api1', {
       target: 'http://localhost:8082', // API endpoint 1
      //target: 'http://3.87.108.193:8082',
      //target:'http://tweetappuserservice-env.eba-rtp2gyeu.us-east-2.elasticbeanstalk.com',
      changeOrigin: true,
      pathRewrite: {
        "^/api1": "",
      },
      headers: {
        Connection: "keep-alive",
        "Access-Control-Allow-Origin": '*',
       "Access-Control-Allow-Methods": 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
      }
    })
  );
  app.use(
    createProxyMiddleware('/api2', {
       target: 'http://localhost:8083', // API endpoint 2
      //target: 'http://34.229.167.216:8083',
      //target:'http://tweetapptweetservice-env.eba-ccbj9pt4.us-east-2.elasticbeanstalk.com',
      changeOrigin: true,
      pathRewrite: {
        "^/api2": "",
      },
      headers: {
        Connection: "keep-alive",
        "Access-Control-Allow-Origin": '*',
       "Access-Control-Allow-Methods": 'GET,PUT,POST,DELETE,PATCH,OPTIONS',
      }
    })
  );
}