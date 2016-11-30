function () {
    var api = {};
    api.login = function(name, password) {
         var request = {
            url: api.host + "/api/login",
            data: { username:name, password:password }
        };
        var resp = http.execute(request);
        return resp.data.key;
    };
    api.list = function() {
         var request = {
            url: api.host + "/api/list",
        };
        var resp = http.execute(request);
        return resp.data;
    };
    return api;
}
