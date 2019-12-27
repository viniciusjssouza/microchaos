package microchaos.service.spec.model

data class Execution(
    val type: String,
    val duration: Distribution? = null,
    val httpRequest: HttpRequest? = null
) {
}
