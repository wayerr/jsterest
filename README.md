# jsterest
tool for make REST API test on javascript


Tool is scan specified dir for test files and run specified test.

Each test is a js function like this:
```js
function test() {
  var request = {
    url:"http://localhost:8761/ui/token/login",
    method: "GET", // POST & etc, by default - GET
    headers: {
      // set of headers, tool is use json 
      'Content-Type': 'application/json'
    },
    // request data, string or javascript object which is serialized to JSON
    data:{
      username:"admin",
      password:"password"
    }
  };
  var resp = http.execute(request);
  /*
  response is like follow:
  {
    code: 200,
    message: "OK", //server message
    headers: {
      "Content-Type":"application/json",
      "X-Other-Header":"header value"
    },
    contentType:"application/json",
    // string or java object (converted automatically when response content type is json)
    data: {...}
  }       
  */
  console.debug("resp: ", resp.data.key);
}
```

