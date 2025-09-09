package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.dto.BookCategoryDTO.Companion.toBook
import vn.example.ReadingAPI.dto.BookDTO.Companion.toBook
import vn.example.ReadingAPI.model.*
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.CategoryRepository
import vn.example.ReadingAPI.repository.ChapterRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.awt.color.ICC_Profile
import java.time.LocalDateTime
import kotlin.coroutines.Continuation

data class ReadingProgressDTO(

    val auser: UserDTO?,
    val abook: BookDTO?,
    val achapter: ChapterDTO?,
    val status: String,
    val progressPercentage: Float,
    val progressPath: String,
    val createdAt: LocalDateTime? = null
){
    companion object {
        fun from(reading: ReadingProgress?): ReadingProgressDTO? {
            return reading?.let {
                ReadingProgressDTO(
                    auser = UserDTO.getUserEmpty(it.auser),
                    abook = BookDTO.from(reading.abook),
                    achapter = null,
                    status = it.status,
                    progressPercentage = it.progressPercentage,
                    progressPath = it.progressPath,
                    createdAt = it.createdAt,

                )
            }
        }
        //BookController
        fun getCategory(reading: ReadingProgress?): ReadingProgressDTO? {
            return reading?.let {
                ReadingProgressDTO(
                    auser = UserDTO.getUserEmpty(it.auser),
                    abook = BookDTO.getCategory(reading.abook),
                    status = it.status,
                    achapter = null,
                    progressPercentage = it.progressPercentage,
                    progressPath = it.progressPath,
                    createdAt = it.createdAt,

                    )
            }
        }

        //ReadingProgressCOntroller
        fun getCategoryWithReading(reading: ReadingProgress?): ReadingProgressDTO? {
            return reading?.let {
                ReadingProgressDTO(
                    auser = UserDTO.getUserEmpty(it.auser),
                    abook = BookDTO.getCategoryWithReading(reading.abook),
                    achapter = null,
                    status = it.status,
                    progressPercentage = it.progressPercentage,
                    progressPath = it.progressPath,
                    createdAt = it.createdAt,

                    )
            }
        }

        fun getBookWithReading(reading: ReadingProgress?): ReadingProgressDTO? {
            return reading?.let {
                ReadingProgressDTO(
                    auser = UserDTO.getUserEmpty(it.auser),
                    abook = BookDTO.getBook(reading.abook),
                    achapter = null,
                    status = it.status,
                    progressPercentage = it.progressPercentage,
                    progressPath = it.progressPath,
                    createdAt = it.createdAt,

                    )
            }
        }
        //Đọc tiếp
        fun getReading(reading: ReadingProgress?): ReadingProgressDTO? {
            return reading?.let {
                ReadingProgressDTO(
                    auser = null,
                    abook = null,
                    achapter = ChapterDTO.getChapterAndBookmark(it.achapter),
                    status = it.status,
                    progressPercentage = it.progressPercentage,
                    progressPath = it.progressPath,
                    createdAt = it.createdAt,

                    )
            }
        }
        fun getReadingBook(reading: List<ReadingProgress?>?): List<ReadingProgressDTO?> {
            return if (reading != null) {
                reading.map { it ->
                    it?.let { it1 ->
                        ReadingProgressDTO(
                            auser = it1.auser?.let { user -> UserDTO.getUserEmpty(user) },
                            abook = BookDTO.getBook(it1.abook),
                            achapter = ChapterDTO.getChapterAndBookmark(it1.achapter),
                            status = it1.status,
                            progressPercentage = it1.progressPercentage,
                            progressPath = it1.progressPath,
                            createdAt = it1.createdAt
                        )
                    }
                }
            } else {
                emptyList()
            }
        }

        fun ReadingProgressDTO.toReading(bookRepository:BookRepository,userRepository: UserRepository,chapterRepository: ChapterRepository): ReadingProgress {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            val user = this.auser?.let { it.username?.let { it1 -> userRepository.findByUsername(it1)} }
            val chapter = this.achapter?.id?.let { chapterRepository.findById(it).orElse(null) }
            if (book == null || user == null || chapter ==null) {
                throw IllegalArgumentException("Book or Category not found")
            }
            return ReadingProgress(
                id = chapter.id?.let { ReadingProgressId(bookId = book.id, username = user.username, chapterId = it) },
                auser = user,
                abook = book,
                achapter= chapter,
                status = this.status,
                progressPercentage = this.progressPercentage,
                progressPath = this.progressPath,
                createdAt = this.createdAt ?: LocalDateTime.now(),

            )
        }
    }
}
