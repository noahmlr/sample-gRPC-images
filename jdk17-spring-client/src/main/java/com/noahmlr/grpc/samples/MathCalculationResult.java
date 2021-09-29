package com.noahmlr.grpc.samples;

public record MathCalculationResult(String operation, String expression, String result) {
    public MathCalculationReply reply() {
        return MathCalculationReply.newBuilder()
                .setOperation(operation)
                .setExpression(expression)
                .setResult(result)
                .build();
    }
}
