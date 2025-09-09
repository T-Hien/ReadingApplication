package vn.example.readingapplication.fragment.reading

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentReadingListBinding
import vn.example.readingapplication.fragment.readbook.ChapterFragment
import vn.example.readingapplication.fragment.reading.ReadingModeFragment.OnDataPassListener
import vn.example.readingapplication.fragment.reading.list.BookmarkFragment
import vn.example.readingapplication.fragment.reading.list.NoteFragment
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ListFragment : BaseFragment() {

    private lateinit var binding: FragmentReadingListBinding
    private var dataPassListener: OnDataPassListener? = null

    private var bookId: Int = 0
    private var chapternum: Int = 0
    private var tile: String? = null
    private var reading: ReadingProgress? = null
    private var tyleBook = ""
    private var index = ""
    private var usernameLogin = "0"
    private var checkAdd: Boolean = false
    private var chapterId: Int = 0
    private var status: String = ""
    private val TAG = "LIST_FRAGMENT"
    private var progressPath:String = "-1"
    companion object {
        private const val BOOKID = "param1"
        private const val CHAPTERNUMBER = "param2"
        private const val TITLE = "param3"
        private const val INDEX = "index"

        fun newInstance(
            param1: Int, index: String, param2: Int, param3: String, reading: ReadingProgress, type: String
        ): ListFragment {
            val fragment = ListFragment()
            val args = Bundle()
            args.putInt(BOOKID, param1)
            args.putString(INDEX, index)
            args.putInt(CHAPTERNUMBER, param2)
            args.putString(TITLE, param3)
            args.putSerializable("reading", reading)
            args.putString("tyleBook", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            bookId = it.getInt(BOOKID)
            chapternum = it.getInt(CHAPTERNUMBER)
            tile = it.getString(TITLE)
            reading = it.getSerializable("reading") as ReadingProgress?
            tyleBook = it.getString("tyleBook").toString()
            index = it.getString(INDEX).toString()
        }
        progressPath = reading?.progressPath!!
        val sharedPreferences = requireContext().getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()

        setupFragments()
        initializeUI()
        updateButtonState()
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = requireContext().getSharedPreferences("SaveAccount", Context.MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()
        updateButtonState()
    }
    private fun initializeUI() {
        binding.btnExit.setOnClickListener {
            parentFragmentManager.popBackStack()
            if (!usernameLogin.equals("0")) {
                Log.d(TAG,"CHECK: $checkAdd")
                dataPassListener?.onDataPassListFragment(progressPath,chapterId,checkAdd)
            }
            else{
                checkAdd = false
            }

        }
        binding.cnRoot.setOnClickListener(){
            parentFragmentManager.popBackStack()
            if (!usernameLogin.equals("0")) {
                dataPassListener?.onDataPassListFragment(progressPath,chapterId,checkAdd)
            }else{
                checkAdd = false
            }

        }
        binding.btnAdd.setOnClickListener {
            if (usernameLogin.equals("0")) {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng đăng nhập!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            } else {
                if (checkAdd) {
                    deleteReading()
                } else {
                    createReading()
                }
            }
        }
    }

    private fun updateButtonState() {
        Infor()
        getReading {
            if (checkAdd) {
                binding.btnAdd.text = "Đã lưu vào thư viện"
                context?.let { ContextCompat.getColor(it, R.color.background_delete) }
                    ?.let { binding.btnAdd.setBackgroundColor(it) }

            } else {
                binding.btnAdd.text = "Thêm vào thư viện"
                context?.let { ContextCompat.getColor(it, R.color.background_edit) }
                    ?.let { binding.btnAdd.setBackgroundColor(it) }
            }
        }
    }
    private fun setupFragments() {
        val userName = reading?.auser?.username.toString()
        val chapNum: Int = chapternum
        binding.rlRoot.adaptViewForInserts()

        val listFragment = listOf(
            ChapterFragment.newInstance(bookId, tyleBook,3),
            BookmarkFragment.newInstance(bookId, userName, chapNum, tyleBook, "1"),
            NoteFragment.newInstance(bookId, userName, chapNum, tyleBook, "2")
        )

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, listFragment)
        binding.vpListToolbar.adapter = adapter

        val tabTitles = arrayOf(
            getString(R.string.title_list_content),
            getString(R.string.title_list_bookmark),
            getString(R.string.title_list_note)
        )

        TabLayoutMediator(binding.tlReadBook, binding.vpListToolbar) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        binding.txtTitle.text = "${getString(R.string.text_chapter)} $chapternum $tile"
    }
    fun Infor(){
        if(chapterId==0){
            chapterId = reading?.achapter?.id!!
        }
        val progressPercentageFloat = reading?.progressPercentage
        bookId = reading?.abook?.id!!
        if (progressPercentageFloat != null && progressPercentageFloat < 25) {
            status = "Unread"
        } else {
            status = "Reading"
        }
    }
    private fun createReading() {
        val readingProgress = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterId),
            progressPath = reading?.progressPath,
            progressPercentage = reading?.progressPercentage
        )

        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createReading(readingProgress).enqueue(object : Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful && response.body()?.status == 200) {
                    Log.d(TAG,"CHECK_DATA: ${response.body()?.status}")
                    binding.btnAdd.text = "Đã lưu vào thư viện"
                    context?.let { ContextCompat.getColor(it, R.color.background_delete) }
                        ?.let { binding.btnAdd.setBackgroundColor(it) }
                    checkAdd = true
                } else {
                    Log.d(TAG,"CHECK_DATA: ${response.body()?.status}")
                    Toast.makeText(context, "Không thành công!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                Toast.makeText(context, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteReading() {
        val reading = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterId),
            progressPath = "",
            progressPercentage = 0F
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteReading(reading).enqueue(object :
            Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        checkAdd = false
                        binding.btnAdd.text = "Thêm sách vào thư viện"
                        context?.let { ContextCompat.getColor(it, R.color.background_edit) }
                            ?.let { binding.btnAdd.setBackgroundColor(it) }
                        progressPath = "-1"
                        dataPassListener?.onDataPassListFragment(progressPath,chapterId,checkAdd)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }

    private fun getReading(callback: () -> Unit) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReading2(usernameLogin, bookId).enqueue(object : Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful && response.body()?.data != null) {
                    val readings = response.body()?.data
                    chapterId = readings?.achapter?.id ?: 0
                    checkAdd = true
                    progressPath = readings?.progressPath!!
                    Infor()
                } else {
                    checkAdd = false
                }
                callback()
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Log.e(TAG, "Error: ${t.message}")
                checkAdd = false
                callback()
            }
        })
    }
    interface OnDataPassListener {
        fun onDataPassListFragment(data: String,chapterId:Int,check:Boolean)
    }
    private fun sendData(data: String,chapterId:Int,check:Boolean) {
        dataPassListener?.onDataPassListFragment(data,chapterId,check)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as? OnDataPassListener
    }



}
