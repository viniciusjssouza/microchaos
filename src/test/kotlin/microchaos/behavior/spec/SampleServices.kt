package microchaos.behavior.spec

import microchaos.behavior.spec.model.Service
import microchaos.behavior.spec.model.ServiceSpec

object SampleServices {
    val simpleService = ServiceSpec(
        Service("simpleService")
    )
}