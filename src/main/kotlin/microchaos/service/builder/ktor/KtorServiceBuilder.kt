package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import microchaos.service.builder.EndpointBuilder
import microchaos.service.builder.ServiceBuilder
import microchaos.service.builder.execution.RunnerFactory
import microchaos.service.spec.model.Endpoint
import microchaos.service.spec.model.Service
import java.util.concurrent.TimeUnit

class KtorServiceBuilder(
    service: Service,
    endpointFactory: (routing: Routing, endpointSpec: Endpoint)-> EndpointBuilder
): ServiceBuilder(service) {

    private val server: ApplicationEngine

    init {
        this.server = embeddedServer(Netty, port = service.port) {
            this.routing {
                service.endpoints.forEach { endpoint ->
                    val endpointBuilder = endpointFactory(this, endpoint)
                    endpointBuilder.build(RunnerFactory())
                }
            }
        }
    }

    override fun start(wait: Boolean): ApplicationEngine {
        return this.server.start(wait)
    }

    override fun stop() {
        return this.server.stop(100, 500, TimeUnit.MILLISECONDS)
    }

    override fun getPort(): Int {
        return this.server.environment.connectors.first().port
    }
}
