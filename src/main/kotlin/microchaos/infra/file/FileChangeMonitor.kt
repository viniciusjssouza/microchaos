package microchaos.infra.file

import java.nio.file.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class FileChangeMonitor(private val listener: (Path) -> Unit) {

    var stopped = false

    fun stop() {
        stopped = true
    }

    fun watch(watchedFile: Path) {
        thread(start = true) {
            FileSystems.getDefault().newWatchService().use { watchService ->
                // Here, we need to watch the file directory because watch service doesn't support watching files directly.
                watchedFile.parent.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
                awaitForChanges(watchService, watchedFile)
            }
        }
    }

    private fun awaitForChanges(watchService: WatchService, watchedFile: Path) {
        while (!stopped) {
            try {
                val key = watchService.poll(25, TimeUnit.MILLISECONDS)
                if (key == null) {
                    Thread.yield()
                    continue
                }
                processEvents(key, watchedFile)
                key.reset()
            } catch (e: InterruptedException) {
                return
            }

        }
    }

    private fun processEvents(key: WatchKey, watchedFile: Path) {
        for (event in key.pollEvents()) {
            val fileChanged: Path = event.context() as Path
            if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
                Thread.yield()
                continue
            } else {
                if (fileChanged.toString() == watchedFile.fileName.toString()) {
                    listener(watchedFile)
                }
            }
        }
    }
}