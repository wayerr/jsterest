var api = include("app_api");
function test() {
    api.host = "http://localhost:18080";
    var token = api.login("admin", "password");
    console.debug("token:", token);
    console.debug("list:", api.list(token));
    console.debug("ENV: ", process.env);
    console.debug("PATH: ", process.env.PATH);

    console.assert(true, "It newer been happened");

    try {
        console.assert(false, "process.env.TEST - is undefined");
        throw new Error("FAIL");
    } catch(e) {
        console.log("Error:", e);
        if(!(e instanceof AssertionError)) {
            throw new Error("FAIL");
        }
    }
    var src = {prop: [1]};
    var res = clone(src);
    console.assert(res.prop[0] === src.prop[0], "Cloned objects is not same.");

    try {
        let s = io.load("unknownfile.txt");
        console.assert(false, "It load unexists file!");
    } catch(e) {
        console.error("Expected error: ", e);
    }
}
