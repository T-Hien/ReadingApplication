package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.FavoriteDTO
import vn.example.ReadingAPI.dto.FavoriteDTO.Companion.toFavorite
import vn.example.ReadingAPI.dto.LikeDTO
import vn.example.ReadingAPI.dto.LikeDTO.Companion.toLike
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.FavoriteRepository
import vn.example.ReadingAPI.repository.LikeRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.FavoriteService
import vn.example.ReadingAPI.service.LikeService

@RestController
@RequestMapping("/api/like")
class LikeController(private var likeSV:LikeService) {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var likeRepository: LikeRepository

    @PostMapping("/create")
    fun createLike(@RequestBody likeDTO: LikeDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val like = likeDTO.toLike(bookRepository,userRepository)
            if (like != null) {
                likeRepository.save(like)
                responseData.status = 200
                responseData.message = "Added favorite successfully"
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
    @GetMapping("/find")
    fun findLike(@RequestParam bookId:Int,@RequestParam username:String): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val like = likeSV.findByBookAndUser(bookId, username)
            val likesDTOs = LikeDTO.getLike(like)
            responseData.status = 200
            responseData.message = "Deleted favorite successfully"
            responseData.data = likesDTOs
            ResponseEntity(responseData, HttpStatus.OK)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @GetMapping("/findByBook")
    fun findLikeByBook(@RequestParam bookId:Int): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val like = likeSV.findByBook(bookId)
            val likesDTOs = LikeDTO.getListLike(like)
            responseData.status = 200
            responseData.message = "List like successfully"
            responseData.dataList = likesDTOs
            ResponseEntity(responseData, HttpStatus.OK)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //Danh sách sách đã yêu thích
    @GetMapping("/findByUser")
    fun findLikeByUser(@RequestParam username:String): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val like = likeSV.findByUsername(username)
            val likesDTOs = LikeDTO.getListLikeByUser(like)
            responseData.status = 200
            responseData.message = "List like successfully"
            responseData.dataList = likesDTOs
            ResponseEntity(responseData, HttpStatus.OK)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/delete")
    fun deleteLike(@RequestParam bookId:Int,@RequestParam username:String): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
//            val like = likeDTO.toLike(bookRepository,userRepository)
//            if (like != null) {
            likeSV.deleteByBookAndUser(bookId,username)
            responseData.status = 200
            responseData.message = "Deleted favorite successfully"
            ResponseEntity(responseData, HttpStatus.OK)
//            } else {
//                responseData.status = 204
//                responseData.message = "Book not found"
//                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
//            }
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
                likeSV.deleteAllByBook(id)
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