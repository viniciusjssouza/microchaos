package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import microchaos.service.builder.EndpointBuilder
import microchaos.service.builder.execution.ExecutionRunner
import microchaos.service.spec.model.Endpoint
import microchaos.service.spec.model.Execution

abstract class KtorEndpointBuilder(val routing: Routing, val endpoint: Endpoint): EndpointBuilder() {

    override fun build(runnerFactory: (execution: Execution) -> ExecutionRunner): EndpointBuilder {
        this@KtorEndpointBuilder.endpoint.behavior.execution.forEach { execution ->
            val runner = runnerFactory(execution)
            runInsideRoute(runner)
        }
        return this
    }

    abstract fun runInsideRoute(runner: ExecutionRunner)
}

class PostEndpoint(routing: Routing, endpoint: Endpoint) : KtorEndpointBuilder(routing, endpoint) {

    override fun runInsideRoute(runner: ExecutionRunner) {
        routing.post(this@PostEndpoint.endpoint.path) {
            runner.run()
        }
    }
}

class GetEndpoint(routing: Routing, endpoint: Endpoint) : KtorEndpointBuilder(routing, endpoint) {

    override fun runInsideRoute(runner: ExecutionRunner) {
        routing.get(this@GetEndpoint.endpoint.path) {
            runner.run();
        }
    }
}