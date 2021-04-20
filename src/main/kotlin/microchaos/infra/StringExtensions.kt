package microchaos.infra

import java.text.Normalizer

private enum class CharType {
    DIGIT, LOWER_CASE, UPPER_CASE, NON_ALPHA_NUMERIC
}

private fun getCharType(char: kotlin.Char): CharType {
    return when {
        char.isLowerCase() -> CharType.LOWER_CASE
        char.isUpperCase() -> CharType.UPPER_CASE
        char.isDigit() -> CharType.DIGIT
        else -> CharType.NON_ALPHA_NUMERIC
    }
}

fun String.slugify(): String {
    val cleanStr = Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("[^^a-zA-Z0-9]".toRegex(), "-")
            .replace("(\\-)+".toRegex(), "-")

    return cleanStr.mapIndexed { idx, char ->
        when {
            char == '-' -> ""
            idx == 0 -> {
                char.toString()
            }
            getCharType(cleanStr[idx-1]) != getCharType(char) -> {
                "-${char}"
            }
            else -> {
                char.toString()
            }
        }
    }.joinToString("")
            .toLowerCase()
}