package vn.example.readingapplication.fragment.readbook

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.adapter.readbook.CommentAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentBookCommentBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CommentFragment : BaseFragment() {

    private lateinit var binding: FragmentBookCommentBinding
    private lateinit var adapter:CommentAdapter
    private val itemList = mutableListOf<Note>()
    private val type = "1"
    private var bookId: Int = 0
    private var username = ""
    private val TAG ="CommentFragment"
    private var activity:String = "-1"


    companion object {
        fun newInstance(bookId: Int,activitty:String): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", bookId)
            args.putString("KEY_DATA2", activitty)
            fragment.arguments = args
            return fragment
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt("KEY_DATA", 2)
            activity = it.getString("KEY_DATA2", "-1")

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookCommentBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", null).toString()
        if(activity.equals("readbook")){
            binding.lnRoot.visibility = View.GONE
            binding.btnExitComment.visibility = View.GONE
            binding.txtTitle.visibility = View.GONE
        }
//        binding.lnCmment.visibility = View.GONE
        binding.btnSend.setOnClickListener{
            createNote()
        }
        adapter = CommentAdapter(itemList)
        binding.rcvBookComment.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvBookComment.adapter = adapter
        getComment()


        getInforUser()
        return binding.root
    }

    private fun getComment() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getCommentByType(bookId, type).enqueue(object : Callback<ResultResponse<Note>> {
            override fun onResponse(call: Call<ResultResponse<Note>>, response: Response<ResultResponse<Note>>) {
                if (response.isSuccessful) {
                    val comment = response.body()?.dataList
                    comment?.let { notes ->
                        itemList.clear()
                        itemList.addAll(notes)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }
    private fun createNote() {
        val user = User(
            username = username
        )
        val book = Book(
            id = bookId
        )
        var chap: Int? = null
        val note = Note(
            id = null,
            content = binding.txtMyComment.text.toString(),
            chapternumber = chap,
            abook = book,
            auser = user,
            type = "1",
            createdAt = null,
            bookmark = null,
            listReplies = listOf()
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createNote(note).enqueue(object : Callback<ResultResponse<Note>> {
            override fun onResponse(
                call: Call<ResultResponse<Note>>,
                response: Response<ResultResponse<Note>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null) {
                        if (resultResponse.status == 200) {
                            Toast.makeText(context, getString(R.string.text_write_note_v2_successfully), Toast.LENGTH_LONG).show()
                            binding.txtMyComment.setText("")
                            getComment()
                        } else {
                            Toast.makeText(context, resultResponse.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }

    private fun getInforUser() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val user = response.body()?.data
                    user?.let {

                        binding.txtName.text = if (!user.name.isNullOrEmpty()) {
                            user.name
                        } else {
                            user.username

                        }
                        context?.let {
                            Glide.with(it)
                                .load(user.image)
                                .placeholder(R.drawable.bg_library_admin)
                                .error(R.drawable.img_book_conan)
                                .into(binding.imgUserFace)
                        }
                    }

                } else {
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }

}
