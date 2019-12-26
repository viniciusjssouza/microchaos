package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import microchaos.service.builder.EndpointBuilder
import microchaos.service.spec.model.Endpoint

class KtorEndpointFactory : (Routing, Endpoint) -> EndpointBuilder {

    private val endpointsTypesByMethods = mapOf(
        "get" to { routing: Routing, endpoint: Endpoint -> GetEndpoint(routing, endpoint) },
        "post" to { routing: Routing, endpoint: Endpoint -> PostEndpoint(routing, endpoint) }
    )

    override fun invoke(routing: Routing, endpoint: Endpoint): EndpointBuilder {
        val endpointFactoryFunc = this.endpointsTypesByMethods.getOrElse(endpoint.method, {
            throw IllegalArgumentException("No ktor endpoint found for method ${endpoint.method}")
        })
        return endpointFactoryFunc(routing, endpoint)
    }
}