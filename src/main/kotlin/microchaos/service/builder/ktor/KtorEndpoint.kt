package microchaos.service.builder.ktor

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import microchaos.service.spec.model.Endpoint

object KtorEndpoint {
    fun build(application: Application, endpoint: Endpoint): Unit {
//        routing {
//            get("/") {
//                call.respondText("Hello World!", ContentType.Text.Plain)
//            }
//            get("/demo") {
//                call.respondText("HELLO WORLD!")
//            }
//        }
    }

}
