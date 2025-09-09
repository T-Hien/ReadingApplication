package vn.example.readingapplication.fragment.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.SharedViewModel
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.search.SearchBookAdapter
import vn.example.readingapplication.adapter.search.SearchBookKeywordAdapter
import vn.example.readingapplication.databinding.FragmentSearchBookBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Search
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class SearchBookFragment : Fragment(), SearchBookAdapter.OnItemClickListener, SearchBookKeywordAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSearchBookBinding
    private lateinit var adapter: SearchBookAdapter
    private lateinit var adapter2: SearchBookKeywordAdapter
    private lateinit var viewModel: SharedViewModel
    private val itemList = mutableListOf<Search>()
    private val itemList2 = mutableListOf<Book>()
    private var username = ""
    private val type = "sach"
    private var keyword: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val username2 = sharedPreferences?.getString("username", null)
        if (username2 != null) {
            username = username2
        }
        setupInitialSearch()
        getSearch()
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.data.observe(viewLifecycleOwner) { data ->
            keyword = data
            binding.txtNotification.visibility = View.GONE
            if(keyword.isEmpty()){
                binding.txtSearchBook.visibility = View.VISIBLE
                setupInitialSearch()
                getSearch()
            }
            else{
                binding.txtSearchBook.visibility = View.GONE
                setupKeywordSearch()
                fetchBooks(keyword)
            }

        }
    }

    private fun setupInitialSearch() {
        adapter = SearchBookAdapter(itemList, this)
        if (isAdded) {

            val layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            binding.rcvSearchBook.layoutManager = layoutManager
            binding.rcvSearchBook.adapter = adapter
        }
    }

    private fun setupKeywordSearch() {
        adapter2 = SearchBookKeywordAdapter(itemList2, this)
        if (isAdded) {
            val layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            binding.rcvSearchBook.layoutManager = layoutManager
            binding.rcvSearchBook.adapter = adapter2
        }
    }

    private fun someFunction(data:String) {
        viewModel.data.value = data
    }

    private fun getSearch() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getSearchType(username, type).enqueue(object : Callback<ResultResponse<Search>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Search>>,
                response: Response<ResultResponse<Search>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { search ->
                        itemList.clear()
                        itemList.addAll(search)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Search>>, t: Throwable) {
                Log.d("ERRORSearchBook", "${t.message}")
            }
        })
    }

    private fun fetchBooks(keyword: String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getSearchBook(keyword).enqueue(object : Callback<ResultResponse<Book>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    saveSearchHistory("search", keyword)
                    if(username!=null){
                        createSearch()
                    }
                    val book = response.body()?.dataList
                    book?.let { books ->
                        itemList2.clear()
                        itemList2.addAll(books)
                        adapter2.notifyDataSetChanged()

                    }
                } else {
                    itemList2.clear()
                    binding.txtNotification.visibility = View.VISIBLE
                    binding.txtNotification.text = getString(R.string.text_no_book_find)

                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
            }
        })
    }
    private fun createSearch() {
        val search = Search(
            id = null,
            type = "sach",
            auser = User(username = username),
            keyword = keyword
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createSearch(search).enqueue(object : Callback<ResultResponse<Search>> {
            override fun onResponse(
                call: Call<ResultResponse<Search>>,
                response: Response<ResultResponse<Search>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Search>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun saveSearchHistory(type: String, keyword: String) {
        val sharedPreferences = requireContext().getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Lấy danh sách cũ
        val history = sharedPreferences.getStringSet(type, mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        // Thêm từ khóa mới (loại bỏ trùng lặp)
        if (keyword.isNotBlank()) {
            history.add(keyword)
        }

        // Giới hạn danh sách tối đa
        if (history.size > 10) {
            history.remove(history.first())
        }

        // Ghi lại danh sách
        editor.putStringSet(type, history)
        editor.apply()
    }


    override fun onItemClick(search: Search) {
        someFunction(search.keyword)
    }

    override fun onItemClick(book: Book) {
        val bookId = book.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)
    }
}
