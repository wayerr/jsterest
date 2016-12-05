/**
 * A http wrapper for using from JS tests.
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
var http = new (function() {
    this.debug = false;
    var Url = java.net.URL;
    var thiz = this;
    function processResponse(conn) {
        var headers = {};

        var resp = {
            code: conn.responseCode,
            message: conn.responseMessage,
            contentType: conn.contentType
        };
        var hasError = resp.code >= 400;
        if(hasError || thiz.debug) {
            console.debug("Got response:", JSON.stringify(resp));
        }
        if(!hasError) {
            for(var headerKey in conn.headerFields) {
                if(!headerKey) {
                    //skip  '"null":"HTTP/1.1 200 OK"'
                    continue;
                }
                headers[headerKey] = conn.getHeaderField(headerKey);
            }
            resp.headers = headers;
            // data may be too big for logging
            var data = io.readFully(conn.inputStream);
            if(resp.contentType && resp.contentType.indexOf('application/json') == 0) {
                data = JSON.parse(data);
            }
            resp.data = data;
        }
        return resp;
    }
    this.execute = function(req, ctx) {
        if(this.debug) {
            console.debug("Execute request:", JSON.stringify(req));
        }
        var url = new Url(req.url);
        var conn = url.openConnection();
        if(req.method) {
            //default method detected inner by exists of request body, se we not need to specify it
            conn.requestMethod = req.method;
        }
        var contentType;
        for(var headerKey in req.headers) {
            var headerVal = req.headers[headerKey];
            if("content-type" === headerKey.toLowerCase()) {
                contentType = headerVal;
            }
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
