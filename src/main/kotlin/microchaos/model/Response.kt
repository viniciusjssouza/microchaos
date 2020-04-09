package microchaos.model

data class Response(
    val status: Int,
    val probability: Double = 100.0,
    val content: String = ""
) {

    init {
        assert(this.probability >= 0) { "Probability cannot be less than 0" }
    }
}
