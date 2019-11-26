package microchaos.service.builder.ktor

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import microchaos.service.spec.SampleServices
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

internal class KtorServiceBuilderIT {

    companion object {
        private val simpleService = SampleServices.simpleService
        private val client = HttpClient()
        private val builder = KtorServiceBuilder(simpleService)
        private val ioEndpoint = simpleService.service.endpoints.first { it.path.equals("/some-io") }

        @BeforeAll
        @JvmStatic
        fun startApplication() {
            builder.start(false)
        }

        @AfterAll
        @JvmStatic
        fun stopApplication() {
            builder.stop()
            client.close()
        }
    }

    @Test
    fun `test io endpoint`() {
        val responseContent = runBlocking {
            client.get<String>("http://localhost:${simpleService.service.port}${ioEndpoint.path}")
        }
        assertThat(responseContent).isEqualTo(ioEndpoint.path)
    }

}