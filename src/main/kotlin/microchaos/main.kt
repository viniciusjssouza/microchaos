package microchaos

import microchaos.config.BehaviorConfig
import microchaos.config.LocalFileBehaviorConfigSource
import microchaos.model.ktor.KtorImplFactory
import microchaos.parser.YamlBehaviorSpecParser

fun main(args: Array<String>) {
    val implFactory = KtorImplFactory()
    val config = BehaviorConfig(
        arrayOf(
            LocalFileBehaviorConfigSource()
        ),
        YamlBehaviorSpecParser(implFactory)
    )
    val model = config.load()
    model.service.start(wait = true)
}
