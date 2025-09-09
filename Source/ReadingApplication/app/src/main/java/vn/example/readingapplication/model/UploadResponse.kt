package vn.example.readingapplication.model

data class UploadResponse(
    val content: Content?
)
data class Content(
    val url: String? // URL của hình ảnh
)
