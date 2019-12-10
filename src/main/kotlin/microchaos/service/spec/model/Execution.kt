package microchaos.service.spec.model

data class Execution(
    val type: String,
    val distribution: Distribution? = null,
    val httpRequest: HttpRequest? = null
) {
}
