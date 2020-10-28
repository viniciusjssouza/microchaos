package microchaos.model.ktor

import io.ktor.application.ApplicationCall
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.mockk.*
import kotlinx.coroutines.runBlocking
import microchaos.model.SampleServices
import microchaos.model.Behavior
import microchaos.model.Endpoint
import microchaos.model.command.Command
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class KtorEndpointTest {

    private val ioEndpoint = SampleServices.simpleService.service.endpoints.first { it.path == "/some-io" }
    private val requestEndpoint = SampleServices.simpleService.service.endpoints.first { it.path == "/google" }
    private val routing: Routing = mockk<Routing>()

    @BeforeEach
    fun setupMocks() {
        every { routing.get(path = ioEndpoint.path, body = any()) } returns routing
        every { routing.post(path = requestEndpoint.path, body = any()) } returns routing
    }

    @Test
    fun `build a get endpoint`() {
        val originalEndpoint = GetEndpoint(ioEndpoint)
        val endpoint = spyk(originalEndpoint)
        endpoint.build(routing)

        verify { endpoint.setupEndpoint(routing, any()) }
        verify { routing.get(path = ioEndpoint.path, body = any()) }
    }

    @Test
    fun `build a post endpoint`() {
        val originalEndpoint = PostEndpoint(requestEndpoint)
        val endpoint = spyk(originalEndpoint)
        endpoint.build(routing)

        verify { endpoint.setupEndpoint(routing, any()) }
        verify { routing.post(path = requestEndpoint.path, body = any()) }
    }

    @Test
    fun `run all the commands`() {
        val executions = Array(3) { mockk<Command>() }.toList()
        val appCall = mockk<ApplicationCall>()
        executions.forEach { every { it.run() } returns it }
        val endpoint = PostEndpoint(Endpoint("/test", "get", behavior = Behavior(executions)))
        runBlocking {
            endpoint.handleRequest(appCall)
        }
        Thread.sleep(50)
        executions.forEach { verify { it.run() } }
    }
}