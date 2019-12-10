package microchaos.service.builder.execution

import microchaos.service.spec.model.Execution
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.Test

internal class ExecutionRunnerTest {

    @Test
    fun `unsupported runner type`() {
        val spec = Execution("unknown")
        assertThatIllegalArgumentException().isThrownBy { ExecutionRunner.fromSpec(spec) }
    }

    @Test
    fun `run an "io bounded" execution`() {
        //val spec = Execution("ioBounded", distribution = Distibution("logNormal"))
    }

}