use tonic::{transport::Server, Request, Response, Status};

use newton_math::newton_math_server::{NewtonMath, NewtonMathServer};
use newton_math::{MathCalculationRequest, MathCalculationReply};

pub mod newton_math {
    tonic::include_proto!("newtonmath");
}

#[derive(Debug, Default)]
pub struct MyNewtonMath {}

#[tonic::async_trait]
impl NewtonMath for MyNewtonMath {
    async fn perform_operation(
        &self,
        request: Request<MathCalculationRequest>,
    ) -> Result<Response<MathCalculationReply>, Status> {
        println!("Got a request: {:?}", request);

        let request_data = request.into_inner();
        let operation = request_data.operation;
        let expression = request_data.expression;

        let reply = newton_math::MathCalculationReply {
            operation,
            expression,
            result: String::from("Placeholder"),
        };

        Ok(Response::new(reply))
    }
}

#[tokio::main]
async fn main() -> Result<(), Box<dyn std::error::Error>> {
    let addr = "[::1]:50052".parse()?;
    let service = MyNewtonMath::default();

    Server::builder()
        .add_service(NewtonMathServer::new(service))
        .serve(addr)
        .await?;

    Ok(())
}
