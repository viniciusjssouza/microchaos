package microchaos.service.spec.model

data class Execution(
   val type: String,
   val distribution: Distibution? = null,
   val httpRequest: HttpRequest? = null
) {
}
