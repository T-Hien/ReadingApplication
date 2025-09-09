package vn.example.readingapplication.activity.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.home.category.adapter.ListCategoryAdapter
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityCategoriesBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CategoriesActivity : BaseActivity(), ListCategoryAdapter.OnItemClickListener {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var adapter: ListCategoryAdapter
    private val categoryList = mutableListOf<Category>()
    private val bookMap = mutableMapOf<Category, MutableList<Book>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnExit.setOnClickListener{
            finish()
        }
        binding.rvListCategory.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        adapter = ListCategoryAdapter(categoryList, this, bookMap)
        binding.rvListCategory.adapter = adapter
        getListCategories()
    }

    private fun getListCategories() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getListCategory().enqueue(object : Callback<ResultResponse<Category>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        categoryList.addAll(categories)
                        Log.d("CategoryActivity", "$categoryList")
                        for (category in categoryList) {
                            category.id?.let { fetchBooks(it) }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
            }
        })
    }

    private fun fetchBooks(categoryId: Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookByCategoryId(categoryId).enqueue(object : Callback<ResultResponse<BookCategory>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<BookCategory>>,
                response: Response<ResultResponse<BookCategory>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { bookCategoriesList ->
                        bookCategoriesList.forEach { bookCategory ->
                            val category = bookCategory.category
                            val book = bookCategory.abook
                            if (category != null && book != null) {
                                bookMap.getOrPut(category) { mutableListOf() }.add(book)
                                Log.d("CategoríeActivity","${bookMap}")
                            }
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<BookCategory>>, t: Throwable) {
                Log.d("CategoríeActivity", "${t.message}")
            }
        })
    }

    override fun onItemClick(book: Book) {
        val intent = Intent(this, ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", book.id) // Pass bookId here
        startActivity(intent)
    }
}
