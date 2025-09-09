package vn.example.ReadingAPI.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import vn.example.ReadingAPI.controller.Model.ResponseData
import vn.example.ReadingAPI.dto.*
import vn.example.ReadingAPI.dto.CategoryDTO.Companion.toCategory
import vn.example.ReadingAPI.dto.SearchDTO.Companion.toSearch
import vn.example.ReadingAPI.model.Category
import vn.example.ReadingAPI.repository.CategoryRepository
import vn.example.ReadingAPI.service.CategoryService

@RestController
@RequestMapping("/api/category")
class CategoryController(private var categorySV:CategoryService) {
    @Autowired
    private lateinit var categoryRepository: CategoryRepository


    //Tất cả thể loại
    @GetMapping("all")
    fun getAll():ResponseEntity<ResponseData>{
        val responseData = ResponseData()
        return try{
            val categorys = categorySV.getAllCategory()
            if(categorys.isNotEmpty()){
                val categoryDTOs = categorys.map {
                    CategoryDTO.from(it)
                }
                responseData.status = 200
                responseData.message = "Get Category in successfully"
                responseData.dataList = categoryDTOs
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
    @GetMapping("/search")
    fun searchCategories(@RequestParam keyword: String):ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try{
            val categories = categorySV.searchCategoriesByName(keyword)
            if (categories.isNotEmpty()) {
                val categoryDTOs = categories.map { CategoryDTO.from(it) }

                responseData.status = 200
                responseData.message = "Get Book in successfully"
                responseData.dataList = categoryDTOs
                ResponseEntity.ok(responseData)
            }else{
                responseData.status = 204
                responseData.message = "No book found"
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
            }

        }catch(e:Exception){
            responseData.status = 500
            responseData.message = "An error occurred: ${e.message}"
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData)
        }
    }

    @PostMapping("/create")
    fun createCategory(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val category = categoryDTO.toCategory()
            val existingCategory = categoryRepository.findByName(category.name)

            if (existingCategory != null) {
                responseData.status = 409
                responseData.message = "Category already exists"
                ResponseEntity(responseData, HttpStatus.CONFLICT)
            } else {
                val data = categoryRepository.save(category)
                responseData.status = 200
                responseData.message = "Added category successfully"
//                responseData.data = data.id
                ResponseEntity(responseData, HttpStatus.CREATED)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/update")
    fun updateCategory(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val category = categoryDTO.toCategory()
            val existingCategory = categoryRepository.findById(category.id)

            if (existingCategory == null) {
                responseData.status = 409
                responseData.message = "No category found"
                ResponseEntity(responseData, HttpStatus.CONFLICT)
            } else {
                categoryRepository.save(category)
                responseData.status = 200
                responseData.message = "Update category successfully"
                ResponseEntity(responseData, HttpStatus.CREATED)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @DeleteMapping("/delete")
    fun deleteCategory(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val categoryId = categoryDTO.id
            val existingCategory = categoryRepository.findById(categoryId)

            if (existingCategory.isEmpty) {
                responseData.status = 404
                responseData.message = "No category found with the given ID"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            } else {
                categoryRepository.delete(existingCategory.get())
                responseData.status = 200
                responseData.message = "Deleted category successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @PostMapping("/delete")
    fun deleteCategory2(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val categoryId = categoryDTO.id
            val existingCategory = categoryRepository.findById(categoryId)

            if (existingCategory.isEmpty) {
                responseData.status = 404
                responseData.message = "No category found with the given ID"
                ResponseEntity(responseData, HttpStatus.NOT_FOUND)
            } else {
                categoryRepository.delete(existingCategory.get())
                responseData.status = 200
                responseData.message = "Deleted category successfully"
                ResponseEntity(responseData, HttpStatus.OK)
            }
        } catch (e: Exception) {
            responseData.status = 500
            responseData.message = "Fail: ${e.message}"
            ResponseEntity(responseData, HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
    @GetMapping("/favorites")
    fun getTopCategoriesByFavorites(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        val categories = categorySV.getTopCategoriesByFavorites()
        responseData.dataList = categories
        responseData.status = 200
        return ResponseEntity(responseData, HttpStatus.OK)

    }

    @GetMapping("item")
    fun getAuthor(@RequestParam id:Int):ResponseEntity<ResponseData>{
        val responseData = ResponseData()
        return try{
            val category = categoryRepository.findById(id)
            if(category.isPresent){
                val categoryDTOs = category.map {
                    CategoryDTO.from(it)
                }
                responseData.status = 200
                responseData.message = "Get Category in successfully"
                responseData.data = categoryDTOs
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

    @GetMapping("count")
    fun getCountCategory(): ResponseEntity<ResponseData> {
        val responseData = ResponseData()
        return try {
            val count = categoryRepository.getCount()
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