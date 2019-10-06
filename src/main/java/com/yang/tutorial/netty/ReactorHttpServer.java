package com.yang.tutorial.netty;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;

/**
 * @author yangz
 */
public class ReactorHttpServer {

    public static void main(String[] args) {
        // Prepares an HTTP server ready for configuration
        DisposableServer server =
                HttpServer.create()
                        .port(8080)
                        .route(routes ->
                                routes.get("/hello",
                                        (request, response) ->
                                                response.status(HttpResponseStatus.OK)
                                                        .header(HttpHeaderNames.CONTENT_LENGTH, "12")
                                                        .sendString(Mono.just("Hello World!"))))
                        .bindNow();

        server.onDispose()
                .block();
    }

}
