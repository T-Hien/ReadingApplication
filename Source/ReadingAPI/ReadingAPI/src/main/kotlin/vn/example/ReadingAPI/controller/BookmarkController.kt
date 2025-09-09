package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookmarkDTO
import vn.example.ReadingAPI.dto.BookmarkDTO.Companion.toBookmark
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.model.Bookmark
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.BookmarkRepository
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.BookmarkService

@RestController
@RequestMapping("/api/bookmark")
class BookmarkController(private var bookmarkSV:BookmarkService) {
    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var noteRepository:NoteRepository
    @Autowired
    private lateinit var bookRepository:BookRepository
    @Autowired
    private lateinit var bookmarkRepository: BookmarkRepository


    //Gần đây Discorver 2
    @GetMapping("/{username}")
    fun getBookmarkByUsername(@PathVariable username: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookmarks = bookmarkSV.getBookmarkByUsername(username)
            if (bookmarks.isNotEmpty()) {
                val bookmarkDTOs = bookmarks.map { bookmark ->
                    BookmarkDTO.simpleFrom(bookmark)
                }
                responseData.status = 200
                responseData.message = "Get reading progress successfully"
                responseData.dataList = bookmarkDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No reading progress found"
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("/get")
    fun getBookmarkByBook(@RequestParam chapternum:Int,@RequestParam type:String,@RequestParam bookId:Int,@RequestParam username: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val bookmarks = bookmarkSV.getBookmarkByBookAndUser(chapternum,type, bookId, username)
            if (bookmarks.isNotEmpty()) {
                val bookmarkDTOs = bookmarks.map { bookmark ->
                    BookmarkDTO.getBookmarkByNote(bookmark)
                }
                responseData.status = 200
                responseData.message = "Get bookmark successfully"
                responseData.dataList = bookmarkDTOs
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No bookmark found"
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @PostMapping("/create")
    fun createBookmark(@RequestBody bmDTO: BookmarkDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val bookmark = bmDTO.toBookmark(bookRepository, userRepository,noteRepository)
            if (bookmark != null) {
                bookmarkRepository.save(bookmark)
                responseData.status = 200
                responseData.message = "Added note successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            } else {
                responseData.status = 404
                responseData.message = "Book or User not found"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/delete")
    fun deleteById(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                val optionalBookmark = bookmarkRepository.findById(id)
                if (optionalBookmark.isPresent) {
                    val bookmark = optionalBookmark.get()
                    bookmarkRepository.deleteById(id)
                    responseData.status = 200
                    responseData.message = "Delete successfully"
                    responseData.dataNum = bookmark.note?.id?: 0
                    ResponseEntity(responseData, HttpStatus.OK)
                } else {
                    responseData.status = 404
                    responseData.message = "Bookmark not found"
                    ResponseEntity(responseData, HttpStatus.NOT_FOUND)
                }
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

    @PostMapping("/deleteByBookId")
    fun deleteByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                bookmarkSV.deleteAllByBook(id)
                responseData.status = 200
                responseData.message = "Delete successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            } else {
                responseData.status = 400
                responseData.message = "ID is required"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/deleteByNoteId")
    fun deleteByNoteId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                bookmarkSV.deleteByNote(id)
                responseData.status = 200
                responseData.message = "Delete successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            } else {
                responseData.status = 400
                responseData.message = "ID is required"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}