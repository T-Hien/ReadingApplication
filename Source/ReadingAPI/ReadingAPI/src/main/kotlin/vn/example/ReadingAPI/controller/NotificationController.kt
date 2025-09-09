package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.dto.NotificationDTO
import vn.example.ReadingAPI.dto.NotificationDTO.Companion.toNotification
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.NotificationRepository
import vn.example.ReadingAPI.service.NotificationService


@RestController
@RequestMapping("/api/notification")
class NotificationController(private var notificationSV:NotificationService) {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var notificationRepository: NotificationRepository

    @GetMapping("/all")
    fun getAllNotification():ResponseEntity<ResponseData>{
        val responseData = ResponseData()
        return try{
            val notifications = notificationSV.getAllNotification(0)
            if(notifications.isNotEmpty()){
                val notificationDTOs = notifications.map {
                    NotificationDTO.getNotificationAndBook(it)
                }
                responseData.status = 200
                responseData.message = "Get reading progress successfully"
                responseData.dataList = notificationDTOs
                ResponseEntity.ok(responseData)

            }else{
                responseData.status = 204
                responseData.message = "No notification"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }

        }catch(e: Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createNotification(@RequestBody notificationDTO: NotificationDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val notication = notificationDTO.toNotification(bookRepository)
            if (notication != null) {
                notificationRepository.save(notication)
                responseData.status = 200
                responseData.message = "Added notification successfully"
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
    @PostMapping("/delete")
    fun deleteNotification(@RequestBody notificationDTO: NotificationDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val notication = notificationDTO.toNotification(bookRepository)
            if (notication != null) {
                notificationRepository.delete(notication)
                responseData.status = 200
                responseData.message = "Delete notification successfully"
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
    @PostMapping("/deleteByBookId")
    fun deleteByBookId(@RequestBody requestBody: Map<String, Int>): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val id = requestBody["id"]

        return try {
            if (id != null) {
                notificationSV.deleteAllByBook(id)
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