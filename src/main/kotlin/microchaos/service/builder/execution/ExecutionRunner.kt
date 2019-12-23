package microchaos.service.builder.execution

import microchaos.service.spec.model.Execution

enum class ExecutionStatus {
    WAITING, RUNNING, FINISHED
}

abstract class ExecutionRunner(val spec: Execution) {

    var status: ExecutionStatus = ExecutionStatus.WAITING

    abstract fun run()
}

class IoBoundedRunner(spec: Execution) : ExecutionRunner(spec) {

    override fun run() {
        this.status = ExecutionStatus.RUNNING
        this.status = ExecutionStatus.FINISHED
    }
}

class CpuBoundedRunner(spec: Execution) : ExecutionRunner(spec) {

    override fun run() {
        this.status = ExecutionStatus.RUNNING
        this.status = ExecutionStatus.FINISHED
    }
}

class RequestRunner(spec: Execution) : ExecutionRunner(spec) {

    override fun run() {
        this.status = ExecutionStatus.RUNNING
        this.status = ExecutionStatus.FINISHED
    }
}