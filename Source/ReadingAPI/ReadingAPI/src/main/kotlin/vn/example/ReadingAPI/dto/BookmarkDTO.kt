package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.model.Bookmark
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.time.LocalDateTime

data class BookmarkDTO(
    val id: Int?,
    val note: NoteDTO?,
    val auser: UserDTO?,
    val abook: BookDTO?,
    val progress_percentage: String?,
    val position: Int?,
    val type: String,
    val chapternumber: Int?,
    val createdAt: LocalDateTime? = null
) {
    companion object {
        fun from(bookmark: Bookmark): BookmarkDTO {
            return BookmarkDTO(
                id = bookmark.id,
                note = bookmark.note?.let { NoteDTO.from(it) },
                auser = bookmark.aUser?.let { UserDTO.getUserEmpty(it) },
                abook = BookDTO.from(bookmark.abook),
                progress_percentage = bookmark.progress_percentage,
                position = bookmark.position,
                type = bookmark.type,
                chapternumber = bookmark.chapternumber,
                createdAt = bookmark.createdAt
            )
        }

        fun BookmarkDTO.toBookmark(
            bookRepository: BookRepository,
            userRepository: UserRepository,
            noteRepository: NoteRepository
        ): Bookmark? {
            val book = this.abook?.id?.let { bookRepository.findById(it).orElse(null) }
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }
            val note = this.note?.id?.let { noteRepository.findById(it).orElse(null) }

            return if (book != null && user != null) {
                Bookmark(
                    id = this.id ?: 0,
                    abook = book,
                    aUser = user,
                    note = note,
                    type = this.type ?: "",
                    position = this.position,
                    progress_percentage = this.progress_percentage,
                    chapternumber = this.chapternumber,
                    createdAt = this.createdAt ?: LocalDateTime.now()
                )
            } else {
                null
            }
        }


        fun simpleFrom(bookmark: Bookmark): BookmarkDTO {
            return BookmarkDTO(
                id = bookmark.id,
                note = null,
                auser = null,
                abook = BookDTO.getBook(bookmark.abook),
                progress_percentage = null,
                position = null,
                type = "",
                chapternumber = bookmark.chapternumber,
                createdAt = LocalDateTime.now()
            )
        }

        fun getBookmarkByNote(bookmark: Bookmark): BookmarkDTO {
            return BookmarkDTO(
                id = bookmark.id,
                note = null,
                auser = null,
                abook = null,
                progress_percentage = bookmark.progress_percentage,
                position = bookmark.position,
                type = bookmark.type,
                chapternumber = bookmark.chapternumber,
                createdAt = LocalDateTime.now()
            )
        }
    }
}
