package microchaos.support

object ModelFiles {
    fun simpleService(): String = this.javaClass.getResource("/simpleService.yaml").file
}