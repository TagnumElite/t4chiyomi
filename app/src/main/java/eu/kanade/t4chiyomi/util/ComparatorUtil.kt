package eu.kanade.t4chiyomi.util

object ComparatorUtil {
    val CaseInsensitiveNaturalComparator = compareBy<String, String>(String.CASE_INSENSITIVE_ORDER) { it }.then(naturalOrder())
}
