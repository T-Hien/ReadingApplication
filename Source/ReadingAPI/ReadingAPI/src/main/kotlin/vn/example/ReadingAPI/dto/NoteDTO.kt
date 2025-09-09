package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote2
import vn.example.ReadingAPI.model.Book
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.model.User
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.time.LocalDateTime

data class NoteDTO(
    val id: Int?,
    val content: String?,
    val chapternumber: Int? ,
    val abook: BookDTO?,
    val auser: UserDTO?,
    val type: String? = null,
    val createdAt: LocalDateTime? = null,
    val bookmark: BookmarkDTO?,
    val status :Int?,
    val description:String?,
    val listReplies: List<RepliesDTO?> = emptyList()
) {
    companion object {
        fun from(note: Note): NoteDTO {
            return note.let {
                NoteDTO(
                    id = it.id,
                    content = it.content,
                    chapternumber = it.chapternumber,
                    abook = it.abook?.let { it1 -> BookDTO.getBook(it1) },
                    auser = it.auser?.let { it1 -> UserDTO.getUserEmpty(it1) },
                    type = it.type,
                    createdAt = it.createdAt,
                    bookmark = null,
                    status = it.status,
                    description = it.description,
                    listReplies = RepliesDTO.from(it.listReplies)
                )
            }
        }
        //Lấy note theo chương
        fun fromChap(note: Note): NoteDTO {
            return note.let {
                NoteDTO(
                    id = it.id,
                    content = it.content,
                    chapternumber = it.chapternumber,
                    abook = it.abook?.let { it1 -> BookDTO.getBook(it1) },
                    auser = it.auser?.let { it1 -> UserDTO.getUserChap(it1) },
                    type = it.type,
                    createdAt = it.createdAt,
                    bookmark = null,
                    status = it.status,
                    description = it.description,
                    listReplies = RepliesDTO.from(it.listReplies)
                )
            }
        }
        fun fromChap2(note: List<Note>): List<NoteDTO> {
            return if (note != null) {
                note.map {
                    NoteDTO(
                        id = it.id,
                        content = it.content,
                        chapternumber = it.chapternumber,
                        abook = null,
                        auser = null,
                        type = it.type,
                        createdAt = it.createdAt,
                        bookmark = null,
                        status = it.status,
                        description = it.description,
                        listReplies = RepliesDTO.from(it.listReplies)
                    )
                }
            } else {
                emptyList()
            }
        }
        fun fromChapComment(note: Note): NoteDTO {
            return note.let {
                NoteDTO(
                    id = it.id,
                    content = it.content,
                    chapternumber = it.chapternumber,
                    abook = it.abook?.let { it1 -> BookDTO.getBook(it1) },
                    auser = it.auser?.let { it1 -> UserDTO.getUserChap(it1) },
                    type = it.type,
                    createdAt = it.createdAt,
                    bookmark = null,
                    status = it.status,
                    description = it.description,
                    listReplies = emptyList()
                )
            }
        }
        fun NoteDTO.toNote(bookRepository: BookRepository, userRepository: UserRepository): Note? {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }
            return if (book != null && user != null) {
                this.status?.let {
                    this.description?.let { it1 ->
                        Note(
                            id = this.id ?: 0,
                            content = this.content,
                            chapternumber = this.chapternumber,
                            abook = book,
                            auser = user,
                            type = this.type ?: "",
                            createdAt = this.createdAt ?: LocalDateTime.now(),
                            bookmark = null,
                            status = it,
                            description = it1,
                            listReplies = emptyList()
                        )
                    }
                }
            } else {
                null
            }
        }
        fun NoteDTO.toNote2(bookRepository: BookRepository, userRepository: UserRepository): Note? {
                return this.status?.let {
                    this.description?.let { it1 ->
                        Note(
                            id = this.id ?: 0,
                            content = this.content,
                            chapternumber = this.chapternumber,
                            abook = null,
                            auser = null,
                            type = this.type ?: "",
                            createdAt = this.createdAt ?: LocalDateTime.now(),
                            bookmark = null,
                            status = it,
                            description = it1,
                            listReplies = emptyList()
                        )
                    }
                }
        }

        fun getOnlyNote(note: List<Note>?): List<NoteDTO?> {
            return if (note != null) {
                note.map {
                    NoteDTO(
                        id = it.id,
                        content = it.content,
                        chapternumber = it.chapternumber,
                        abook = null,
                        auser = null,
                        type = it.type,
                        createdAt = it.createdAt,
                        bookmark = null,
                        status = it.status,
                        description = it.description,
                        listReplies = emptyList()
                    )
                }
            } else {
                emptyList()
            }
        }

        //Lấy Ghi chú
        fun getNotes(note: Note): NoteDTO {
            return note.let {
                NoteDTO(
                    id = it.id,
                    content = it.content,
                    chapternumber = it.chapternumber,
                    abook = null,
                    auser = null,
                    type = it.type,
                    createdAt = it.createdAt,
                    status = it.status,
                    description = it.description,
                    bookmark = it.bookmark?.let { it1 -> BookmarkDTO.getBookmarkByNote(it1) },
                    listReplies = emptyList()
                )
            }
        }
    }
}
