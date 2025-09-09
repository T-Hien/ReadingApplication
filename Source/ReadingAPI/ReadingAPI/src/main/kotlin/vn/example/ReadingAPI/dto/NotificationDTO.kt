package vn.example.ReadingAPI.dto

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.Setter
import org.jetbrains.annotations.NotNull
import vn.example.ReadingAPI.model.Book
import vn.example.ReadingAPI.model.Notification
import vn.example.ReadingAPI.repository.BookRepository
import java.io.Serializable
import java.time.LocalDateTime

data class NotificationDTO (
    val id: Int? = null,
    val abook: BookDTO,
    val message: String? = null,
    val type: String? = null,
    val createdAt: LocalDateTime? = null
):Serializable{
    companion object{
        fun getNotificationAndBook(noti:Notification):NotificationDTO{
            return NotificationDTO(
                id = noti.id,
                abook =BookDTO.getBook(noti.abook),
                message = noti.message,
                type = noti.type,
                createdAt = noti.createdAt
            )
        }

        fun NotificationDTO.toNotification(bookRepository: BookRepository): Notification? {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            return if (book != null) {
                Notification(
                    id = this.id,
                    abook = book,
                    message = this.message,
                    type = this.type,
                    createdAt = this.createdAt?: LocalDateTime.now()
                )
            } else {
                null
            }
        }
    }
}
