package microchaos.model

import microchaos.model.command.RequestCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RequestCommandTest {
    @Test
    fun `run an "request" command`() {
        val httpRequest = HttpRequest(method = "get", target = "https://postman-echo.com/get?foo1=bar1&foo2=bar2")
        val runner = RequestCommand(httpRequest = httpRequest)
        val response = runner.run()
        assertThat(response).contains("\"foo1\":\"bar1\"")
    }
}