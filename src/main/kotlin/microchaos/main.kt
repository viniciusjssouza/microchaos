package microchaos

import microchaos.config.BehaviorConfig
import microchaos.config.ConsulBehaviorConfigSource
import microchaos.config.LocalFileBehaviorConfigSource
import microchaos.infra.logging.loggerFor
import microchaos.model.ServiceModel
import microchaos.model.ktor.KtorImplFactory
import microchaos.parser.YamlBehaviorSpecParser
import java.lang.Thread.sleep

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
    waitActivity()
}

/**
 * This is required to keep the application running upon reloading the configuration, otherwise, when Ktor server is
 * shutdown, the application would finish. With this sleep, the application continues to run, even when Ktor is stopped.
 */
private fun waitActivity() {
    while (true) {
        sleep(500);
    }
}

fun runModel(newModel: ServiceModel) {
    if (::model.isInitialized) {
       log.info("Stopping service...")
       model.service.stop()
       log.info("Service stopped")
    }
    model = newModel
    log.info("Starting service...")
    model.service.start()
    log.info("Service started")
}
