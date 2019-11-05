package microchaos.behavior.spec.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceSpec(
    val service: Service
) {
}