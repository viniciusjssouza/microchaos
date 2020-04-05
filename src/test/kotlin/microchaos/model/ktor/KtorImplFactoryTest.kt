package microchaos.model.ktor

import microchaos.model.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource


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

    @Test
    fun `build a network failure command`() {
        val command = Command("networkFailure")
        val ktorCommand = implFactory.getCommandImpl(command)
        assertThat(ktorCommand).isInstanceOf(LinuxNetworkFailureCommand::class.java)
    }

    @Test
    fun `build an application shutdown command`() {
        val command = Command("applicationShutdown")
        val ktorCommand = implFactory.getCommandImpl(command)
        assertThat(ktorCommand).isInstanceOf(ApplicationShutdownCommand::class.java)
    }

    @ParameterizedTest
    @ValueSource(classes= [Service::class, Endpoint::class, Command::class])
    fun `map model classes`(modelClass: Class<*>) {
        assertThat(implFactory.getMapper(modelClass)).isNotNull
    }
}