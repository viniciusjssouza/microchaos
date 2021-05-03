package microchaos.model.command

import microchaos.infra.logging.loggerFor
import microchaos.model.HttpRequest
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse
import java.time.Duration

class RequestCommand(
    private val httpRequest: HttpRequest
) : Command() {

    companion object {
        private val log = loggerFor<RequestCommand>()
    }

    override fun run(): String {
        log.info("Sending request to ${this.httpRequest.method} '${this.httpRequest.target}'")
        val client = HttpClient.newBuilder().build()
        val request = this.httpRequest.let {
            java.net.http.HttpRequest
                .newBuilder()
                .timeout(Duration.ofSeconds(3))
                .method(it.method.toUpperCase(), java.net.http.HttpRequest.BodyPublishers.noBody())
                .uri((URI.create(it.target)))
                .build()

        } ?: throw IllegalArgumentException("Every request command must have a http request declaration")
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        if (response.statusCode() >= 500) {
            throw RuntimeException(
                "Unexpected response from server. Status: ${response.statusCode()} Content: ${response.body()}"
            )
        }
        return response.body()
    }
}
