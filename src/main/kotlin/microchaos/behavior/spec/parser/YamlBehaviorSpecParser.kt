package microchaos.behavior.spec.parser

import com.charleskorn.kaml.Yaml
import microchaos.behavior.spec.model.Service
import microchaos.behavior.spec.model.ServiceSpec

class YamlBehaviorSpecParser : ServiceSpecParser  {
    override fun parse(representation: String): ServiceSpec {
        return Yaml.default.parse(ServiceSpec.serializer(), representation)
    }
}