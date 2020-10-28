package microchaos.model.ktor

import microchaos.model.*
import microchaos.model.command.Command
import microchaos.model.command.CpuBoundCommand
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


internal class KtorImplFactoryTest {

    private val implFactory = KtorImplFactory()

    @Test
    fun `build a get endpoint`() {
        val endpoint = Endpoint("/test", "get", Behavior(someCommands()))

        val ktorEndpoint = implFactory.getEndpointImpl(endpoint)
        assertThat(ktorEndpoint).isInstanceOf(GetEndpoint::class.java)
    }

    @Test
    fun `build a post endpoint`() {
        val endpoint = Endpoint("/test", "post", Behavior(someCommands()))

        val ktorEndpoint = implFactory.getEndpointImpl(endpoint)
        assertThat(ktorEndpoint).isInstanceOf(PostEndpoint::class.java)
    }

    @ParameterizedTest
    @ValueSource(classes= [Service::class, Endpoint::class])
    fun `map model classes`(modelClass: Class<*>) {
        assertThat(implFactory.getMapper(modelClass)).isNotNull
    }

    private fun someCommands(): List<Command> {
        val distribution = Distribution("normal", 500.0, 0.0001)
        return listOf(CpuBoundCommand(duration = distribution))
    }
}