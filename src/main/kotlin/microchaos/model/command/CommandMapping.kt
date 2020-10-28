package microchaos.model.command

val mapping = mapOf<String, Class<out Command>>(
    "applicationShutdown" to ApplicationShutdownCommand::class.java,
    "cpuBound" to CpuBoundCommand::class.java,
    "ioBound" to IoBoundCommand::class.java,
    "networkFailure" to NetworkFailureCommand::class.java,
    "request" to RequestCommand::class.java
)