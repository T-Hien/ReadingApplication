package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.NoteDTO
import vn.example.ReadingAPI.dto.NoteDTO.Companion.toNote
import vn.example.ReadingAPI.dto.SearchDTO
import vn.example.ReadingAPI.dto.SearchDTO.Companion.toSearch
import vn.example.ReadingAPI.repository.SearchRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.SearchService


@RestController
@RequestMapping("/api/search")
class SearchController(private var searchService: SearchService) {
    @Autowired
    private lateinit var userRps:UserRepository
    @Autowired
    private lateinit var searchRepository: SearchRepository

    //Hiện tìm kiếm của người dùng đăng nhập
    @GetMapping("username/{username}/type/{type}")
    fun getSearchByType(@PathVariable username: String, @PathVariable type: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val searches = searchService.getByUsernameAndType(username, type)
            if (searches.isNotEmpty()) {
                val searchDTOs = searches.map {
                    SearchDTO.getSearch(it)
                }
                responseData.status = 200
                responseData.message = "Get reading progress successfully"
                responseData.dataList = searchDTOs
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


    //Thêm tìm kiếm mới
    @PostMapping("/create")
    fun createNote(@RequestBody searchDTO: SearchDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val search = searchDTO.toSearch(userRps)
            val existingSearch = search?.keyword?.let { searchRepository.findByKeyword(it) }
            if (existingSearch != null) {
                responseData.status = 209
                responseData.message = "Keyword already exists"
                ResponseEntity(responseData, HttpStatus.CONFLICT)
            } else {
                if (search != null) {
                    searchRepository.save(search)
                }
                responseData.status = 200
                responseData.message = "Added search successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}