package com.noahmlr.grpc.samples;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewtonMathController {

    private final NewtonMathClient newtonMathClient;

    public NewtonMathController(NewtonMathClient newtonMathClient) {
        this.newtonMathClient = newtonMathClient;
    }

    @PostMapping("/calculate")
    private MathCalculationResult performCalculation(@RequestBody MathCalculation calculation) {
        var request = calculation.request();
        var reply = newtonMathClient.performCalculation(request);
        return new MathCalculationResult(reply.getOperation(), reply.getExpression(), reply.getResult());

    }

}
