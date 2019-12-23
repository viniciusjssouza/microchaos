package microchaos.service.builder.ktor

import io.ktor.routing.Routing
import io.mockk.mockk
import microchaos.service.spec.model.Behavior
import microchaos.service.spec.model.Endpoint
import microchaos.service.spec.model.Execution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KtorEndpointFactoryTest {

    private val factory = KtorEndpointFactory()
    private val routing: Routing = mockk<Routing>()

    @Test
    fun `build a get endpoint`() {
        val endpoint = Endpoint("/test", "get", Behavior(listOf(Execution("ioBounded"))))

        val endpointBuilder = factory.invoke(routing, endpoint)
        assertThat(endpointBuilder).isInstanceOf(GetEndpoint::class.java)
    }

    @Test
    fun `build a post endpoint`() {
        val endpoint = Endpoint("/test", "post", Behavior(listOf(Execution("ioBounded"))))

        val endpointBuilder = factory.invoke(routing, endpoint)
        assertThat(endpointBuilder).isInstanceOf(PostEndpoint::class.java)
    }
}