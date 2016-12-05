# jsterest
The tool for make REST API test on javascript. It is scan specified dir for test files and run specified test.

It required only java > 1.8.0_112 (minor version is impotant due to some bugs in js engine).

Command line example:

    java -jar jsterest.jar -l /tmp/jsterest/log/ -t /home/username/api_test/ one_test, second_test

where: one_test, second_test - the javascript files /home/username/api_test/one_test.js and etc.

Each test is a js function in 'strict mode' like this:
```js
function test() {
  var request = {
    url:"http://localhost:8761/ui/token/login",
    method: "POST", // GET, POST & etc, by default - GET, or POST if it has data
    headers: {
      // set of headers, tool is use json 
      'Content-Type': 'application/json'
    },
    // request data, string or javascript object which is serialized to JSON
    data:{
      username:"admin",
      password:"password"
    },
    // optional function which can handle response, also it invoked before response logging 
    //   and can set `response.message` for example or do custom logging
    onResponse: function(request, response) {}
  };
  var resp = http.execute(request);
  console.debug("resp: ", resp);
  /*
  it prints json like follow:
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
}
```

## build ##

It simple user maven for build:

```bash
cd jterest_dir
mvn -Dmaven.test.skip=true clean package
cd /target/
#and now we can run it
java -jar jsterest-1.0-SNAPSHOT.jar -t ../src/test/resources sample
```

