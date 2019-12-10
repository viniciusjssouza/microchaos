package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.ktor.routing.get
import io.mockk.*
import microchaos.service.spec.SampleServices
import microchaos.service.spec.model.Behavior
import microchaos.service.spec.model.Endpoint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

internal class KtorEndpointTest {

    private val ioEndpoint = SampleServices.simpleService.service.endpoints.first { it.path.equals("/some-io") }
    private val routing: Routing = mockk<Routing>()

    @Test
    fun `unsupported http method`() {
        val endpoint = Endpoint(method = "makeCake", path = "/test", behavior = Behavior(emptyList()))
        assertThatIllegalArgumentException().isThrownBy { KtorEndpoint.from(routing, endpoint) }
    }

    @Test
    fun `return a get endpoint`() {
        every { routing.get(path = ioEndpoint.path, body = any()) } returns routing
        val endpoint = KtorEndpoint.from(routing, ioEndpoint).build()

        assertThat(endpoint::class).isEqualTo(GetEndpoint::class)
        verify { routing.get(ioEndpoint.path, body = any()) }
    }

}