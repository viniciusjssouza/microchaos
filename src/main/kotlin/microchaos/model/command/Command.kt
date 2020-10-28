package microchaos.model.command

abstract class Command {
    abstract fun run(): Any
}
