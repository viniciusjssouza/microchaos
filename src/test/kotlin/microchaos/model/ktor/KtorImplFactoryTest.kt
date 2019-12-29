package microchaos.model.ktor

import microchaos.model.Behavior
import microchaos.model.Command
import microchaos.model.Distribution
import microchaos.model.Endpoint
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class KtorImplFactoryTest {

    private val implFactory = KtorImplFactory()

    @Test
    fun `build a get endpoint`() {
        val endpoint = Endpoint("/test", "get", Behavior(listOf(Command("ioBounded"))))

        val ktorEndpoint = implFactory.getEndpointImpl(endpoint)
        assertThat(ktorEndpoint).isInstanceOf(GetEndpoint::class.java)
    }

    @Test
    fun `build a post endpoint`() {
        val endpoint = Endpoint("/test", "post", Behavior(listOf(Command("ioBounded"))))

        val ktorEndpoint = implFactory.getEndpointImpl(endpoint)
        assertThat(ktorEndpoint).isInstanceOf(PostEndpoint::class.java)
    }

    @Test
    fun `build an io bounded command`() {
        val command = Command("ioBounded", Distribution("logNormal", 1.0, 1.0))
        val ktorCommand = implFactory.getCommandImpl(command)
        assertThat(ktorCommand).isInstanceOf(IoBoundedCommand::class.java)
    }

    @Test
    fun `build a cpu bounded command`() {
        val command = Command("cpuBounded", Distribution("logNormal", 1.0, 1.0))
        val ktorCommand = implFactory.getCommandImpl(command)
        assertThat(ktorCommand).isInstanceOf(CpuBoundedCommand::class.java)
    }

    @Test
    fun `build a request command`() {
        val command = Command("request", Distribution("logNormal", 1.0, 1.0))
        val ktorCommand = implFactory.getCommandImpl(command)
        assertThat(ktorCommand).isInstanceOf(RequestCommand::class.java)
    }
}