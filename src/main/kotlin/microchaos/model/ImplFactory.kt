package microchaos.model

abstract class ImplFactory {

    private val mappers = mapOf<Class<out Any>, (Any) -> Any>(
        Service::class.java to { source -> this.getServiceImpl(source as Service) },
        Endpoint::class.java to { source -> this.getEndpointImpl(source as Endpoint) }
    )

    abstract fun getServiceImpl(service: Service): Service
    abstract fun getEndpointImpl(endpoint: Endpoint): Endpoint
    abstract fun getCommandImpl(command: Command): Command

    fun hasRegisteredMapper(modelClass: Class<out Any>) = mappers.containsKey(modelClass)

    fun getMapper(modelClass: Class<out Any>): (Any) -> Any  {
        return mappers.getOrElse(modelClass, {
            throw IllegalArgumentException("No mapper found for model class ${modelClass.name}")
        })
    }
}