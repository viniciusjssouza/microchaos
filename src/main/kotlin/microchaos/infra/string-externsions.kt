package microchaos.infra

import java.text.Normalizer

fun String.slugify() = Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .replace("[^\\p{ASCII}]".toRegex(), "")
        .replace("[^a-zA-Z0-9\\s]+".toRegex(), "").trim()
        .replace("\\s+".toRegex(), "-")
        .toLowerCase()