package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.AuthorDTO
import vn.example.ReadingAPI.dto.AuthorDTO.Companion.toAuthor
import vn.example.ReadingAPI.dto.AuthorFavoriteDTO
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.CategoryDTO
import vn.example.ReadingAPI.dto.CategoryDTO.Companion.toCategory
import vn.example.ReadingAPI.repository.AuthorRepository
import vn.example.ReadingAPI.repository.DetailAuthorRepository
import vn.example.ReadingAPI.service.AuthorService

@RestController
@RequestMapping("/api/author")
class AuthorController(private var authorSV:AuthorService) {
    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Autowired
    private  lateinit var detailAuthorRepository: DetailAuthorRepository
    @GetMapping("book/{bookId}")
    fun getAuthorsByBookId(@PathVariable bookId: Int): ResponseEntity<List<AuthorDTO>> {
        val authors = authorSV.getAuthorsByBookId(bookId)
        val authorDTOs = authors.map { AuthorDTO(
            id = it.id,
            name = it.name,
            birth_date = it.birth_date,
            death_date = it.death_date,
            listDetailAuthor = emptyList()
        ) }
        return ResponseEntity.ok(authorDTOs)
    }

    @GetMapping("search")
    fun searchAuthors(@RequestParam keyword: String):ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val authors = authorSV.searchAuthorsByName(keyword)
            if (authors .isNotEmpty()) {
                val authorDTOs = authors .map { AuthorDTO.from(it) }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = authorDTOs
                ResponseEntity.ok(responseData)
            }else{
                responseData.status = 204
                responseData.message = "No author found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)

            }

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("all")
    fun getAll():ResponseEntity<ResponseData>{
        val responseData = ResponseData()
        return try{
            val authors = authorRepository.findAll()
            if(authors.isNotEmpty()){
                val authorDTOs = authors.map {
                    AuthorDTO.from(it)
                }
                responseData.status = 200
                responseData.message = "Get Category in successfully"
                responseData.dataList = authorDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 500
                responseData.message = "No category found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("item")
    fun getAuthor(@RequestParam id:Int):ResponseEntity<ResponseData>{
        val responseData = ResponseData()
        return try{
            val authors = authorRepository.findById(id)
            if(authors.isPresent){
                val authorDTOs = authors.map {
                    AuthorDTO.from(it)
                }
                responseData.status = 200
                responseData.message = "Get Author in successfully"
                responseData.data = authorDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 500
                responseData.message = "No author found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createAuthor(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val author = authorDTO.toAuthor()
            authorRepository.save(author)
            responseData.status = 200
            responseData.message = "Added author successfully"
            ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @DeleteMapping("/delete")
    fun deleteAuthor(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            // Lấy ID của tác giả từ DTO
            val authorId = authorDTO.id ?: return ResponseEntity(
                ResponseData(400, "Author ID is missing"),
                HttpStatus.BAD_REQUEST
            )

            // Kiểm tra tác giả có tồn tại trong cơ sở dữ liệu hay không
            val existingAuthor = authorRepository.findById(authorId)
            if (existingAuthor.isEmpty) {
                // Nếu không tồn tại, trả về thông báo lỗi
                responseData.status = 404
                responseData.message = "Author not found"
                return ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }

            // Nếu tác giả tồn tại, tiến hành xóa
            authorRepository.deleteById(authorId)
            responseData.status = 200
            responseData.message = "Author deleted successfully"
            ResponseEntity(responseData, HttpStatus.OK)

        } catch (e: Exception) {
            // Xử lý lỗi ngoại lệ khi xóa
            responseData.status = 500
            responseData.message = "Failed to delete author: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/delete")
    fun deleteAuthor2(@RequestBody authorDTO: AuthorDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            // Lấy ID của tác giả từ DTO
            val authorId = authorDTO.id ?: return ResponseEntity(
                ResponseData(400, "Author ID is missing"),
                HttpStatus.BAD_REQUEST
            )

            // Kiểm tra tác giả có tồn tại trong cơ sở dữ liệu hay không
            val existingAuthor = authorRepository.findById(authorId)
            if (existingAuthor.isEmpty) {
                // Nếu không tồn tại, trả về thông báo lỗi
                responseData.status = 404
                responseData.message = "Author not found"
                return ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            }

            // Nếu tác giả tồn tại, tiến hành xóa
            authorRepository.deleteById(authorId)
            responseData.status = 200
            responseData.message = "Author deleted successfully"
            ResponseEntity(responseData, HttpStatus.OK)

        } catch (e: Exception) {
            // Xử lý lỗi ngoại lệ khi xóa
            responseData.status = 500
            responseData.message = "Failed to delete author: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }




    @GetMapping("/favorites")
    fun getTopAuthorsByFavorites(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val authors = authorSV.getTopAuthorsByFavorites()
        responseData.dataList = authors
        responseData.status = 200
        return ResponseEntity(responseData, HttpStatus.OK)
    }

    @GetMapping("count")
    fun getCountAuthor(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val count = authorRepository.getCount()
            responseData.status = 200
            responseData.message = "Get user successfully"
            responseData.dataNum = count
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

}