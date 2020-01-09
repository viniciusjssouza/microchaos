package microchaos.infra.network

data class IpAddress(val address: String) {

    companion object {
        const val VALIDATION_REGEX =
            "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$"
    }

    fun isValid(): Boolean {
        return this.address.matches(Regex(VALIDATION_REGEX))
    }
}