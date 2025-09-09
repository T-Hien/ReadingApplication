package vn.example.ReadingAPI.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.*
import vn.example.ReadingAPI.dto.SettingDTO.Companion.toSetting2
import vn.example.ReadingAPI.dto.UserDTO.Companion.toUser
import vn.example.ReadingAPI.model.User
import vn.example.ReadingAPI.repository.UserRepository
import vn.example.ReadingAPI.service.UserService
import java.util.*

@RestController
@RequestMapping("/api/user")
class UserController(private val userRepository: UserRepository,
                     private var userService: UserService,
                     private val userRps : UserRepository) {


    @PostMapping("/login/{username}")
    fun login(@PathVariable("username") username: String, @RequestParam("password") password: String): ResponseEntity<String> {
        val user = userRepository.findByUsernameAndPassword(username, password)
        return if (user != null) {
            ResponseEntity.ok("Login successful")
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials")
        }
    }
    //Đăng nhập
    @PostMapping("/sign-in", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signIn(@RequestParam username: String, @RequestParam password: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            // Kiểm tra username có tồn tại trong cơ sở dữ liệu không
            val user = userRepository.findByUsername(username)

            // Nếu username tồn tại, kiểm tra mật khẩu
            if (!user.password.equals(password)) {
                responseData.status = 401
                responseData.message = "Incorrect password"
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseData)
            }
            // Kiểm tra trạng thái tài khoản
            return if (user.status == 0) {
                responseData.status = 200
                responseData.dataNum = user.role!!
                responseData.message = "Login successfully"
                ResponseEntity.ok(responseData)
            } else {
                responseData.status = 403
                responseData.message = "Account is locked"
                ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }


    @GetMapping("/{username}")
    fun getUser(@PathVariable username: String): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val user = userService.getUser(username)
            val userDTO = UserDTO.fromUser(user)
            responseData.status = 200
            responseData.message = "Get user successfully"
            responseData.data = userDTO
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("/all")
    fun getAllUser(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val user = userRepository.findAll()
            val userDTO = UserDTO.getUserRole(user)
            responseData.status = 200
            responseData.message = "Get all user successfully"
            responseData.dataList = listOf(userDTO)
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @PostMapping("/create")
    fun createUser(@RequestBody userDTO: UserDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val user = userDTO.toUser(userRepository)
                userRps.save(user)
                responseData.status = 200
                responseData.message = "Added/Update user successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
    //        }
            return ResponseEntity(responseData, HttpStatus.CREATED)
        } catch (e: Exception) {
            println("Exception during user creation: ${e.message}")
            e.printStackTrace()
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            return ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    //Lấy tất cả tài khoản theo quyền
    @GetMapping("findAll/role")
    fun getUserByRole(@RequestParam role: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val users = userService.getUserByRole(role)
            if (users.isNotEmpty()){
                val userDTOs = UserDTO.getUserRole(users)
                responseData.status = 200
                responseData.message = "Get user successfully"
                responseData.dataList = listOf(userDTOs)
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No user found"
                ResponseEntity.ok(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }
    @GetMapping("findAll2/role")
    fun getAllUserByRole(@RequestParam role: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val users = userRps.findByRole(role)
            if (users.isNotEmpty()){
                val userDTOs = users.map { UserDTO.fromUser2(it) }
                responseData.status = 200
                responseData.message = "Get user successfully"
                responseData.dataList = userDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No user found"
                ResponseEntity.ok(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("findAll3/role")
    fun getAllUserByRoleAndStatus(@RequestParam role: Int, @RequestParam status:Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val users = userRps.findByRoleAndStatus(role, status)
            if (users.isNotEmpty()){
                val userDTOs = users.map { UserDTO.fromUser2(it) }
                responseData.status = 200
                responseData.message = "Get user successfully"
                responseData.dataList = userDTOs
                ResponseEntity.ok(responseData)
            }
            else{
                responseData.status = 204
                responseData.message = "No user found"
                ResponseEntity.ok(responseData)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @GetMapping("count")
    fun getCountUserByRole(@RequestParam role: Int): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val userCount = userRps.getCountByRole(role)
            responseData.status = 200
            responseData.message = "Get user successfully"
            responseData.dataNum = userCount
            ResponseEntity.ok(responseData)
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

}