package microchaos.model

data class Behavior(
    val commands: List<Command>,
    val response: List<Response> = emptyList()
) {
}