package microchaos.model.ktor

import microchaos.model.Command
import microchaos.support.SystemExitExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.*

class ApplicationShutdownCommandTest {

    @RegisterExtension
    @JvmField
    val systemExitExtension = SystemExitExtension(expectedStatus = 1)

    @Test
    fun `run command`() {
        val shutdownCommand = ApplicationShutdownCommand(Command("applicationShutdown"))
        shutdownCommand.run()
    }
}