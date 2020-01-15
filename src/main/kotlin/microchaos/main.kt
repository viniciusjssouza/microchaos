package microchaos

import microchaos.config.Configuration
import microchaos.config.LocalFileConfigSource
import microchaos.model.ktor.KtorImplFactory
import microchaos.parser.YamlBehaviorSpecParser

fun main(args: Array<String>) {
    val implFactory = KtorImplFactory()
    val config = Configuration(
        arrayOf(
            LocalFileConfigSource()
        ),
        YamlBehaviorSpecParser(implFactory)
    )
    val model = config.load()
    model.service.start(wait = true)
}
