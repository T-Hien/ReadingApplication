package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import vn.example.ReadingAPI.model.Setting

interface SettingRepository : JpaRepository<Setting, Int>{
    fun findByUser_Username(username:String):Setting
}
