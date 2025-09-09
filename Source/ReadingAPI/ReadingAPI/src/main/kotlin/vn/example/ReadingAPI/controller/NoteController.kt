package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.BookmarkDTO
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote2
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.NoteService

@RestController
@RequestMapping("/api/note")
class NoteController(private var noteSV:NoteService, private var noteRepository:NoteRepository) {
    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    //Lấy bình luận, ghi chú
    @GetMapping("type")
    fun getNote(@RequestParam bookId: Int, @RequestParam type: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByTypeAndBook(type, bookId) // Should return List<Note>
            val noteDTOs = notes.map { NoteDTO.from(it) } // Use map instead of NoteDTO.from(notes)

            responseData.status = 200
            responseData.message = "Get Note in successfully"
            responseData.dataList = noteDTOs // Directly assign noteDTOs
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    //Lấy bình luận theo chương
    @GetMapping("chap")
    fun getNoteByChap(@RequestParam bookId: Int, @RequestParam type: String,@RequestParam chapnum: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByTypeAndChap(type, bookId,chapnum) // Should return List<Note>
            val noteDTOs = notes.map { NoteDTO.fromChap(it) } // Use map instead of NoteDTO.from(notes)

            responseData.status = 200
            responseData.message = "Get Note in successfully"
            responseData.dataList = noteDTOs // Directly assign noteDTOs
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("/largest-id")
    fun getLargestNoteId(): ResponseEntity<Int?> {
        val largestNoteId = noteSV.getLargestNoteId()
        return ResponseEntity.ok(largestNoteId)
    }

    @PostMapping("/create")
    fun createNote(@RequestBody noteDTO: NoteDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val note = noteDTO.toNote(bookRepository, userRepository)
            if (note != null) {
                val savenote = noteRepository.save(note)
                responseData.status = 200
                responseData.message = "Added note successfully"
                responseData.dataNum = savenote.id!!
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

    @PostMapping("/update")
    fun updateNote(@RequestBody noteDTO: NoteDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val note = noteDTO.toNote(bookRepository, userRepository)
            val notes = note?.id?.let { noteRepository.findById(it) }
            if (notes != null) {
                val savenote = noteRepository.save(note)
                responseData.status = 200
                responseData.message = "Added note successfully"
                responseData.dataNum = savenote.id!!
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
    fun deleteNote(@RequestBody noteDTO: NoteDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val note = noteDTO.toNote2(bookRepository, userRepository)
            if (note != null) {
                val existingNote = note.id?.let { noteRepository.findById(it) }
                if (existingNote?.isPresent == true) {
                    noteRepository.deleteById(note.id)
                    responseData.status = 200
                    responseData.message = "Deleted note successfully"
                    ResponseEntity(responseData, HttpStatus.OK)
                } else {
                    responseData.status = 204
                    responseData.message = "Note not found"
                    ResponseEntity(responseData, HttpStatus.NOT_FOUND)
                }
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

    //Admin
    //get all bình luận
    @GetMapping("byType")
    fun getNoteByType( @RequestParam type: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByType(type)
            val noteDTOs = notes.map { NoteDTO.fromChap(it) }

            responseData.status = 200
            responseData.message = "Get Note in successfully"
            responseData.dataList = noteDTOs
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("byTypeAndStatus")
    fun getNoteByTypeAndStatus( @RequestParam type: String, @RequestParam status:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByTypeAndStatus(type, status)
            val noteDTOs = notes.map { NoteDTO.fromChap(it) }

            responseData.status = 200
            responseData.message = "Get Note in successfully"
            responseData.dataList = noteDTOs
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("byId")
    fun getComentById( @RequestParam noteId: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val note = noteSV.getNoteById(noteId)

            val noteDTOs = note.map {
                NoteDTO.fromChap(it)
            }
            responseData.status = 200
            responseData.message = "Get Note in successfully"
            responseData.dataList = listOf(noteDTOs)
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("byTypeComment")
    fun getNoteByTypeComment( @RequestParam type: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByType(type)
            val noteDTOs = notes.map { NoteDTO.fromChapComment(it) }

            responseData.status = 200
            responseData.message = "Get Book in successfully"
            responseData.dataList = noteDTOs
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    //Bo
    @GetMapping("{type}/{bookId}/{username}")
    fun getNoteByTypeAndBookAndUser(@PathVariable type: String,@PathVariable bookId:Int,@PathVariable username:String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByTypeAndBookAndUser(type, bookId, username)
            if (notes.isNotEmpty()) {
                val noteDTOs = notes.map { NoteDTO.getNotes(it) }
                responseData.status = 200
                responseData.message = "Get Note in successfully"
                responseData.dataList = noteDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No notes found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("getnotebychap")
    fun getNoteByTypeAndBookAndUserAndChap(@RequestParam type: String,@RequestParam bookId:Int,
                                           @RequestParam username:String,@RequestParam chapnum: Int)
    : ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val notes = noteSV.getNoteByTypeAndBookAndUserAndChap(type, bookId, username,chapnum)
            if (notes.isNotEmpty()) {
                val noteDTOs = notes.map { NoteDTO.getNotes(it) }
                responseData.status = 200
                responseData.message = "Get Note in successfully"
                responseData.dataList = noteDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No notes found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
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
                noteSV.deleteAllByBook(id)
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
    @PostMapping("/deleteId")
    fun deleteById(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                noteRepository.deleteById(id)
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
    @PostMapping("/deleteByNoteId")
    fun deleteByNoteId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                noteRepository.deleteById(id)
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

}