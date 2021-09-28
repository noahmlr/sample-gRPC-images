package com.noahmlr.grpc.samples;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 50052;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }

        var server = new NewtonMathServer(port);
        server.start();
        server.blockUntilShutDown();
    }
}
