package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.BookDTO.Companion.toBook
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.dto.RepliesDTO
import vn.example.ReadingAPI.dto.RepliesDTO.Companion.toReply
import vn.example.ReadingAPI.dto.RepliesDTO.Companion.toReply2
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.NoteRepository
import vn.example.ReadingAPI.repository.RepliesRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.NoteService
import vn.example.ReadingAPI.service.RepliesService

@RestController
@RequestMapping("/api/replies")
class RepliesController(private var repliesRepository: RepliesRepository) {
    @Autowired
    private lateinit var noteRepository: NoteRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var repliesSV: RepliesService

    @PostMapping("/create")
    fun createNote(@RequestBody repliesDTO: RepliesDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val reply = repliesDTO.toReply(noteRepository, userRepository)
            if (reply != null) {
                val saveReply = repliesRepository.save(reply)
                responseData.status = 200
                responseData.dataNum = saveReply.id?.toInt()!!
                responseData.message = "Added reply successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            } else {
                responseData.status = 204
                responseData.message = "User or Note not found"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/delete")
    fun deleteNote(@RequestBody repliesDTO: RepliesDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val reply = repliesDTO.toReply2(noteRepository, userRepository)
            if (reply != null) {
                val existingNote = reply.id?.let { repliesRepository.findById(reply.id) }
                if (existingNote?.isPresent == true) {
                    repliesRepository.delete(reply)
                    responseData.status = 200
                    responseData.message = "Deleted reply successfully"
                    ResponseEntity(responseData, HttpStatus.OK)
                } else {
                    responseData.status = 204
                    responseData.message = "Reply not found"
                    ResponseEntity(responseData, HttpStatus.NOT_FOUND)
                }
            } else {
                responseData.status = 204
                responseData.message = "User or Note not found"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
     @PostMapping("/deletebyNote")
        fun deleteRepliesByNote(@RequestBody repliesDTO: RepliesDTO): ResponseEntity<Any> {
            val responseData = ResponseData()
            return try {
                val reply = repliesDTO.toReply2(noteRepository, userRepository)
                if (reply != null) {
                    val existingReplies = reply.note.id?.let { repliesRepository.findAllByNoteId(it) }
                    if (existingReplies != null && existingReplies.isNotEmpty()) {
                        reply.note.id?.let { repliesSV.deleteByNoteId(it) }
                        responseData.status = 200
                        responseData.message = "Deleted replies successfully"
                        ResponseEntity(responseData, HttpStatus.OK)
                    } else {
                        responseData.status = 204
                        responseData.message = "Replies not found"
                        ResponseEntity(responseData, HttpStatus.NOT_FOUND)
                    }
                } else {
                    responseData.status = 204
                    responseData.message = "User or Note not found"
                    ResponseEntity(responseData, HttpStatus.NOT_FOUND)
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
                repliesSV.deleteAllByNote(id)
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