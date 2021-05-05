package microchaos.model

import microchaos.infra.HealthCheck
import microchaos.model.command.ApplicationShutdownCommand
import microchaos.support.SystemExitExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.*

class ApplicationShutdownCommandTest {

    @RegisterExtension
    @JvmField
    val systemExitExtension = SystemExitExtension(expectedStatus = 1)

    @Test
    fun `run command`() {
        val shutdownCommand = ApplicationShutdownCommand()
        shutdownCommand.waitTimeMillis = 500L
        shutdownCommand.run()
        // we need to wait until th the shutdown command call exit
        Thread.sleep(800)
        assertThat(HealthCheck.isOk).isFalse()
    }
}