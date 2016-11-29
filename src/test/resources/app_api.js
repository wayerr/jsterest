function () {
    var api = {};
    api.login = function(name, password) {
         var request = {
            url: api.host + "/api/login",
            data: { username:name, password:password }
        };
        var resp = http.execute(request);
        console.debug("resp: ", resp.data.key);
    };
    return api;
}
