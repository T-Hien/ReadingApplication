package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.AuthorDTO
import vn.example.ReadingAPI.dto.AuthorDTO.Companion.toAuthor
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.CategoryDTO
import vn.example.ReadingAPI.dto.CategoryDTO.Companion.toCategory
import vn.example.ReadingAPI.dto.DetailAuthorDTO
import vn.example.ReadingAPI.dto.DetailAuthorDTO.Companion.toBook
import vn.example.ReadingAPI.model.BookCategoryId
import vn.example.ReadingAPI.model.DetailAuthorId
import vn.example.ReadingAPI.repository.AuthorRepository
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.DetailAuthorRepository
import vn.example.ReadingAPI.service.AuthorService

@RestController
@RequestMapping("/api/detailauthor")
class DetailAuthorController(private var detailAuthorRepository: DetailAuthorRepository) {
    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var authorRepository:AuthorRepository

    @Autowired
    private lateinit var authorService: AuthorService

    @Transactional
    @PostMapping("/create")
    fun createDetailAuthor(@RequestBody detailAuthorDTO: DetailAuthorDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val detail = detailAuthorDTO.toBook(bookRepository, authorRepository)
            val exists = detail.id.bookId.let { DetailAuthorId(bookId = detail.id.bookId, author = detail.id.author) }
                .let { detailAuthorRepository.findById(it).isPresent }
            if (exists) {
                responseData.status = 209
                responseData.message = "Detail already exists"
                return ResponseEntity(responseData, HttpStatus.BAD_REQUEST)
            }
            detailAuthorRepository.save(detail)
            responseData.status = 200
            responseData.message = "Added author successfully"
            return ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message} bookId: ${detailAuthorDTO.abook},${detailAuthorDTO}"
            return ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/delete")
    fun deleteDetailAuthor(@RequestBody detailAuthorDTO: DetailAuthorDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val detail = detailAuthorDTO.toBook(bookRepository, authorRepository)

            val detailAuthorId = DetailAuthorId(bookId = detail.abook.id, author = detail.author.id)
            val exists = detailAuthorRepository.findById(detailAuthorId).isPresent

            if (exists) {
                detailAuthorRepository.deleteById(detailAuthorId)
                responseData.status = 200
                responseData.message = "Deleted author successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            } else {
                responseData.status = 209
                responseData.message = "No detail author found"
                ResponseEntity(responseData, HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/deleteById")
    fun deleteAllDetailAuthorByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]
        return try {
            if (id != null) {
                authorService.deleteByBookId(id)
            }
            responseData.status = 200
            responseData.message = "Deleted author successfully"
            ResponseEntity(responseData, HttpStatus.OK)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}