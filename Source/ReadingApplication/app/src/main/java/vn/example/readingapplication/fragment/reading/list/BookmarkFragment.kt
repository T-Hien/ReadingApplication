package vn.example.readingapplication.fragment.reading.list

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.reading.list.BookmarkAdapter
import vn.example.readingapplication.databinding.FragmentBookChapterBinding
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class BookmarkFragment : Fragment(), BookmarkAdapter.OnItemClickListener {

    private lateinit var binding: FragmentBookChapterBinding
    private lateinit var adapter: BookmarkAdapter
    private val itemList = mutableListOf<Bookmarks>()
    private var username = ""
    private var bookId: Int = 0
    private var tyle_book :String =""
    private var chapter_num = 0
    private var type = ""


    companion object {
        fun newInstance(bookId: Int,username:String,chapternub:Int,tyle:String,type:String): BookmarkFragment {
            val fragment = BookmarkFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", bookId)
            args.putString("USERNAME", username)
            args.putInt("CHAPTER_NUMBER", chapternub)
            args.putString("TYLE_BOOK", tyle)
            args.putString("TYPE_BOOKMARK", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt("KEY_DATA", 2)
            username = it.getString("USERNAME").toString()
            chapter_num = it.getInt("CHAPTER_NUMBER")
            tyle_book = it.getString("TYLE_BOOK").toString()
            type = it.getString("TYPE_BOOKMARK").toString()
            Log.d("DATA:", "$bookId")
        }
    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = requireContext().getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "0").toString()
        if(!username.equals("0")){
            binding.rcvBookChapter.visibility = View.VISIBLE

            adapter = BookmarkAdapter(itemList, this)
            binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.rcvBookChapter.adapter = adapter
            getListBookmark()
        }
        else{
            binding.rcvBookChapter.visibility = View.GONE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookChapterBinding.inflate(inflater, container, false)

        adapter = BookmarkAdapter(itemList, this)
        binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
        binding.rcvBookChapter.adapter = adapter
        getListBookmark()
        return binding.root
    }

    private fun getListBookmark() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookmarkByBook(chapter_num, type,bookId,username).enqueue(object : Callback<ResultResponse<Bookmarks>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Bookmarks>>,
                response: Response<ResultResponse<Bookmarks>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { bookmarks ->
                        itemList.clear()
                        itemList.addAll(bookmarks)
                        adapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(context, "No bookmarks found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Bookmarks>>, t: Throwable) {
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("bookmark_ERROR:", "${t.message}")
            }
        })
    }
    interface OnBackPressedListener {
        fun onBackPressed2(data: String)
    }

    private var backPressedListener: OnBackPressedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressedListener = context as? OnBackPressedListener
    }

    override fun onDetach() {
        super.onDetach()
        backPressedListener = null
    }

    override fun onItemClick(bookmark: Bookmarks) {
        Log.d("INFF","${bookmark.progress_percentage}")
        backPressedListener?.onBackPressed2(bookmark.position.toString())

    }
}
