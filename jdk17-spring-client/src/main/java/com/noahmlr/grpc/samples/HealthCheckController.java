package com.noahmlr.grpc.samples;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    private final NewtonMathClient newtonMathClient;

    public HealthCheckController(NewtonMathClient newtonMathClient) {
        this.newtonMathClient = newtonMathClient;
    }

    @GetMapping("/health")
    public HealthResponse getHealth() {
        try {
            var sampleRequest = MathCalculationRequest.newBuilder()
                    .setOperation("simplify")
                    .setExpression("2(2)")
                    .build();
            newtonMathClient.performCalculation(sampleRequest);
            return new HealthResponse(Status.STABLE);
        } catch (Exception ex) {
            return new HealthResponse(Status.UNSTABLE);
        }
    }

    private enum Status {
        STABLE, UNSTABLE;
    }

    private record HealthResponse(Status status) {}
}
