package microchaos.service.builder.execution

import microchaos.service.spec.model.Distribution
import microchaos.service.spec.model.Execution
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class RunnerFactoryTest {

    private val factory = RunnerFactory()

    @Test
    fun `build an io bounded runner`() {
        val execution = Execution("ioBounded", Distribution("logNormal", 1.0, 1.0))
        val runner = factory.invoke(execution)
        assertThat(runner).isInstanceOf(IoBoundedRunner::class.java)
    }

    @Test
    fun `build an cpu bounded runner`() {
        val execution = Execution("cpuBounded", Distribution("logNormal", 1.0, 1.0))
        val runner = factory.invoke(execution)
        assertThat(runner).isInstanceOf(CpuBoundedRunner::class.java)
    }
}