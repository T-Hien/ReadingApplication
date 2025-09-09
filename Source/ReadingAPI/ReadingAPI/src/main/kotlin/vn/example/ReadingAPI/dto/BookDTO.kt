package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.*
import java.time.LocalDateTime

data class BookDTO(
    val id: Int,
    val title: String? = null,
    val createdAt: LocalDateTime? = null,
    val description: String? = null,
    val cover_image: String? = null,
    val type_file: String? = null,
    val status: String? = null,
    val active: Int?,
    val listBookCategory: List<BookCategoryDTO?>?,
    val listDetailAuthor: List<DetailAuthorDTO>?,
    val listReadingProgress: List<ReadingProgressDTO?>?,
    val favorite:FavoriteDTO?,
    val listNotification: List<Notification?>?,
    val listChapter: List<ChapterDTO>?,
    val listNote: List<NoteDTO?>?,
    val listBookmark: List<Bookmark?>?,
    val listLike: List<Likes?>?

    ) {
    companion object {
        fun from(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description = book.description ?: "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.from(book.listBookCategory),
                listDetailAuthor = DetailAuthorDTO.from(book.listDetailAuthor),
                listReadingProgress = emptyList(),
                favorite = null,
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        fun getAllBook(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description = book.description ?: "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.getCategory(book.listBookCategory),
                listDetailAuthor = DetailAuthorDTO.from(book.listDetailAuthor),
                listReadingProgress = emptyList(),
                favorite = book.favorite?.let { FavoriteDTO.fromFavorite(it) },
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        //all2
        fun getAllBook2(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description = book.description ?: "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.getCategory(book.listBookCategory),
                listDetailAuthor = DetailAuthorDTO.from(book.listDetailAuthor),
                listReadingProgress = emptyList(),
                favorite = book.favorite?.let { FavoriteDTO.fromFavorite(it) },
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        //Book By Id
        fun getAllBookById(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description = book.description ?: "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = emptyList(),
                listDetailAuthor = emptyList(),
                listReadingProgress = emptyList(),
                favorite = null,
                listNote = NoteDTO.getOnlyNote(book.listNote),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        //BookControllerDTO
        fun getCategory(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description =  "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.getCategory(book.listBookCategory),
                listDetailAuthor = emptyList(),
                listReadingProgress = emptyList(),
                favorite = book.favorite?.let { FavoriteDTO.fromFavorite(it) },
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }

        //ReadBook
        fun getBookAndCategory(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description =  book.description,
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.getCategory(book.listBookCategory),
                listDetailAuthor = DetailAuthorDTO.from(book.listDetailAuthor),
                listReadingProgress = ReadingProgressDTO.getReadingBook(book.listReadingProgress),
                favorite = book.favorite?.let { FavoriteDTO.fromFavorite(it) },
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = ChapterDTO.from(book.listChapter),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        fun getCategoryWithReading(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title = book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description =  "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = book.status ?: "",
                listBookCategory = BookCategoryDTO.getCategory(book.listBookCategory),
                listDetailAuthor = emptyList(),
                listReadingProgress = emptyList(),
                favorite = null,
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        fun getBook(book: Book): BookDTO {
            return BookDTO(
                id = book.id ?: 0,
                title =  book.title,
                createdAt = book.createdAt ?: LocalDateTime.now(),
                description = "",
                cover_image = book.cover_image ?: "",
                type_file = book.type_file ?: "",
                status = "",
                listBookCategory = emptyList(),
                listDetailAuthor = emptyList(),
                listReadingProgress = emptyList(),
                favorite = null,
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = book.active
            )
        }
        fun BookDTO.toBook(): Book {
            return Book(
                id = this.id,
                title =  this.title?:"",
                createdAt = this.createdAt ?: LocalDateTime.now(),
                description = this.description?:"",
                cover_image = this.cover_image ?: "",
                type_file = this.type_file ?: "",
                status = this.status?:"",
                listBookCategory = emptyList(),
                listDetailAuthor = emptyList(),
                listReadingProgress = emptyList(),
                favorite = null,
                listNote = emptyList(),
                listBookmark = emptyList(),
                listChapter = emptyList(),
                listNotification = emptyList(),
                listLike = emptyList(),
                active = this.active?:0
            )
        }
    }
}
