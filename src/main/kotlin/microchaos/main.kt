package microchaos

import microchaos.config.BehaviorConfig
import microchaos.config.ConsulBehaviorConfigSource
import microchaos.config.LocalFileBehaviorConfigSource
import microchaos.infra.logging.loggerFor
import microchaos.model.ServiceModel
import microchaos.model.ktor.KtorImplFactory
import microchaos.parser.YamlBehaviorSpecParser

lateinit var model: ServiceModel
val log = loggerFor("main")


fun main() {
    val implFactory = KtorImplFactory()
    val config = BehaviorConfig(
        arrayOf(
            ConsulBehaviorConfigSource(),
            LocalFileBehaviorConfigSource()
        ),
        YamlBehaviorSpecParser(implFactory)
    ).onModelChange(::runModel)
    runModel(config.load())
}

fun runModel(newModel: ServiceModel) {
    if (::model.isInitialized) {
       log.info("Stopping service...")
       model.service.stop()
       log.info("Service stopped")
    }
    model = newModel
    log.info("Starting service...")
    model.service.start(wait = true)
    log.info("Service started")
}
