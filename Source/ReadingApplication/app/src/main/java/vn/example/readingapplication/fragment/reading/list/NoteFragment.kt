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
import vn.example.readingapplication.adapter.reading.list.NoteAdapter
import vn.example.readingapplication.databinding.FragmentBookChapterBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class NoteFragment : Fragment(), BookmarkAdapter.OnItemClickListener {

    private lateinit var binding: FragmentBookChapterBinding
    private lateinit var adapter: NoteAdapter
    private val itemList = mutableListOf<Bookmarks>()
    private var username = ""
    private var bookId: Int = 0
    private var tyle_book :String =""
    private var chapter_num = 0
    private var type = ""

    companion object {
        fun newInstance(bookId: Int,username:String,chapternub:Int,tyle:String,type:String): NoteFragment {
            val fragment = NoteFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", bookId)
            args.putString("USERNAME", username)
            args.putInt("CHAPTER_NUMBER", chapternub)
            args.putString("TYLE_BOOK", tyle)
            args.putString("TYPE_BOOKMARK", type)    //Bookmark or Note
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookChapterBinding.inflate(inflater, container, false)
        Log.d("NOTE_INFOR:","NOTEFFRAGMENT_newInstance: ${bookId}+$username+$chapter_num+$tyle_book+$type")

        if(!username.equals("0")){
            adapter = NoteAdapter(itemList, this)
            binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.rcvBookChapter.adapter = adapter
            getListNote()
        }
        else{
            binding.rcvBookChapter.visibility = View.GONE
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = requireContext().getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        username = sharedPreferences.getString("username", "0").toString()
        if(!username.equals("0")){
            binding.rcvBookChapter.visibility = View.VISIBLE
            adapter = NoteAdapter(itemList, this)
            binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
            binding.rcvBookChapter.adapter = adapter
            getListNote()
        }
        else{
            binding.rcvBookChapter.visibility = View.GONE
        }
    }
    private var bookmark:MutableList<Bookmarks> = mutableListOf()

    private fun noteToBookmark(note: Note): Bookmarks {
        return Bookmarks(
            id = note.id,
            note = Note(id = note.id, content = note.content),
            chapternumber = note.chapternumber,
            type = note.bookmark?.type,
            position = note.bookmark?.position?:0,
            auser = note.auser?.username?.let { User(username = it) },
            abook = Book(id = note.abook?.id),
            progress_percentage = note.bookmark?.progress_percentage,
            createdAt = note.createdAt
        )
    }

    private fun getListNote() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getNoteByChap(type, bookId, username,chapter_num).enqueue(object : Callback<ResultResponse<Note>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Note>>,
                response: Response<ResultResponse<Note>>
            ) {
                Log.d("NOTE_API", "Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { notes ->
                        Log.d("NOTE_API", "Notes Received: $notes")
                        val bookmarks = notes.map { note ->
                            noteToBookmark(note).also {
                                Log.d("NOTE_TO_BOOKMARK", "Converted: $it")
                            }
                        }
                        bookmark.clear()
                        bookmark.addAll(bookmarks)

                        itemList.clear()
                        itemList.addAll(bookmark)
                        Log.d("NOTE_LIST", "Final ItemList: $itemList")
                        adapter.notifyDataSetChanged()
                        Log.d("ADAPTER_CHECK", "Adapter Item Count: ${adapter.itemCount}")
                    } ?: run {
                        Log.e("NOTE_API", "No notes found")
                    }
                } else {
                    Log.e("NOTE_API", "Failed to fetch notes")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Log.e("NOTE_API", "Error: ${t.message}")
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface OnBackPressedListener {
        fun onBackPressed3(data: String,position2:String)
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
        bookmark.note?.content?.let { backPressedListener?.onBackPressed3(it,bookmark.position.toString()) }

    }
}
