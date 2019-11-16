package microchaos.service.spec.model

data class Behavior(
    val execution: List<Execution>,
    val response: List<Response> = emptyList()
) {
}