package vn.example.readingapplication.activity.user.bookauthor

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
import vn.example.readingapplication.adapter.home.category.adapter.CategoryBookAuthorAdapter
import vn.example.readingapplication.databinding.ActivityCategoriesBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class BookAuthorHomeActivity : AppCompatActivity(), CategoryBookAuthorAdapter.OnItemClickListener {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var adapter: CategoryBookAuthorAdapter
    private val bookList =mutableListOf<Book>()
    private var authorName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authorId = intent.getIntExtra("AUTHOR_ID",-1)
        Log.d("INF_AUKEY","$authorId")
        authorName = intent.getStringExtra("AUTHOR_NAME").toString()
        binding.txtCategoryType.text = authorName.uppercase()
        binding.btnExit.setOnClickListener{
            finish()
        }
        binding.rvListCategory.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        adapter = CategoryBookAuthorAdapter(bookList, this)
        binding.rvListCategory.adapter = adapter
        fetchBookAuthor(authorId)

    }
    private fun fetchBookAuthor(authorId: Int) {
                            val apiService = RetrofitClient.getClient().create(ApiService::class.java)
                            apiService.getSearchAuthorListBook(authorId).enqueue(object : Callback<ResultResponse<Book>> {

                                @SuppressLint("NotifyDataSetChanged")
                                override fun onResponse(
                                    call: Call<ResultResponse<Book>>,
                                    response: Response<ResultResponse<Book>>
                                ) {
                                    if (response.isSuccessful) {
                                        val books = response.body()?.dataList?.let { books ->
                                            bookList.clear()
                                            bookList.addAll(books)
                                            adapter.notifyDataSetChanged()
                    }
                } else {
                    binding.txtNotification.visibility = View.VISIBLE
                    binding.txtNotification.text = getString(R.string.text_no_category)+"${authorName.uppercase()}"
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("ERRO,","${t.message}")
            }
        })
    }
    override fun onItemClick(book: Book) {
        val intent = Intent(this, ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", book.id)
        startActivity(intent)
    }
}
