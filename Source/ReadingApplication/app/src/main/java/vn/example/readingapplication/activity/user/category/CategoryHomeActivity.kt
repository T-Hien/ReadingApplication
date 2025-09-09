package vn.example.readingapplication.activity.user.category

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.home.category.adapter.CategoryAdapter
import vn.example.readingapplication.databinding.ActivityCategoriesBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CategoryHomeActivity : AppCompatActivity(), CategoryAdapter.OnItemClickListener {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var adapter: CategoryAdapter
    private val categoryList =mutableListOf<BookCategory>()
    private val bookMap = mutableMapOf<Category, MutableList<Book>>()
    private var categoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryId = intent.getIntExtra("CATEGORY_ID",-1)
        categoryName = intent.getStringExtra("CATEGORY_NAME").toString()
        val categoryDescription = intent.getStringExtra("CATEGORY_DESCRIPTION").toString()

        if(categoryDescription.isNotEmpty()&&categoryDescription !=null){
            binding.txtDescription.visibility = View.VISIBLE
            binding.txtDescription.text = categoryDescription
        }
        binding.txtCategoryType.text = categoryName.uppercase()
        binding.btnExit.setOnClickListener{
            finish()
        }
        binding.rvListCategory.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        adapter = CategoryAdapter(categoryList, this)
        binding.rvListCategory.adapter = adapter
        fetchBookCategory(categoryId)

    }
    private fun fetchBookCategory(genres: Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookByCategoryId(genres).enqueue(object : Callback<ResultResponse<BookCategory>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<BookCategory>>,
                response: Response<ResultResponse<BookCategory>>
            ) {
                if (response.isSuccessful) {
                    val books = response.body()?.dataList?.let { bookcategories ->
                        categoryList.clear()
                        categoryList.addAll(bookcategories)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    binding.txtNotification.visibility = View.VISIBLE
                    binding.txtNotification.text = getString(R.string.text_no_category)+" ${categoryName.uppercase()}"
                }
            }

            override fun onFailure(call: Call<ResultResponse<BookCategory>>, t: Throwable) {
                Log.d("ERRO","${t.message}")
            }
        })
    }


    override fun onItemClick(book: BookCategory) {
        val intent = Intent(this, ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", book.abook?.id)
        startActivity(intent)
    }
}
