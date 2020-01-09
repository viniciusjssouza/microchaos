package microchaos.model.ktor

import io.ktor.routing.Routing
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import microchaos.model.SampleServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KtorServiceTest {

    @Test
    fun `initialize service on the configured port`() {
        val simpleService = KtorService(SampleServices.simpleService.service)
        runInApplication(simpleService) {
            assertThat(it.getRuntimePort()).isEqualTo(simpleService.port)
        }
    }

    @Test
    fun `build endpoints`() {
        val mockEndpoints = listOf(mockk<KtorEndpoint>(), mockk<KtorEndpoint>())
        mockEndpoints.forEach {
            every { it.build(any()) } returns it
        }

        val service = KtorService("test", "web", 8080, mockEndpoints)

        runInApplication(service) {
            mockEndpoints.forEach {
                verify { it.build(ofType(Routing::class)) }
            }
        }
    }

    private fun runInApplication(service: KtorService, testFunc: (builder: KtorService) -> Unit) {
        try {
            service.start(wait = false)
            testFunc(service)
        } finally {
            service.stop()
        }

    }
}