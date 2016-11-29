var api = include("app_api");
function test() {
   api.host = "http://localhost:18080";
   var token = api.login("admin", "password");
   console.debug("token:", token);
   //console.debug(api.nodes(token));
}
