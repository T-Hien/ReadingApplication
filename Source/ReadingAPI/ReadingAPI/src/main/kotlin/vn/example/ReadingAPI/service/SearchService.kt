package vn.example.ReadingAPI.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vn.example.ReadingAPI.model.Search
import vn.example.ReadingAPI.repository.SearchRepository

@Service
class SearchService(private var searchRepository: SearchRepository){
    @Transactional
    fun getByUsernameAndType(username:String, type:String):List<Search>{
        return searchRepository.findByAuserUsernameAndType(username, type)
    }
}
