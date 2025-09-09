package vn.example.readingapplication.fragment.home

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.CategoriesActivity
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.home.ListGenresBook1Adapter
import vn.example.readingapplication.adapter.home.ListGenresBook2Adapter
import vn.example.readingapplication.adapter.home.ListShowBooksAdapter
import vn.example.readingapplication.adapter.home.RecentlyBooksAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentHomeDiscoverBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class DiscoverFragment : BaseFragment(),
    ListShowBooksAdapter.OnItemClickListener,
    RecentlyBooksAdapter.OnItemClickListener,
    ListGenresBook1Adapter.OnItemClickListener{

    private lateinit var binding: FragmentHomeDiscoverBinding
    private lateinit var booksAdapter: ListShowBooksAdapter
    private lateinit var apdapterRecent: RecentlyBooksAdapter
    private lateinit var adapter: ListGenresBook1Adapter
    private lateinit var adapter2:ListGenresBook2Adapter
    private lateinit var adapter3:ListGenresBook2Adapter
    private lateinit var adapter4:ListGenresBook2Adapter

    private val bookList = mutableListOf<Book>()
    private val bookmarkList = mutableListOf<ReadingProgress>()
    private val bookcategoryList1 = mutableListOf<BookCategory>()
    private val bookcategoryList2 = mutableListOf<BookCategory>()
    private val bookcategoryList3 = mutableListOf<BookCategory>()
    private val bookcategoryList4 = mutableListOf<BookCategory>()

    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDiscoverBinding.inflate(inflater, container, false)
        val welcomeMessage = getString(R.string.welcome_message)
        Log.d("INF_LAGUAGE","$welcomeMessage")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()

        binding.btnAllCategory.setOnClickListener{
            val intent = Intent(context, CategoriesActivity::class.java)
            startActivity(intent)
        }
        getRecycleView()
    }
    override fun onResume() {
        super.onResume()
        Log.d("DiscoverFragment", "Fragment resumed, reloading data...")
        reloadData()
    }
    private fun reloadData() {
        fetchBooks()
        getAllLibrary()
        fetchBookCategory(5, bookcategoryList1, adapter)
        fetchBookCategory(6, bookcategoryList2, adapter2)
        fetchBookCategory(1, bookcategoryList3, adapter3)
        fetchBookCategory(3, bookcategoryList4, adapter4)
    }

    private fun getRecycleView() {
        binding.rcvShowBooks.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        booksAdapter = ListShowBooksAdapter(bookList,this)
        binding.rcvShowBooks.adapter = booksAdapter
        fetchBooks()
        binding.rcvRecentlylistBook.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)
        apdapterRecent = RecentlyBooksAdapter(bookmarkList,this)
        binding.rcvRecentlylistBook.adapter =  apdapterRecent
//        getBookmarkByUsername(username)
        getAllLibrary()

        adapter = ListGenresBook1Adapter(bookcategoryList1,this)
        binding.rcvListBook1.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvListBook1.adapter = adapter
        fetchBookCategory(5, bookcategoryList1, adapter)
        adapter2 = ListGenresBook2Adapter(bookcategoryList2, this)
        binding.rcvListBook2.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)
        binding.rcvListBook2.adapter = adapter2
        fetchBookCategory(6, bookcategoryList2, adapter2)

        adapter3 = ListGenresBook2Adapter(bookcategoryList3, this)
        binding.rcvListBook3.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)
        binding.rcvListBook3.adapter = adapter3
        fetchBookCategory(1, bookcategoryList3, adapter3)

        adapter4 = ListGenresBook2Adapter(bookcategoryList4, this)
        binding.rcvListBook4.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)
        binding.rcvListBook4.adapter = adapter4
        fetchBookCategory(3, bookcategoryList4, adapter4)
    }

    private fun fetchBookCategory(genres: Int, categoryList: MutableList<BookCategory>, adapter: RecyclerView.Adapter<*>) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookByCategoryId(genres).enqueue(object : Callback<ResultResponse<BookCategory>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<BookCategory>>,
                response: Response<ResultResponse<BookCategory>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { bookcategories ->
                        categoryList.clear()
                        categoryList.addAll(bookcategories)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<BookCategory>>, t: Throwable) {
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchBooks() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllBooks().enqueue(object : Callback<ResultResponse<Book>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        bookList.clear()
                        bookList.addAll(books)
                        booksAdapter.notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("Discover1", "${t.message}")
            }
        })
    }
    private fun getAllLibrary() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookReadingAll(username).enqueue(object : Callback<ResultResponse<List<ReadingProgress>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<List<ReadingProgress>>>,
                response: Response<ResultResponse<List<ReadingProgress>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { bookmarks ->
                        bookmarkList.clear()
                        bookmarks.flatten().let {
                            bookmarkList.addAll(it)
                        }
                        binding.lnRecently.visibility = View.VISIBLE
                        apdapterRecent.notifyDataSetChanged()
                        if (response.body()?.dataList.isNullOrEmpty()) {
                            binding.lnRecently.visibility = View.GONE
                        }
                    }
                } else {
                    binding.lnRecently.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<ResultResponse<List<ReadingProgress>>>, t: Throwable) {
                Log.d("ERRORLibraryAll", "${t.message}")
            }
        })
    }

    override fun onItemClick(book: Book) {
        val bookId = book.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)

    }

    override fun onItemClick(bookmark: ReadingProgress) {
        val bookId = bookmark.abook?.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)
    }

    override fun onItemClick(bookCategory: BookCategory) {
        val bookId = bookCategory.abook?.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)
    }

}
