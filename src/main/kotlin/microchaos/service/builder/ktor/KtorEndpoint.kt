package microchaos.service.builder.ktor

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import microchaos.service.builder.execution.ExecutionRunner
import microchaos.service.spec.model.Endpoint

abstract class KtorEndpoint(val routing: Routing, val endpoint: Endpoint) {
    companion object {
        private val endpointsTypesByMethods = mapOf(
            "get" to GetEndpoint::class
        )

        fun from(routing: Routing, endpoint: Endpoint): KtorEndpoint {
            val endpointClass = this.endpointsTypesByMethods.getOrElse(endpoint.method, {
                throw IllegalArgumentException("No ktor endpoint found for method ${endpoint.method}")
            })
            return endpointClass.constructors.first().call(routing, endpoint)
        }
    }

    abstract fun build(): KtorEndpoint

}

class GetEndpoint(routing: Routing, endpoint: Endpoint) : KtorEndpoint(routing, endpoint) {

    override fun build(): KtorEndpoint {
        routing.get(this@GetEndpoint.endpoint.path) {
            this@GetEndpoint.endpoint.behavior.execution.forEach {
                ExecutionRunner.fromSpec(it).run()
            }
        }
        return this
    }

}