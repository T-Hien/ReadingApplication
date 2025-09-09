package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.FavoriteDTO
import vn.example.ReadingAPI.dto.FavoriteDTO.Companion.toFavorite
import vn.example.ReadingAPI.repository.BookRepository
import vn.example.ReadingAPI.repository.FavoriteRepository
import vn.example.ReadingAPI.service.FavoriteService

@RestController
@RequestMapping("/api/favorite")
class FavoriteController(private var favoriteSV: FavoriteService) {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var favoriteRepository: FavoriteRepository

    @GetMapping("/{bookId}")
    fun getFavoriteByBookId(@PathVariable bookId:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val favorites = favoriteSV.getFavoriteByBookId(bookId)
            if (favorites != null) {
                val favoriteDTOs = FavoriteDTO.fromFavorite(favorites)
                responseData.status = 200
                responseData.message = "Get favorite successfully"
                responseData.data = favoriteDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No favorite found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        }catch (e: Exception) {
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    //Nổi bật
    @GetMapping("/top20")
    fun getTop20Favorites(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val favorites = favoriteSV.getTop20Favorites()
            if (favorites != null) {
                val favoriteDTOs = favorites.map { favorite ->
                    FavoriteDTO.getFavoriteTop(favorite)
                }
                responseData.status = 200
                responseData.message = "Get favorite successfully"
                responseData.dataList = favoriteDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No favorite found"
                ResponseEntity.status(HttpStatus.NO_CONTENT).body(responseData)
            }
        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createFavorite(@RequestBody favoriteDTO: FavoriteDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val favorite = favoriteDTO.toFavorite(bookRepository)
            if (favorite != null) {
                favoriteRepository.save(favorite)
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
    @PostMapping("/delete")
    fun deleteFavorite(@RequestBody favoriteDTO: FavoriteDTO): ResponseEntity<Any> {
        val responseData = ResponseData()
        return try {
            val favorite = favoriteDTO.toFavorite(bookRepository)
            if (favorite != null) {
                favorite.id?.let { favoriteRepository.deleteById(it) }
                responseData.status = 200
                responseData.message = "Deleted favorite successfully"
                ResponseEntity(responseData, HttpStatus.OK)
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
        val book_id = requestBody["id"]
        return try {
            if (book_id != null) {
                favoriteSV.deleteAllByBook(book_id)
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