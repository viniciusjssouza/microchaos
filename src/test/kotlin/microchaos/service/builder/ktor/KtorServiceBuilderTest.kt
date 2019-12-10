package microchaos.service.builder.ktor

import io.ktor.application.Application
import io.ktor.routing.Routing
import io.ktor.server.engine.ApplicationEngineFactory
import io.ktor.server.engine.embeddedServer
import io.mockk.*
import microchaos.service.spec.SampleServices
import microchaos.service.spec.model.Endpoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import kotlin.test.expect

internal class KtorServiceBuilderTest {

    private val simpleService = SampleServices.simpleService

    @AfterEach
    fun unmock() {
        unmockkAll()
    }

    @Test
    fun `initialize service on the configured port`() {
        val ktorEndpoint = mockkClass(GetEndpoint::class)
        every { KtorEndpoint.from(ofType(Routing::class), ofType(Endpoint::class)) } returns ktorEndpoint
        every { ktorEndpoint.build() } returns ktorEndpoint
        runInApplication {
            assertThat(it.getPort()).isEqualTo(simpleService.service.port)
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