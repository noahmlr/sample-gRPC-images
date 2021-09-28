package com.noahmlr.grpc.samples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    public record Response(String message) {}

    private final HelloWorldClient helloWorldClient;

    public HelloWorldController(HelloWorldClient helloWorldClient) {
        this.helloWorldClient = helloWorldClient;
    }

    @GetMapping("/hello")
    private Response hello(@RequestParam(value = "name", defaultValue = "Noah") String name) {
        var reply = helloWorldClient.greet(name);
        return new Response(reply.getMessage());
    }

}
