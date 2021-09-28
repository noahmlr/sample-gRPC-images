package com.noahmlr.grpc.samples;

import io.grpc.Channel;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class HelloWorldClient  {
    Logger logger = Logger.getLogger(HelloWorldClient.class.getName());

    private final GreeterGrpc.GreeterFutureStub futureStub;

    public HelloWorldClient(Channel channel) {
        futureStub = GreeterGrpc.newFutureStub(channel);
    }

    public HelloReply greet(String name) {
        var request = HelloRequest.newBuilder().setName(name).build();
        try {
            return futureStub.sayHello(request).get();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to Greet", ex);
            return null;
        }
    }

}
