package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.AuthorDTO
import vn.example.ReadingAPI.dto.BookDTO
import vn.example.ReadingAPI.dto.CategoryDTO
import vn.example.ReadingAPI.dto.CategoryDTO.Companion.toCategory
import vn.example.ReadingAPI.dto.SettingDTO
import vn.example.ReadingAPI.dto.SettingDTO.Companion.toSetting2
import vn.example.ReadingAPI.repository.SettingRepository
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.AuthorService
import vn.example.ReadingAPI.service.SettingService

@RestController
@RequestMapping("/api/setting")
class SettingController(private var settingSV:SettingService) {
    @Autowired
    private lateinit var settingRepository: SettingRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/{username}")
    fun getSettingByUsername(@PathVariable username: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val settings = settingSV.getSetting(username)
            val settingDTOs = SettingDTO.from(settings)
            responseData.status = 200
            responseData.message = "Get Setting Successful"
            responseData.data = settingDTOs
            ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/create")
    fun createSetting(@RequestBody settingDTO: SettingDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val setting = settingDTO.toSetting2(userRepository)
            settingRepository.save(setting)
            responseData.status = 200
            responseData.message = "Added/Update setting successfully"
            ResponseEntity(responseData, HttpStatus.CREATED)

        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}