package vn.example.readingapplication.model

data class Note(
    val id: Int?,
    val content: String?=null,
    val status:Int?=0,
    val description:String?=null,
    val abook: Book?=null,
    val auser: User?=null,
    val type: String?=null,
    val createdAt: String?=null,
    val chapternumber: Int?=null,
    val bookmark: Bookmarks?=null,
    val listReplies: List<Replies>? = null
)
