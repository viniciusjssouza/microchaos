package microchaos.model

import microchaos.infra.measureExecutionTime
import microchaos.model.command.IoBoundCommand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

internal class IoBoundCommandTest {

    @Test
    fun `run an "io bounded" command`() {
        val distribution = Distribution("normal", 200.0, 0.0001)
        val runner = IoBoundCommand(duration = distribution)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(100L))
    }
}