package microchaos.service.spec.model

data class Response(
    val status: Int,
    val probability: Double = 100.0,
    val content: String = ""
) {

}
