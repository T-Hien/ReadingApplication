package vn.example.readingapplication.utils.extention

import java.text.SimpleDateFormat
import java.util.Date

fun Date.dateFormat(pattern: String): String {
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}

fun Date.defaultDateFormat(): String {
    return dateFormat("dd/MM/yyyy")
}