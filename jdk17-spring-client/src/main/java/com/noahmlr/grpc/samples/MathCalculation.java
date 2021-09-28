package com.noahmlr.grpc.samples;

public record MathCalculation(String operation, String expression) {
    public MathCalculationRequest request() {
        return MathCalculationRequest.newBuilder()
                .setOperation(operation)
                .setExpression(expression)
                .build();
    }
}
