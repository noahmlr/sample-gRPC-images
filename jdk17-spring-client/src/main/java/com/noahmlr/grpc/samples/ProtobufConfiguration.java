package com.noahmlr.grpc.samples;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;


@Configuration
public class ProtobufConfiguration {

    Logger logger = Logger.getLogger(ProtobufConfiguration.class.getName());

    private Channel channel;

    @Value("${protobuf.target.host:localhost}")
    private String host;

    @Value("${protobuf.target.port:50052}")
    private int port;

    @Bean
    public Channel channel() {
        logger.info("Creating channel pointing at " + host + ":" + port);
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        return channel;
    }

    @PreDestroy
    public void destroyChannel() throws Exception {
        logger.info("Destroying channel");
        if (channel instanceof ManagedChannel mc) {
            mc.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
