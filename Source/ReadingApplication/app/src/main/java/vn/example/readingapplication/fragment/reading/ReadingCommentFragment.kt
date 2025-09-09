package vn.example.readingapplication.fragment.reading

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.adapter.readbook.CommentAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentBookCommentBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.Setting
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ReadingCommentFragment :BaseFragment() {

    private lateinit var binding: FragmentBookCommentBinding
    private lateinit var adapter:CommentAdapter
    private val itemList = mutableListOf<Note>()
    private val type = "1"
    private var bookId: Int = 0
    private var username = "0"
    private var activity:String = "-1"
    private val TAG ="ReadingCommentFragment"
    private val LOGIN_REQUEST_CODE = 1001

    companion object {
        private const val ARG_PARAM1 = "bookId"
        private const val ARG_PARAM2 = "chapternum"

        fun newInstance(bookId: Int,chapternum: Int,activitty:String,): ReadingCommentFragment {
            val fragment = ReadingCommentFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, bookId)
            args.putInt(ARG_PARAM2, chapternum)
            args.putString("KEY_DATA2", activitty)
            fragment.arguments = args
            return fragment
        }
    }
    private var chapternum: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt(ARG_PARAM1)
            chapternum = it.getInt(ARG_PARAM2)
            activity = it.getString("KEY_DATA2", "-1")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBookCommentBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username","0").toString()

        adapter = CommentAdapter(itemList)
        binding.rcvBookComment.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvBookComment.adapter = adapter

        if(activity.equals("readbook")){
            binding.lnRoot.visibility = View.GONE
            binding.txtTitle.visibility = View.GONE
            binding.btnDown.visibility = View.GONE
            binding.btnExitComment.visibility = View.GONE
            getComment2()
        }
        else{
            binding.btnExitComment.visibility = View.VISIBLE
            getComment()
        }
        getInforUser()
        binding.btnSend.setOnClickListener{
            if(username.equals("0") || username.equals("null")){
                if(activity.equals("readbook")){
                    loginRequestListener?.onLoginRequired()
                }
                Toast.makeText(context, getString(R.string.text_please_write_note), Toast.LENGTH_SHORT).show()
                val loginIntent = Intent(requireContext(), LoginActivity::class.java)
                startActivityForResult(loginIntent, LOGIN_REQUEST_CODE)
//                Toast.makeText(context,getString(R.string.text_please_write_note),Toast.LENGTH_SHORT).show()
//                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
            else{
                createNote()
            }
        }

        binding.btnDown.setOnClickListener(){
            binding.lnRoot.visibility = View.VISIBLE
            binding.btnDown.visibility = View.GONE
        }
        binding.lnRoot.setOnClickListener(){
            parentFragmentManager.popBackStack()
        }
        binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY > 190) {
                binding.lnRoot.visibility = View.GONE
                binding.btnDown.visibility = View.VISIBLE
            }
        }

        if(bookId!=null&&chapternum!=0){
            bookId = bookId as Int
            binding.btnExitComment.setOnClickListener(){
                parentFragmentManager.popBackStack()
            }
        }

        return binding.root
    }
    interface OnLoginRequestListener {
        fun onLoginRequired()
    }
    private var loginRequestListener: OnLoginRequestListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnLoginRequestListener) {
            loginRequestListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        loginRequestListener = null
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        if(!username.equals("0")|| !username.equals("null")){
            if(!activity.equals("readbook")){
                getSetting()
            }
            getInforUser()
        }

    }

    private fun getComment() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        chapternum?.let {
            apiService.getCommentByChap(bookId, type, it).enqueue(object : Callback<ResultResponse<Note>> {
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
                    Log.d("COMMENT:", "${t.message}")
                }
            })
        }
    }
    private fun getComment2() {
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
    private fun getSetting() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getSetting(username).enqueue(object : Callback<ResultResponse<Setting>> {
            override fun onResponse(
                call: Call<ResultResponse<Setting>>,
                response: Response<ResultResponse<Setting>>
            ) {
                if (response.isSuccessful) {
                    val setting = response.body()?.data
                    if(setting?.readingMode.equals("Ban đêm"))
                    {
                        context?.let { ContextCompat.getColor(it,R.color.background_BN) }
                            ?.let { binding.lnContent.setBackgroundColor(it) }
                        binding.txtTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        binding.btnExitComment.setImageResource(R.drawable.baseline_arrow_back_ios_black_24)
                        binding.btnDown.setImageResource(R.drawable.baseline_expand_more_black_24)
                    }
                    else{
                        context?.let { ContextCompat.getColor(it,R.color.background_night) }
                            ?.let { binding.lnContent.setBackgroundColor(it) }
                        binding.txtTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        binding.btnExitComment.setImageResource(R.drawable.baseline_arrow_back_ios_24)
                        binding.btnDown.setImageResource(R.drawable.baseline_expand_more_24)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Setting>>, t: Throwable) {
                Log.d("SettingMode:", "${t.message}")
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
        if (chapternum != 0 && chapternum !=-1) {
            chap = chapternum
        }
        val note = Note(
            id = null,
            content = binding.txtMyComment.text.toString(),
            chapternumber = chap,
            abook = book,
            auser = user,
            type = "1",
            createdAt = null,
            status = 0,
            description = "",
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
                            Toast.makeText(
                                context,
                                getString(R.string.text_write_note_v2_successfully),
                                Toast.LENGTH_LONG
                            ).show()
                            if (activity == "reabook") {
                                getComment2()
                            } else {
                                getComment()
                            }
                            binding.txtMyComment.text.clear()
                        } else {
                            Log.d("CREATE_NOTE:", " 1=> ${resultResponse.message}")
                            Toast.makeText(
                                context,
                                resultResponse.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Log.d("CREATE_NOTE:", " 2=> ${response.body()}")
                        Toast.makeText(
                            context,
                            "Null response body",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Toast.makeText(
                    context,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("CREATE_NOTE:", "3=>${t.message}")
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
                        context?.let {
                            Glide.with(it)
                                .load(user.image)
                                .placeholder(R.drawable.bg_library_admin)
                                .error(R.drawable.img_book_conan)
                                .into(binding.imgUserFace)
                        }
                        binding.txtName.text = if (!user.name.isNullOrEmpty()) {
                            user.name
                        } else {
                            user.username

                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

}
