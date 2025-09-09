package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.ChapterDTO
import vn.example.ReadingAPI.dto.ChapterDTO.Companion.toChapter
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.ChapterRepository
import vn.example.ReadingAPI.service.ChapterService

@RestController
@RequestMapping("/api/chapter")
class ChapterController (private var chapterService: ChapterService){

    @Autowired
    private lateinit var bookRepository:BookRepository

    @Autowired
    private lateinit var chapterRepository: ChapterRepository

    @GetMapping("/chapterNum/{chapterNum}/bookId/{bookId}")
    fun getChapterByBookAndId(@PathVariable("chapterNum")chapterNum:Int,@PathVariable("bookId")bookId:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val chapters = chapterService.getChapterByNum(bookId, chapterNum)
            val chapterDTOs = ChapterDTO.getChapterAndBookmark(chapters)
                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = listOf(chapterDTOs)
                ResponseEntity.ok(responseData)

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //List Chap in ReadBook
    @GetMapping("bookId/{bookId}")
    fun getChapterByBook(@PathVariable("bookId")bookId:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val chapters = chapterService.getChapterByBookId(bookId)
            val chapterDTOs = ChapterDTO.from(chapters)
            responseData.status = 200
            responseData.message = "Get Book in successfully"
            responseData.dataList = chapterDTOs
            ResponseEntity.ok(responseData)

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createChapter(@RequestBody chapterDTO:ChapterDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val chapter = chapterDTO.toChapter(bookRepository)
            if (chapter != null) {
                chapterRepository.save(chapter)
                responseData.status = 200
                responseData.message = "Added chapter successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            } else {
                responseData.status = 204
                responseData.message = "Book not found"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/deleteById")
    fun deleteAllChapterByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<Any> {
        val responseData = ResponseData()
        val id = requestBody["id"]
        return try {
            if (id != null) {
                chapterService.deleteAllByBookId(id)
            }
            responseData.status = 200
            responseData.message = "Deleted chapter successfully"
            ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


    
}