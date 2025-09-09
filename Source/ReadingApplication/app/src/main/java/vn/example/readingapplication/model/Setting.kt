package vn.example.readingapplication.model

data class Setting(
    val id: Int?,
    val user: User,
    val font: String?,
    val font_size: Int,
    val readingMode: String?
)
