package microchaos.service.spec.parser

import microchaos.service.spec.model.Service
import microchaos.service.spec.model.ServiceSpec

interface ServiceSpecParser {
    fun parse(representation: String): ServiceSpec
}