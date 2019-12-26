package microchaos.service.spec.model

data class Endpoint(
    val path: String,
    val method: String,
    val behavior: Behavior
) {

}