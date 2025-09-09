package vn.example.readingapplication.utils.extention

inline fun builderString(builder: StringBuilder.() -> Unit): String {
    val stringBuilder = StringBuilder()
    stringBuilder.builder()
    return stringBuilder.toString()
}


