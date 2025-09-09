package vn.example.readingapplication.model

data class Category(
    val id: Int?,
    val name: String? = null,
    val description: String? = null,
    val listBookCategory: List<BookCategory>? = null
){
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Category && this.id == other.id
    }
}
