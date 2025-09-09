package vn.example.readingapplication.utils.extention

import java.text.NumberFormat
import java.util.Locale

fun Int?.formatPointStandard(locale: Locale = Locale.US): String {
    return try {
        val nf: NumberFormat = NumberFormat.getNumberInstance(locale)
//    return nf.format(this ?: 0)?.replace(",", ".") ?: ""
        nf.format(this ?: 0)?.replace(".", ",") ?: ""
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}