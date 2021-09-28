package com.noahmlr.grpc.samples;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewtonMathServer {
    private static final Logger logger = Logger.getLogger(NewtonMathServer.class.getName());
    private static final String API_URL = "https://newton.vercel.app/api/v2";

    private final int port;
    private final HttpClient client;

    private Server server;

    public NewtonMathServer(int port) {
        this.port = port;
        this.client = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build();
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(this.port)
                .addService(new NewtonMathImpl())
                .addService(ProtoReflectionService.newInstance())
                .build()
                .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                NewtonMathServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    public void blockUntilShutDown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    class NewtonMathImpl extends NewtonMathGrpc.NewtonMathImplBase {
        @Override
        public void performOperation(MathCalculationRequest request, StreamObserver<MathCalculationReply> responseObserver) {
            try {
                var httpRequest = HttpRequest.newBuilder()
                        .uri(new URI(String.join("/", API_URL,
                                request.getOperation(),
                                URLEncoder.encode(request.getExpression(), StandardCharsets.UTF_8)))
                        )
                        .GET()
                        .build();
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                var reply = new ObjectMapper().readValue(response.body(), MathCalculationResult.class).reply();
                responseObserver.onNext(reply);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error hitting Newton API", ex);
                responseObserver.onError(ex);
            }
            responseObserver.onCompleted();
        }
    }


}
