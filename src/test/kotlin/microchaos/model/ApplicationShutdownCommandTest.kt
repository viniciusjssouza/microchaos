package microchaos.model

import microchaos.model.command.ApplicationShutdownCommand
import microchaos.support.SystemExitExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.*

class ApplicationShutdownCommandTest {

    @RegisterExtension
    @JvmField
    val systemExitExtension = SystemExitExtension(expectedStatus = 1)

    @Test
    fun `run command`() {
        val shutdownCommand = ApplicationShutdownCommand()
        shutdownCommand.run()
    }
}