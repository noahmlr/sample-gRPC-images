package com.noahmlr.grpc.samples;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.Http;
import io.grpc.Channel;
import org.springframework.stereotype.Service;

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

@Service
public class NewtonMathClient {
    Logger logger = Logger.getLogger(NewtonMathClient.class.getName());

    private final NewtonMathGrpc.NewtonMathFutureStub futureStub;
    private final HttpClient httpClient;

    public NewtonMathClient(Channel channel) {
        httpClient = HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build();
        futureStub = NewtonMathGrpc.newFutureStub(channel);
    }

    public MathCalculationReply performCalculation(MathCalculationRequest request) {
        try {
            return futureStub.performOperation(request).get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to perform calculation", ex);
            return fallback(request);
        }
    }

    // USED FOR TESTING PURPOSES IF GRPC IS DOWN / NOT WORKING
    public MathCalculationReply fallback(MathCalculationRequest request) {
        try {
            var httpRequest = HttpRequest.newBuilder()
                    .uri(new URI(String.join("/", "https://newton.vercel.app/api/v2",
                            request.getOperation(),
                            URLEncoder.encode(request.getExpression(), StandardCharsets.UTF_8)))
                    )
                    .GET()
                    .build();
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return new ObjectMapper().readValue(response.body(), MathCalculationResult.class).reply();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to fall back", ex);
            throw new RuntimeException(ex);
        }
    }

}
