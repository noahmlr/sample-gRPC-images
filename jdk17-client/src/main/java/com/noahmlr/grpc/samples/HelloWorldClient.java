package com.noahmlr.grpc.samples;

import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

public class HelloWorldClient {
    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public HelloWorldClient(Channel channel) {
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        var request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException ex) {
            System.out.println("Failed to Greet");
            return;
        }
        System.out.println("Greeting: " + response.getMessage());
    }
}
