package microchaos.model.ktor

import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import microchaos.model.Behavior
import microchaos.model.Endpoint

abstract class KtorEndpoint(
    path: String,
    method: String,
    behavior: Behavior
) : Endpoint(path, method, behavior) {

    companion object {
        private val endpointsFactories = mapOf(
            "get" to { endpoint: Endpoint -> GetEndpoint(endpoint) },
            "post" to { endpoint: Endpoint -> PostEndpoint(endpoint) }
        )

        fun from(endpoint: Endpoint): KtorEndpoint {
            val endpointFactoryFunc = this.endpointsFactories.getOrElse(endpoint.method, {
                throw IllegalArgumentException("No ktor endpoint found for method ${endpoint.method}")
            })
            return endpointFactoryFunc(endpoint)
        }
    }

    val requestRunnerFn = {
        behavior.commands.forEach { command -> command.run() }
    }

    constructor(endpoint: Endpoint) : this(endpoint.path, endpoint.method, endpoint.behavior)

    fun build(routing: Routing): KtorEndpoint {
        onRequest(routing, this.requestRunnerFn)
        return this
    }

    abstract fun onRequest(routing: Routing, action: () -> Unit)
}

class PostEndpoint(endpoint: Endpoint) : KtorEndpoint(endpoint) {

    override fun onRequest(routing: Routing, action: () -> Unit) {
        routing.post(this@PostEndpoint.path) {
            action()
        }
    }
}

class GetEndpoint(endpoint: Endpoint) : KtorEndpoint(endpoint) {

    override fun onRequest(routing: Routing, action: () -> Unit) {
        routing.get(this@GetEndpoint.path) {
            action()
        }
    }
}