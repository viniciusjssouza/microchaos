package microchaos.service.builder.execution

import microchaos.service.spec.model.Execution

enum class ExecutionStatus {
    WAITING, RUNNING, FINISHED
}

abstract class ExecutionRunner(spec: Execution) {

    var status: ExecutionStatus = ExecutionStatus.WAITING

    companion object {
        private val executionByTypes = mapOf(
            "ioBounded" to IoBoundedRunner::class
        )

        fun fromSpec(spec: Execution): ExecutionRunner {
            val runnerClass = this.executionByTypes.getOrElse(spec.type, {
                throw IllegalArgumentException("No runner found for method ${spec.type}")
            })
            return runnerClass.constructors.first().call(spec)
        }
    }

    abstract fun run()
}

class IoBoundedRunner(spec: Execution) : ExecutionRunner(spec) {

    override fun run() {
        this.status = ExecutionStatus.RUNNING
        this.status = ExecutionStatus.FINISHED
    }
}
