/**
 * A http warpper for using from JS tests.
 *
 * Request:
 * {
 *    url: "http://host:port/path",
 *    method: "GET",
 *    headers: {
 *      "Content-Type":"application/json"
 *    },
 *    data: {...}
 * }
 *
 * Response:
 * {
 *    code: 200,
 *    headers: {
 *      "Content-Type":"application/json"
 *    },
 *    data: {...}
 * }
 *
 * Also see: https://wiki.openjdk.java.net/display/Nashorn/Nashorn+extensions
 */
http = new (function() {
    var Url = java.net.URL;
    function processResponse(conn) {
        var headers = {};

        for(var headerKey in conn.headerFields) {
            headers[headerKey] = conn.getHeaderField(headerKey);
        }
        var resp = {
            code: conn.responseCode,
            message: conn.responseMessage,
            contentType: conn.contentType,
            headers: headers
        };
        console.debug("Got response:", JSON.stringify(resp));
        // data may be too big for logging
        var data = io.readFully(conn.inputStream);
        console.debug("Got response:", resp.contentType);
        if(resp.contentType.indexOf('application/json') == 0) {
            data = JSON.parse(data);
        }
        resp.data = data;
        return resp;
    }
    this.execute = function(req, ctx) {
        console.debug("Execute request:", JSON.stringify(req));
        var url = new Url(req.url);
        var conn = url.openConnection();
        conn.requestMethod = req.method || "GET";
        var contentType;
        for(var headerKey in req.headers) {
            var headerVal = req.headers[headerKey];
            if("content-type" === headerKey.toLowerCase()) {
                contentType = headerVal;
            }
            console.debug(headerKey, headerVal);
            conn.setRequestProperty(headerKey, headerVal);
        }
        if(!contentType) {
            //set default content type
            conn.setRequestProperty("Content-Type", "application/json");
        }
        if(req.data) {
            var data = req.data;
            if(typeof data !== "string") {
                data = JSON.stringify(data);
            }
            conn.doOutput = true;
            io.writeFully(data, conn.getOutputStream());
        }
        conn.connect();
        return processResponse(conn);
    };
})();
