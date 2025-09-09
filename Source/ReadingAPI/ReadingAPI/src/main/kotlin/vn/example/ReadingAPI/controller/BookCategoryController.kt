package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookCategoryDTO
import vn.example.ReadingAPI.dto.BookCategoryDTO.Companion.toBook
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.CategoryDTO
import vn.example.ReadingAPI.model.BookCategoryId
import vn.example.ReadingAPI.repository.BookCategoryRepository
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.CategoryRepository
import vn.example.ReadingAPI.service.BookCategoryService
import vn.example.ReadingAPI.service.BookService

@RestController
@RequestMapping("/api/bookcategory")
class BookCategoryController(private var bookcategorySV:BookCategoryService,
                             private var bookSV:BookService
) {
    @Autowired
    private lateinit var bookRepository: BookRepository
    @Autowired
    private lateinit var categoryRepository:CategoryRepository
    @Autowired
    private lateinit var bookCategoryRepository: BookCategoryRepository

    //Book With Category
    //Discover 3
    @GetMapping("/category/{id}")
    fun getBookCategoryByCategory(@PathVariable id: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookcates = bookcategorySV.getBookCategoryByIdAndActive(id,0)
            if (bookcates.isNotEmpty()) {
                val bookmarkDTOs = bookcates.map { bookcategory ->
                    BookCategoryDTO(
                        abook = bookcategory.abook?.let { BookDTO.getBook(it) },
                        category = bookcategory.category?.let { CategoryDTO.from(it) }
                    )
                }
                responseData.status = 200
                responseData.message = "Get reading progress successfully"
                responseData.dataList = bookmarkDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No bookcategory found"
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("/all")
    fun getAllBookCategory(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookcates = bookcategorySV.getAll()

            // Use a Set to avoid duplicates
            val bookmarkDTOs = bookcates.map { bookcategory ->
                BookCategoryDTO.fromCate(bookcategory) // Make sure you're mapping the correct category
            }.distinctBy { it?.category?.id } // Ensure uniqueness by category ID

            if (bookmarkDTOs.isNotEmpty()) {
                responseData.status = 200
                responseData.message = "Get reading progress successfully"
                responseData.dataList = listOf(bookmarkDTOs)
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No reading progress found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createBookCategory(@RequestBody bookCategoryDTO: BookCategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookcategory = bookCategoryDTO.toBook(bookRepository, categoryRepository)

            val exists =
                bookcategory.id?.bookId?.let { BookCategoryId(bookId = it, categoryId = bookcategory.id.categoryId) }
                    ?.let { bookCategoryRepository.findById(it).isPresent }
            if (exists == true) {
                responseData.status = 209
                responseData.message = "BookCategory with this bookId and categoryId already exists"
                return ResponseEntity(responseData, HttpStatus.BAD_REQUEST)
            }

            // Lưu BookCategory nếu cặp chưa tồn tại
            bookCategoryRepository.save(bookcategory)
            responseData.status = 200
            responseData.message = "Added bookcategory successfully"
            ResponseEntity(responseData, HttpStatus.CREATED)

        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/delete")
    fun deleteBookCategory(@RequestBody bookCategoryDTO: BookCategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookcategory = bookCategoryDTO.toBook(bookRepository, categoryRepository)

            val exists =
                bookcategory.id?.let { BookCategoryId(bookId = it.bookId, categoryId = bookcategory.id.categoryId) }
                    ?.let { bookCategoryRepository.findById(it).isPresent }
            if (exists == true) {
                bookCategoryRepository.delete(bookcategory)
                responseData.status = 200
                responseData.message = "Delete bookcategory successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            }
            else{
                responseData.status = 209
                responseData.message = "No found"
                return ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/deleteByBookId")
    fun deleteByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"] 

        return try {
            if (id != null) {
                bookcategorySV.deleteByBookId2(id)
                responseData.status = 200
                responseData.message = "Delete bookcategory successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            } else {
                responseData.status = 400
                responseData.message = "ID is required"
                ResponseEntity(responseData, HttpStatus.BAD_REQUEST)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}