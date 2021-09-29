package com.noahmlr.grpc.samples;

import io.grpc.Channel;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class NewtonMathClient {
    Logger logger = Logger.getLogger(NewtonMathClient.class.getName());

    private final NewtonMathGrpc.NewtonMathFutureStub futureStub;

    public NewtonMathClient(Channel channel) {
        futureStub = NewtonMathGrpc.newFutureStub(channel);
    }

    public MathCalculationReply performCalculation(MathCalculationRequest request) {
        try {
            return futureStub.performOperation(request).get();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to perform calculation", ex);
            throw new RuntimeException(ex);
        }
    }

}
