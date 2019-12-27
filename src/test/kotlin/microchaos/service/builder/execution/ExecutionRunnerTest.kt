package microchaos.service.builder.execution

import microchaos.infra.measureExecutionTime
import microchaos.service.spec.model.Distribution
import microchaos.service.spec.model.Execution
import microchaos.service.spec.model.HttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

internal class ExecutionRunnerTest {

    @Test
    fun `run an "io bounded" execution`() {
        val distribution = Distribution("normal", 200.0, 0.0001)
        val spec = Execution("ioBounded", duration = distribution)
        val runner = IoBoundedRunner(spec)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(50L))
    }

    @Test
    fun `run an "cpu bounded" execution`() {
        val distribution = Distribution("normal", 20000.0, 0.0001)
        val spec = Execution("cpuBounded", duration = distribution)
        val runner = CpuBoundedRunner(spec)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(100L))
    }

    @Test
    fun `run an "request" execution`() {
        val httpRequest = HttpRequest(method = "get", target = "https://postman-echo.com/get?foo1=bar1&foo2=bar2")
        val spec = Execution("request", httpRequest = httpRequest)
        val runner = RequestRunner(spec)
        val response = runner.run()
        assertThat(response).contains("\"foo1\":\"bar1\"")
    }
}