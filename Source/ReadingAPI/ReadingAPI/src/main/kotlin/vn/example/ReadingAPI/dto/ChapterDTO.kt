package vn.example.ReadingAPI.dto

import vn.example.ReadingAPI.model.Chapter
import vn.example.ReadingAPI.model.Note
import vn.example.ReadingAPI.model.ReadingProgress
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.UserRepository
import java.time.LocalDateTime

data class ChapterDTO(
    val id: Int? = null,
    val abook: BookDTO?,
    val title: String?,
    val chapternumber: Int?,
    val createdAt: LocalDateTime?,
    val file_path: String?,
    val listReadingProgress: List<ReadingProgress?>?
){
    companion object{
        fun from(chapters:List<Chapter>):List<ChapterDTO>{
            return chapters.map { ChapterDTO(
                id = it.id,
                abook = null,
                title = it.title,
                chapternumber = it.chapternumber,
                createdAt = it.createdAt,
                file_path = it.file_path,
                listReadingProgress = emptyList()
            ) }
        }

        //Lấy chap
        fun getChapterAndBookmark(it:Chapter):ChapterDTO{
            return  ChapterDTO(
                id = it.id,
                abook = null,
                title = it.title?:"",
                chapternumber = it.chapternumber,
                createdAt = it.createdAt,
                file_path = it.file_path,
                listReadingProgress = emptyList()
            )
        }
        fun ChapterDTO.toChapter(bookRepository: BookRepository): Chapter? {
            val book = this.abook?.let { bookRepository.findById(it.id).orElse(null) }
            return if (book != null) {
                Chapter(
                    id = this.id,
                    abook = book,
                    title = this.title?:"",
                    chapternumber = this.chapternumber,
                    createdAt = this.createdAt?: LocalDateTime.now(),
                    file_path = this.file_path,
                    listReadingProgress = emptyList()
                )
            } else {
                null
            }
        }
    }
}