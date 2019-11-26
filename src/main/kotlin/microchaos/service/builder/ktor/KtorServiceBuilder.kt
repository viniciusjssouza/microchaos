package microchaos.service.builder.ktor

import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import microchaos.service.spec.model.ServiceSpec
import java.util.concurrent.TimeUnit

class KtorServiceBuilder(private val serviceSpec: ServiceSpec) {

    val server: ApplicationEngine;

    init {
        this.server = embeddedServer(Netty, port = serviceSpec.service.port) {
            serviceSpec.service.endpoints.forEach { endpoint ->
                KtorEndpoint.build(this, endpoint)
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
