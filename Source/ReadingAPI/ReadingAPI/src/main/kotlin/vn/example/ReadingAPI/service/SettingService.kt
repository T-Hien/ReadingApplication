package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import vn.example.ReadingAPI.model.Author
import vn.example.ReadingAPI.model.Setting
import vn.example.ReadingAPI.repository.AuthorRepository
import vn.example.ReadingAPI.repository.DetailAuthorRepository
import vn.example.ReadingAPI.repository.SettingRepository

@Service
class SettingService(private val settingRepository: SettingRepository) {
    fun getSetting(username:String):Setting{
        return settingRepository.findByUser_Username(username)
    }

}