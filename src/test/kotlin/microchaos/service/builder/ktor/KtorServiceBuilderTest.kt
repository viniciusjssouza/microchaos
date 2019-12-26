package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.mockk.*
import microchaos.service.builder.EndpointBuilder
import microchaos.service.builder.execution.ExecutionRunner
import microchaos.service.builder.execution.RunnerFactory
import microchaos.service.spec.SampleServices
import microchaos.service.spec.model.Endpoint
import microchaos.service.spec.model.Execution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KtorServiceBuilderTest {

    class EndpointBuilderMock : EndpointBuilder() {
        var calls = 0

        override fun build(runnerFactory: (execution: Execution) -> ExecutionRunner): EndpointBuilder {
            calls++
            return this
        }
    }

    class EndpointMockFactory : (Routing, Endpoint) -> EndpointBuilder {
        val endpoints = ArrayList<Endpoint>()
        val endpointBuilder = EndpointBuilderMock()


        override fun invoke(routing: Routing, endpoint: Endpoint): EndpointBuilder {
            this.endpoints.add(endpoint)
            return endpointBuilder
        }
    }

    private val simpleService = SampleServices.simpleService
    private val mockFactory = EndpointMockFactory()

    @Test
    fun `initialize service on the configured port`() {
        runInApplication {
            assertThat(it.getPort()).isEqualTo(simpleService.service.port)
        }
    }

    @Test
    fun `build endpoints`() {
        runInApplication {
            assertThat(mockFactory.endpointBuilder.calls).isEqualTo(simpleService.service.endpoints.size)
            assertThat(mockFactory.endpoints).isEqualTo(simpleService.service.endpoints)
        }
    }

    private fun runInApplication(testFunc: (builder: KtorServiceBuilder) -> Unit) {
        val builder = KtorServiceBuilder(simpleService.service, mockFactory)
        try {
            builder.start(wait = false)
            testFunc(builder)
        } finally {
            builder.stop()
        }

    }
}