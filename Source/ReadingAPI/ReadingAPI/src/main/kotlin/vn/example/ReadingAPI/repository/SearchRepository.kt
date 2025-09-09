package vn.example.ReadingAPI.repository


import org.springframework.data.jpa.repository.JpaRepository
import vn.example.ReadingAPI.model.Search

interface SearchRepository : JpaRepository<Search, Int>{
    fun findByAuserUsernameAndType(username:String, type:String):List<Search>
    fun findByKeyword(keyword:String):Search?
}
