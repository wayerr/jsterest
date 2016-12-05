function test() {
    var res = http.execute({url:"http://localhost:18080/not_found.txt"});
    if(res.code !== 200) {
        throw new Error("FAIL:" + res.message);
    }
}
