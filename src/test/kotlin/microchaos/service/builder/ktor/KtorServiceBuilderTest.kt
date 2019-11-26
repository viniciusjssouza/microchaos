package microchaos.service.builder.ktor

import io.ktor.application.Application
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import microchaos.service.spec.SampleServices
import org.junit.jupiter.api.Test
import kotlin.reflect.typeOf

internal class KtorServiceBuilderTest {

    private val simpleService = SampleServices.simpleService
    private val ioEndpoint = simpleService.service.endpoints.first { it.path.equals("/some-io") }

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
        mockkObject(KtorEndpoint)

        runInApplication {
            simpleService.service.endpoints.forEach { endpoint ->
                verify { KtorEndpoint.build(ofType(Application::class), endpoint) }
            }
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