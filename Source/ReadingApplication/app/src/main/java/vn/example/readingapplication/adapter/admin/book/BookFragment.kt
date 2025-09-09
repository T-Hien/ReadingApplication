package vn.example.readingapplication.adapter.admin.book

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.admin.book.AddBookActivity
import vn.example.readingapplication.databinding.FragmentHomeFavoritesBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class BookFragment : Fragment(),
    BookManagementAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeFavoritesBinding
    private val listUser = mutableListOf<User>()
    private var active:Int = 0
    private lateinit var adapter: BookManagementAdapter
    private val bookList = mutableListOf<Book>()

    companion object {
        fun newInstance(bookId: Int): BookFragment {
            val fragment = BookFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", bookId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            active = it.getInt("KEY_DATA", -1)
        }
        binding = FragmentHomeFavoritesBinding.inflate(inflater, container, false)
        adapter = BookManagementAdapter(bookList)
        binding.rcvFavoritesBook.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvFavoritesBook.adapter = adapter
        fetchBooks()
        binding.lnAdd.visibility = View.VISIBLE
        if(active==1){
            binding.lnTxtAdd.visibility = View.GONE
        }
        binding.btnAdd.setOnClickListener(){
            val intent = Intent(context,AddBookActivity::class.java)
            intent.putExtra("Active",active)
            startActivity(intent)
        }

        return binding.root
    }

    fun reloadData() {
        fetchBooks()
    }


    private fun fetchBooks() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllBooksAd2(active).enqueue(object : Callback<ResultResponse<Book>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        bookList.clear()
                        bookList.addAll(books)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("Discover1", "${t.message}")
            }
        })
    }

    override fun onItemClick(book: Book) {
    }
}
