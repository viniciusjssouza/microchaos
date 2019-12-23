package microchaos.service.builder.execution

import microchaos.service.spec.model.Execution

class RunnerFactory : (Execution) -> ExecutionRunner {
    companion object {
        private val executionsByTypes = mapOf(
            "ioBounded" to { execution: Execution -> IoBoundedRunner(execution) },
            "cpuBounded" to { execution: Execution -> CpuBoundedRunner(execution) },
            "request" to { execution: Execution -> RequestRunner(execution) }
        )
    }

    override fun invoke(spec: Execution): ExecutionRunner {
        val runnerFactory = executionsByTypes.getOrElse(spec.type, {
            throw IllegalArgumentException("No runner found for method ${spec.type}")
        })
        return runnerFactory(spec)
    }
}