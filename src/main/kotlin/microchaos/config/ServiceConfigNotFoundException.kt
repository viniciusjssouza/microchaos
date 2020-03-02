package microchaos.config

import java.io.IOException

class ServiceConfigNotFoundException(message: String, original: Throwable?): IOException(message, original) {
}
