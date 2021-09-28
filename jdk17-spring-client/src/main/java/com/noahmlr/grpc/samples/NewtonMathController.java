package com.noahmlr.grpc.samples;

import org.springframework.web.bind.annotation.*;

@RestController
public class NewtonMathController {

    private final NewtonMathClient newtonMathClient;

    public NewtonMathController(NewtonMathClient newtonMathClient) {
        this.newtonMathClient = newtonMathClient;
    }

    @PostMapping("/calculate")
    private MathCalculationResult performCalculation(@RequestBody MathCalculation calculation) {
        var reply = newtonMathClient.performCalculation(calculation.request());
        return new MathCalculationResult(reply.getOperation(), reply.getExpression(), reply.getResult());
    }

}
