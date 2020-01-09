package microchaos.model.ktor

import microchaos.model.Command
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse

internal class RequestCommand(command: Command) : KtorCommand(command) {

    override fun run(): String {
        val client = HttpClient.newBuilder().build()
        val request = this.httpRequest?.let {
            java.net.http.HttpRequest
                .newBuilder()
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