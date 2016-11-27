function test(ctx) {
    var request = {
        headers: {
            'X-Auth-Token': ctx.token
        },
        path:"/login",
        data:[]
    };
    http.execute(request);
    java.lang.System.out.println(JSON.stringify(request));
}
