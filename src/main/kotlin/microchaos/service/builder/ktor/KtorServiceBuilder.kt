package microchaos.service.builder.ktor

import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import microchaos.service.spec.model.ServiceSpec
import java.util.concurrent.TimeUnit

class KtorServiceBuilder(private val serviceSpec: ServiceSpec) {

    private val server: ApplicationEngine

    init {
        this.server = embeddedServer(Netty, port = serviceSpec.service.port) {
            this.routing {
                serviceSpec.service.endpoints.forEach { endpoint ->
                    KtorEndpoint.from(this, endpoint).build()
                }
            }
        }
    }

    fun start(wait: Boolean = true): ApplicationEngine {
        return this.server.start(wait)
    }

    fun stop() {
        return this.server.stop(100, 500, TimeUnit.MILLISECONDS)
    }

}
