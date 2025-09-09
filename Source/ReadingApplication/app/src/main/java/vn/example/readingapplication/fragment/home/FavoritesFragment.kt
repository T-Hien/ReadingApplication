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
import vn.example.readingapplication.adapter.home.FavoritesAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentHomeLatestBinding
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class FavoritesFragment : BaseFragment(),FavoritesAdapter.OnItemClickListener {

    private lateinit var binding: FragmentHomeLatestBinding
    private lateinit var adapter: FavoritesAdapter
    private val bookList = mutableListOf<Favorite>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeLatestBinding.inflate(inflater, container, false)
        adapter = FavoritesAdapter(bookList,this)
        binding.rcvFavoritesBook.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvFavoritesBook.adapter = adapter
        getBook()
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        Log.d("FavoritesFragment", "Fragment resumed, reloading data...")
        reloadData()
    }
    private fun reloadData() {
        getBook()
    }

    private fun getBook() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getFavoriteBooks().enqueue(object : Callback<ResultResponse<Favorite>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Favorite>>,
                response: Response<ResultResponse<Favorite>>
            ) {
                if (response.isSuccessful) {

                    response.body()?.dataList?.let { books ->

                        bookList.clear()
                        bookList.addAll(books)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Favorite>>, t: Throwable) {
                Log.d("ERROR:","Favorite:${t.message}")
            }
        })
    }

    override fun onItemClick(fav: Favorite) {
        val bookId = fav.abook?.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId) // Pass bookId here
        startActivity(intent)
    }
}
