package microchaos.parser

import microchaos.model.ServiceModel

interface ServiceSpecParser {
    fun parse(representation: String): ServiceModel
}