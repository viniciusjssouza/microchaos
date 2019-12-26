package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import microchaos.service.builder.execution.ExecutionRunner
import microchaos.service.spec.SampleServices
import microchaos.service.spec.model.Execution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KtorEndpointBuilderTest {

    class ExecutionRunnerMock(spec: Execution) : ExecutionRunner(spec) {
        var calls = 0

        override fun run() {
            calls++
        }
    }

    class RunnerMockFactory : (Execution) -> ExecutionRunner {
        val executions = ArrayList<Execution>()
        val runners = ArrayList<ExecutionRunnerMock>()


        override fun invoke(execution: Execution): ExecutionRunner {
            this.executions.add(execution)
            val runner =  ExecutionRunnerMock(execution)
            runners.add(runner)
            return runner
        }
    }

    private val ioEndpoint = SampleServices.simpleService.service.endpoints.first { it.path.equals("/some-io") }
    private val requestEndpoint = SampleServices.simpleService.service.endpoints.first { it.path.equals("/google") }
    private val routing: Routing = mockk<Routing>()
    private val mockFactory = RunnerMockFactory()

    @BeforeEach
    fun setupMocks() {
        every { routing.get(path = ioEndpoint.path, body = any()) } returns routing
        every { routing.post(path = requestEndpoint.path, body = any()) } returns routing
    }

    @Test
    fun `build a get endpoint`() {
        val endpointBuilder = GetEndpoint(routing, ioEndpoint);
        endpointBuilder.build(mockFactory)
        assertThat(mockFactory.executions).isEqualTo(ioEndpoint.behavior.execution)
        verify { routing.get(path = ioEndpoint.path, body = any()) }
    }

    @Test
    fun `build a post endpoint`() {
        val endpointBuilder = PostEndpoint(routing, requestEndpoint);
        endpointBuilder.build(mockFactory)
        assertThat(mockFactory.executions).isEqualTo(requestEndpoint.behavior.execution)
        verify { routing.post(path = requestEndpoint.path, body = any()) }
    }
}