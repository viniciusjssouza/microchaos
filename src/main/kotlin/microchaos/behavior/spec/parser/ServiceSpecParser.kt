package microchaos.behavior.spec.parser

import microchaos.behavior.spec.model.Service
import microchaos.behavior.spec.model.ServiceSpec

interface ServiceSpecParser {
    fun parse(representation: String): ServiceSpec
}