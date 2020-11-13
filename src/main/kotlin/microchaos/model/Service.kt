package microchaos.model

open class Service (
    val name: String,
    val type: String,
    val port: Int,
    val endpoints: List<Endpoint> = arrayListOf(),
    val periodicTasks: List<PeriodicTask> = arrayListOf()
) {

    open fun start(): Any = throw NotImplementedError("Only service implementations can be started")

    open fun stop(): Unit = throw NotImplementedError("Only service implementations can be started")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Service

        if (name != other.name) return false
        if (type != other.type) return false
        if (port != other.port) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + port
        return result
    }


    override fun toString(): String {
        return "Service(name='$name', type='$type', port=$port)"
    }
}
