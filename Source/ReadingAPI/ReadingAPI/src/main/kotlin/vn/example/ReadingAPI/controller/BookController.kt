package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.*
import vn.example.ReadingAPI.dto.BookDTO.Companion.toBook
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.service.*

@RestController
@RequestMapping("/api/book")
class BookController (private var bookSV: BookService
){
    @Autowired
    private lateinit var bookRepository: BookRepository
    //Discorver 1
    @GetMapping("/all")
    fun getAllBooksByOrderBy(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.findTop5ByOrderByIdDesc(0)
            if (books.isNotEmpty()) {
                val bookDTOs = books.map { book ->
                    BookDTO.getAllBook(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //Lấy tất cả sách
    @GetMapping("/all2")
    fun getAllBooks(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.showAll2(0)
            if (books.isNotEmpty()) {
                val bookDTOs = books.map { book ->
                    BookDTO.getCategory(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("/all3")
    fun getAllBooksActive(@RequestParam active:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.showAll2(active)
            if (books.isNotEmpty()) {
                val bookDTOs = books.map { book ->
                    BookDTO.getAllBook2(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("/id")
    fun getBookById(@RequestParam bookId:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.getBookById(bookId)
            if (books.isPresent) {
                val bookDTOs = books.map { book ->
                    BookDTO.getAllBookById(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = listOf(bookDTOs)
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //Thông tin sách
    @GetMapping("/bookId")
    fun getBookByBookId(@RequestParam bookId:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.getBookById(bookId)
            if (books.isPresent) {
                val bookDTOs = books.map { book ->
                    BookDTO.getBookAndCategory(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = listOf(bookDTOs)
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    //Mới nhất
    @GetMapping("/latest")
    fun getAllBooksByOrderByCreatedAt(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookSV.findTop20ByOrderByCreatedAtDesc()
            if (books.isNotEmpty()) {
                val bookDTOs = books.map { book ->
                    BookDTO.getAllBook(book)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No books found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //Tìm kiếm theo tên sách
    @GetMapping("/search")
    fun searchBooks(@RequestParam keyword: String):ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val book = bookSV.searchBooksByTitle(keyword)
            if (book.isNotEmpty()) {
                val bookDTOs = book.map { BookDTO.getCategory(it) }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            }else{
                responseData.status = 204
                responseData.message = "No book found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("author/{authorId}")
    fun getBookByAuthorId(@PathVariable authorId: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val book = bookSV.getBookByAuthor(authorId)
            if (book.isNotEmpty()) {
                val bookDTOs = book.map {
                    BookDTO.getAllBook(it)
                }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No book found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    //Admin Call Book of Author
    @GetMapping("author")
    fun getBookByAuthorId2(@RequestParam authorId: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val book = bookSV.getBookByAuthor(authorId)
            if (book.isNotEmpty()) {
                val bookDTOs = book.map {
                    BookDTO.getBook(it)
                }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No book found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("category")
    fun getBookByCategoryId(@RequestParam categoryId: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val book = bookSV.getBookByCategory(categoryId)
            if (book.isNotEmpty()) {
                val bookDTOs = book.map {
                    BookDTO.getBook(it)
                }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = bookDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No book found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @PostMapping("/create")
    fun createBook(@RequestBody bookDTO: BookDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookDTO.toBook()
            val savedBook = bookRepository.save(books)
            responseData.status = 200
            responseData.message = savedBook.title
            responseData.dataNum = savedBook.id
            ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/update")
    fun updateBook(@RequestBody bookDTO: BookDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookDTO.toBook()
            val book = bookRepository.findById(books.id)
            if (book ==null){
                responseData.status = 209
                responseData.message = "No book found"
                ResponseEntity(responseData, HttpStatus.OK)
            }
            else{
                bookRepository.save(books)
                responseData.status = 200
                responseData.message = "Update book successfully"
                ResponseEntity(responseData, HttpStatus.OK)

            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/delete")
    fun deleteBook(@RequestBody bookDTO: BookDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val books = bookDTO.toBook()
            val book = bookRepository.findById(books.id)
            if (book ==null){
                responseData.status = 209
                responseData.message = "No book found"
                ResponseEntity(responseData, HttpStatus.OK)
            }
            else{
                bookRepository.delete(books)
                responseData.status = 200
                responseData.message = "Delete book successfully"
                ResponseEntity(responseData, HttpStatus.OK)

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
                bookRepository.deleteById(id)
                responseData.status = 200
                responseData.message = "Delete successfully"
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
    @GetMapping("count")
    fun getCountBook(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val count = bookRepository.getCount()
            responseData.status = 200
            responseData.message = "Get successfully"
            responseData.dataNum = count
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
}