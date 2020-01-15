package microchaos.model.ktor

import io.mockk.*
import microchaos.infra.Environment
import microchaos.infra.network.NetworkInterface
import microchaos.model.Command
import microchaos.model.Distribution
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LinuxNetworkFailureCommandTest {

    private val environment = mockk<Environment>()
    private val networkInterface = mockk<NetworkInterface>()
    private val commandSpec = Command("test", Distribution("constant", 1000.0))
    private val command = LinuxNetworkFailureCommand(commandSpec, environment, networkInterface)

    @Test
    fun run() {
        every { environment.isLinuxOS() } returns true
        every { networkInterface.disable() } just runs
        every { networkInterface.enable() } just runs

        Thread {
            command.run()
        }.start()

        Thread.sleep(100)
        verify { networkInterface.disable() }
        Thread.sleep(250)
        verify(exactly = 0) { networkInterface.enable() }
        Thread.sleep(1000)
        verify(exactly = 1) { networkInterface.enable() }
    }

    @Test
    fun `when the system is not linux`() {
        every { environment.isLinuxOS() } returns false
        Assertions.assertThrows(UnsupportedOperationException::class.java) { command.run() }
    }
}