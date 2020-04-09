package microchaos.model.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import microchaos.infra.logging.loggerFor
import microchaos.model.Behavior
import microchaos.model.Endpoint

abstract class KtorEndpoint(
    path: String,
    method: String,
    behavior: Behavior
) : Endpoint(path, method, behavior) {

    companion object {

        val logger = loggerFor<KtorEndpoint>()

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

    constructor(endpoint: Endpoint) : this(endpoint.path, endpoint.method, endpoint.behavior)

    suspend fun handleRequest(call: ApplicationCall) {
        behavior.commands.forEach { command -> command.run() }
        val responseModel = this.behavior.drawResponse()
        if (responseModel.isPresent) {
            val statusCode = HttpStatusCode.fromValue(responseModel.get().status)
            call.respondText(responseModel.get().content, ContentType.Text.Plain, statusCode)
        }
    }

    fun build(routing: Routing): KtorEndpoint {
        setupEndpoint(routing, this::handleRequest)
        return this
    }

    abstract fun setupEndpoint(routing: Routing,  action: suspend (ApplicationCall) -> Unit)
}

class PostEndpoint(endpoint: Endpoint) : KtorEndpoint(endpoint) {

    override fun setupEndpoint(routing: Routing, action: suspend (ApplicationCall) -> Unit) {
        logger.info("Mapping POST endpoint for path '$path'")
        routing.post(this@PostEndpoint.path) {
            action(call)
        }
    }
}

class GetEndpoint(endpoint: Endpoint) : KtorEndpoint(endpoint) {

    override fun setupEndpoint(routing: Routing, action: suspend (ApplicationCall) -> Unit) {
        logger.info("Mapping GET endpoint for path '$path'")
        routing.get(this@GetEndpoint.path) {
            action(call)
        }
    }
}