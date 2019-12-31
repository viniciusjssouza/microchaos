package microchaos.infra

import java.io.IOException


object Environment {
    fun isLinuxOS(): Boolean {
        val osName = System.getProperty("os.name")
        return osName.contains("nix") || osName.contains("nux")
    }

    fun runCLICommand(command: String): String {
        val process = Runtime.getRuntime().exec(arrayOf("/bin/sh", "-c", command))
        process.waitFor()
        val stdOut = process.inputStream.bufferedReader().use { reader -> reader.readText() }
        val stdError = process.errorStream.bufferedReader().use { reader -> reader.readText() }

        if (stdError.isNotEmpty()) {
            throw IOException("Error during CLI command execution: $stdError")
        }
        return stdOut
    }
}
