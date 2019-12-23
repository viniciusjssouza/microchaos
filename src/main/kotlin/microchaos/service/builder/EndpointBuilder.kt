package microchaos.service.builder

import microchaos.service.builder.execution.ExecutionRunner
import microchaos.service.spec.model.Execution

abstract class EndpointBuilder {
    abstract fun build(runnerFactory : (execution: Execution) -> ExecutionRunner): EndpointBuilder;
}