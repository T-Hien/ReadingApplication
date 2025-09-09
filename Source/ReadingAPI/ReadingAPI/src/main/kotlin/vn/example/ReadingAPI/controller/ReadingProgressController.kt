package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookmarkDTO
import vn.example.ReadingAPI.dto.BookmarkDTO.Companion.toBookmark
import vn.example.ReadingAPI.dto.ReadingProgressDTO
import vn.example.ReadingAPI.dto.ReadingProgressDTO.Companion.toReading
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.ChapterRepository
import vn.example.ReadingAPI.repository.ReadingProgressRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.ReadingProgressService

@RestController
@RequestMapping("/api/reading")
class ReadingProgressController(private val readingSV: ReadingProgressService) {
    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository:UserRepository

    @Autowired
    private lateinit var chapterRepository: ChapterRepository

    @Autowired
    private lateinit var readingProgressRepository: ReadingProgressRepository


    //Library All
    @GetMapping("/username/{username}")
    fun getReadingByUsername(@PathVariable username: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val readings = readingSV.getReadingProgressByUsername(username)
            if (readings.isNotEmpty()) {
                val readingDTOs = readings.map { reading ->
                    ReadingProgressDTO.getCategoryWithReading(reading)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = listOf(readingDTOs)
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No library found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    //Library Reading or Undread
    @GetMapping("/username/{username}/status/{status}")
    fun getReadingByUsernameAndStatus(@PathVariable username: String, @PathVariable status: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val readings = readingSV.getReadingByUsernameAndStatus(username, status)
            if (readings.isNotEmpty()) {
                val readingDTOs = readings.map { reading ->
                    ReadingProgressDTO.getCategoryWithReading(reading)
                }
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = listOf(readingDTOs)
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 204
                responseData.message = "No library found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //Lấy tiến trình (đọc tiếp)
    //Bo
    @GetMapping("/username/{username}/bookId/{bookId}")
    fun getReadingById(@PathVariable username: String, @PathVariable bookId: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val readings = readingSV.getReadingByIdUsernameBookId(username, bookId)

            if (readings ==null) {
                responseData.status = 404
                responseData.message = "No readings found for the given username and bookId"
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData)
            } else {
                val readingDTOs = ReadingProgressDTO.getReading(readings)
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.data = readingDTOs
                ResponseEntity.ok(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("/getChapter")
    fun getReadingByIdAndChap(@RequestParam username: String, @RequestParam bookId: Int, @RequestParam chapnum: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val readings = readingSV.getReadingByIdUsernameBookIdAnChapId(username, bookId, chapnum)

            if (readings ==null) {
                responseData.status = 404
                responseData.message = "No readings found for the given username and bookId"
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData)
            } else {
                val readingDTOs = ReadingProgressDTO.getReading(readings)
                responseData.status = 200
                responseData.message = "Get readings in successfully"
                responseData.data = readingDTOs
                ResponseEntity.ok(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/deleteByBookId")
    fun deleteByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                readingSV.deleteAllByBook(id)
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
    @PostMapping("/create")
    fun createBookmark(@RequestBody readingProgressDTO: ReadingProgressDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val reading = readingProgressDTO.toReading(bookRepository,userRepository,chapterRepository)
            if (reading != null) {
                readingProgressRepository.save(reading)
                responseData.status = 200
                responseData.message = "Added reading successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            } else {
                responseData.status = 204
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
    fun deleteBookmark(@RequestBody readingProgressDTO: ReadingProgressDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val reading = readingProgressDTO.toReading(bookRepository,userRepository,chapterRepository)
            if (reading != null) {
                reading.id?.let { readingProgressRepository.deleteById(it) }
                responseData.status = 200
                responseData.message = "Delete reading successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            } else {
                responseData.status = 204
                responseData.message = "Book or User not found"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("static")
    fun getCount(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val readings = readingSV.getTopReadingProgress()
        responseData.dataList = readings
        responseData.status = 200
        return ResponseEntity(responseData, HttpStatus.OK)
    }

}