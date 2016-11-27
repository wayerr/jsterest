function test(ctx) {
    var request = {
        headers: {
            //'X-Auth-Token': "ctx.token"
            'Content-Type': 'application/json'
        },
        url:"http://localhost:8761/ui/token/login",
        data:{
            username:"admin",
            password:"password"
        }
    };
    var resp = http.execute(request);
    console.debug("resp: ", resp.data.key);
}
