var api = include("app_api");
function test() {
   api.host = "http://localhost:8761";
   var token = api.login("admin", "password");
   console.debug(api.nodes(token));
}
