package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.model.Replies
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.time.LocalDateTime

data class RepliesDTO(
    val id: Long? = null,
    val note: Int?,
    val auser: UserDTO?,
    val content: String,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(replies: List<Replies>?): List<RepliesDTO> {
            return replies?.mapNotNull { reply ->
                reply?.let {
                    RepliesDTO(
                        id = it.id,
                        note = it.note?.id,
                        auser = it.auser?.let { it1 -> UserDTO.getUserEmpty(it1) },
                        content = it.content,
                        createdAt = it.createdAt,
                    )
                }
            } ?: emptyList()
        }

        fun RepliesDTO.toReply(noteRepository: NoteRepository, userRepository: UserRepository): Replies? {
            val note = this.note?.let { noteRepository.findById(it).orElse(null) }
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }

            return note?.let {
                Replies(
                    id = this.id,
                    note = it,
                    auser = user,
                    content = this.content,
                    createdAt = this.createdAt ?: LocalDateTime.now()
                )
            }
        }
        fun RepliesDTO.toReply2(noteRepository: NoteRepository, userRepository: UserRepository): Replies? {
            val note = this.note?.let { noteRepository.findById(it).orElse(null) }
            val user = this.auser?.username?.let { userRepository.findByUsername(it) }

            return note?.let {
                Replies(
                    id = this.id,
                    note = it,
                    auser = null,
                    content = this.content,
                    createdAt = this.createdAt ?: LocalDateTime.now()
                )
            }
        }
    }
}
