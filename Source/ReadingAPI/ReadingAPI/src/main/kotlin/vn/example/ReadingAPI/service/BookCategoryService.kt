package vn.example.ReadingAPI.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import vn.example.ReadingAPI.model.BookCategory
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.repository.BookCategoryRepository

@Service
@Transactional
class BookCategoryService(private var bookcategoryRsp:BookCategoryRepository) {
    fun getBookCategoryById(id:Int):List<BookCategory>{
        return bookcategoryRsp.findByCategory_Id(id)
    }
    fun getBookCategoryByIdAndActive(id:Int,active:Int):List<BookCategory>{
        return bookcategoryRsp.findByCategory_IdAndAbook_Active(id,active)
    }
    fun getAll():List<BookCategory>{
        return bookcategoryRsp.findAll()
    }
    fun deleteByBookId(id: Int) {
        val bookCategories = bookcategoryRsp.findAllByAbook_Id(id)
        if (bookCategories.isEmpty()) {
            println("No BookCategory records found for bookId: $id")
        } else {
            bookcategoryRsp.deleteAll(bookCategories)
        }
    }
    fun deleteByBookId2(bookId: Int) {
        bookcategoryRsp.deleteByAbook_Id(bookId)
    }

}