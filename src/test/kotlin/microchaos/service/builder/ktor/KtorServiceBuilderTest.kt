package microchaos.service.builder.ktor

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.mockk.*
import microchaos.service.spec.SampleServices
import microchaos.service.spec.model.Endpoint
import org.junit.jupiter.api.Test

internal class KtorServiceBuilderTest {

    private val simpleService = SampleServices.simpleService

    @Test
    fun `initialize service on the configured port`() {
        mockkStatic("io.ktor.server.engine.EmbeddedServerKt")
        runInApplication {
            verify {
                embeddedServer(ofType(ApplicationEngineFactory::class), simpleService.service.port, module = any())
            }
        }
    }

    @Test
    fun `build endpoints`() {
        mockkObject(KtorEndpoint.Companion)
        val ktorEndpoint = mockkClass(GetEndpoint::class)
        every { KtorEndpoint.from(ofType(Routing::class), ofType(Endpoint::class)) } returns ktorEndpoint
        every { ktorEndpoint.build() } returns ktorEndpoint

        runInApplication {
            simpleService.service.endpoints.forEach { endpoint ->
                verify { KtorEndpoint.from(ofType(Routing::class), endpoint) }
            }
            verify(exactly = simpleService.service.endpoints.size) { ktorEndpoint.build() }
        }
    }

    private fun runInApplication(testFunc: (builder: KtorServiceBuilder) -> Unit) {
        val builder = KtorServiceBuilder(simpleService)
        try {
            builder.start(wait = false)
            testFunc(builder)
        } finally {
            builder.stop()
        }

    }
}