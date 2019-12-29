package microchaos.model.ktor

import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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

    constructor(service: Service):
            this(service.name, service.type, service.port, service.endpoints, service.periodicTasks)

    private val server: ApplicationEngine

    init {
        this.server = embeddedServer(Netty, port = this.port) {
            this.routing {
                endpoints.forEach { endpoint ->
                    val ktorEndpoint = if (endpoint is KtorEndpoint) endpoint else KtorEndpoint.from(endpoint)
                    ktorEndpoint.build(this)
                }
            }
        }
    }

    fun start(wait: Boolean): ApplicationEngine {
        return this.server.start(wait)
    }

    fun stop() {
        return this.server.stop(100, 500, TimeUnit.MILLISECONDS)
    }

    fun getRuntimePort(): Int {
        return this.server.environment.connectors.first().port
    }

}
