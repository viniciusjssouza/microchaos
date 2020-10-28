package microchaos.model

import microchaos.infra.measureExecutionTime
import microchaos.model.command.CpuBoundCommand
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class CpuBoundCommandTest {
    @Test
    @RepeatedTest(2)
    fun `run an "cpu bounded" command`() {
        val distribution = Distribution("normal", 500.0, 0.0001)
        val runner = CpuBoundCommand(duration = distribution)
        val execTime = measureExecutionTime {
            runner.run()
        }
        assertThat(execTime).isCloseTo(distribution.mean.toLong(), Offset.offset(200L))
    }
}