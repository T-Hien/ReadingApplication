package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.model.User
import vn.example.ReadingAPI.repository.UserRepository
import java.util.*

@Service
public class UserService(private var userRepository: UserRepository){

    fun signIn(username: String, password: String): ResponseData {
        val responseData = ResponseData()
        val userList = userRepository.findAll()
        for (user in userList) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return if (user.status == 0) {
                    responseData.status = 200
                    responseData.dataNum = user.role!!
                    responseData.message = "Login successfully"
                    responseData
                } else {
                    responseData.status = 403
                    responseData.message = "Account is locked"
                    responseData
                }
            }
        }
        responseData.status = 500
        responseData.message = "Wrong username or password!"
        return responseData
    }

    fun getUser(username: String): User {
        return userRepository.findByUsername(username)
    }
    fun getUserByRole(role:Int):List<User>{
        return userRepository.findByRole(role)
    }

}
