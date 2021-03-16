package microchaos.model.ktor

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import microchaos.infra.Configuration
import microchaos.infra.logging.loggerFor
import microchaos.infra.slugify
import microchaos.model.Endpoint
import microchaos.model.PeriodicTask
import microchaos.model.Service
import java.util.concurrent.TimeUnit

class KtorService(
    name: String,
    type: String,
    port: Int,
    endpoints: List<Endpoint> = arrayListOf(),
    periodicTasks: List<PeriodicTask> = arrayListOf()
) : Service(name, type, port, endpoints, periodicTasks) {

    companion object {
        private val logger = loggerFor<KtorService>()
    }

    constructor(service: Service):
            this(service.name, service.type, service.port, service.endpoints, service.periodicTasks)

    private val server: ApplicationEngine

    init {
        this.server = embeddedServer(Netty, port = this.port) {
            this.routing {
                setRoutesForEndpoints(this, endpoints)

                // Expose the paths also under /<service name>
                // We need to this because GKE ingress does not supports URL rewrite
                this.route(Configuration.serviceName?.slugify() ?: "") {
                    setRoutesForEndpoints(this, endpoints)
                }
                // health check page. TODO improve it
                get("/healthz") {
                    call.respondText("success", ContentType.Text.Html)
                }
                // GKE ingress requires a root index
                get("/") {
                    call.respondText("microchaos", ContentType.Text.Html)
                }
            }
        }
    }

    private fun setRoutesForEndpoints(route: Route, endpoints: List<Endpoint>) {
        endpoints.forEach { endpoint ->
            val ktorEndpoint = if (endpoint is KtorEndpoint) endpoint else KtorEndpoint.from(endpoint)
            ktorEndpoint.build(route)
        }
    }

    override fun start(): ApplicationEngine {
        logger.info("Starting Ktor service on port ${this.port} with name '${Configuration.serviceName}'")
        return this.server.start()
    }

    override fun stop() {
        this.server.stop(100, 500, TimeUnit.MILLISECONDS)
    }

    fun getRuntimePort(): Int {
        return this.server.environment.connectors.first().port
    }

}
