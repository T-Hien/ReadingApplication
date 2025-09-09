package vn.example.readingapplication.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.home.LatestAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentHomeLatestBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class LatestFragment : BaseFragment(),LatestAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeLatestBinding
    private lateinit var adapter: LatestAdapter
    private val bookList = mutableListOf<Book>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeLatestBinding.inflate(inflater, container, false)

        adapter = LatestAdapter(bookList,this)
        binding.rcvFavoritesBook.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvFavoritesBook.adapter = adapter
        getBook()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        Log.d("LatestFragment", "Fragment resumed, reloading data...")
        reloadData()
    }
    private fun reloadData() {
        getBook()
    }
    private fun getBook() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getNewBooks().enqueue(object : Callback<ResultResponse<Book>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        bookList.clear()
                        bookList.addAll(books)
                        }
                        adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("ERRORLastest:","Lastes:${t.message}")
            }
        })
    }

    override fun onItemClick(book: Book) {
        val bookId = book?.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)
    }
}
