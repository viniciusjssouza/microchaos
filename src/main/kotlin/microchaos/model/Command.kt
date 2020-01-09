package microchaos.model

open class Command(
    val type: String,
    val duration: Distribution? = null,
    val httpRequest: HttpRequest? = null
) {

    open fun run(): Any = throw NotImplementedError()

    protected fun generateDistributionSample(): Double {
        return this.duration?.sample() ?:
            throw IllegalArgumentException("Every ${this::class.simpleName} must contain a distribution")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (type != other.type) return false
        if (duration != other.duration) return false
        if (httpRequest != other.httpRequest) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + (duration?.hashCode() ?: 0)
        result = 31 * result + (httpRequest?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Command(type='$type', duration=$duration, httpRequest=$httpRequest)"
    }
}
