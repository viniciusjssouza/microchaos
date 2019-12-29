package microchaos.model

open class Endpoint(
    val path: String,
    val method: String,
    val behavior: Behavior
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Endpoint

        if (path != other.path) return false
        if (method != other.method) return false
        if (behavior != other.behavior) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + behavior.hashCode()
        return result
    }

    override fun toString(): String {
        return "Endpoint(path='$path', method='$method', behavior=$behavior)"
    }
}