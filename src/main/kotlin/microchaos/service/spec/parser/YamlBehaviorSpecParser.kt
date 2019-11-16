package microchaos.service.spec.parser

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import microchaos.service.spec.model.ServiceSpec


class YamlBehaviorSpecParser : ServiceSpecParser {

    private val mapper: ObjectMapper = ObjectMapper(YAMLFactory())

    constructor() {
        this.mapper.findAndRegisterModules()
    }

    override fun parse(representation: String): ServiceSpec {
        return this.mapper.readValue(representation, ServiceSpec::class.java)
    }
}