package microchaos.model.ktor

import microchaos.infra.measureExecutionTime
import microchaos.model.Command
import microchaos.model.Distribution
import microchaos.model.HttpRequest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class KtorCommandTest {

    @Test
    fun `run an "io bounded" command`() {
        val distribution = Distribution("normal", 200.0, 0.0001)
        val spec = Command("ioBounded", duration = distribution)
        val runner = IoBoundedCommand(spec)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(100L))
    }

    @Test
    @RepeatedTest(2)
    fun `run an "cpu bounded" command`() {
        val distribution = Distribution("normal", 500.0, 0.0001)
        val spec = Command("cpuBounded", duration = distribution)
        val runner = CpuBoundedCommand(spec)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(200L))
    }

    @Test
    fun `run an "request" command`() {
        val httpRequest = HttpRequest(method = "get", target = "https://postman-echo.com/get?foo1=bar1&foo2=bar2")
        val spec = Command("request", httpRequest = httpRequest)
        val runner = RequestCommand(spec)
        val response = runner.run()
        assertThat(response).contains("\"foo1\":\"bar1\"")
    }
}