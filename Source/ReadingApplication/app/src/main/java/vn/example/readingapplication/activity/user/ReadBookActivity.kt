package vn.example.readingapplication.activity.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.MainActivity
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityReadBookBinding
import vn.example.readingapplication.fragment.readbook.ChapterFragment
import vn.example.readingapplication.fragment.readbook.IntroduceFragment
import vn.example.readingapplication.fragment.readbook.ListLikeFragment
import vn.example.readingapplication.fragment.reading.ReadingCommentFragment
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ReadBookActivity : BaseActivity(),ReadingCommentFragment.OnLoginRequestListener {
    private lateinit var binding: ActivityReadBookBinding
    private var bookId: Int = 0
    private var description: String = ""
    private var chapNumber = 1
    private var chapId = 0
    private var tyle_book: String = ""
    private var favoriteId = 0
    private var favorite_num = 0
    private var username = "0"
    private var checkLike: Boolean = false
    private var checkFavorite: Boolean = false
    private var checkLogin: Boolean = false
    var shouldMoveToCommentTab:Boolean = false
    private val TAG = "READBOOK2"


    companion object {
        private const val LOGIN_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadBookBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.background_night))
        supportActionBar?.hide()
        setContentView(binding.root)
        val sharedPreferences = this.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        lifecycleScope.launch {
            delay(300)
            if (username.equals("0")) {
                binding.cvReading.visibility = View.GONE
            }
            bookId = intent.getIntExtra("BOOK_ID", -1)
            chapNumber = intent.getIntExtra("CHAP_NUMBER", 1)
        }
        Log.d(TAG,"onCreate + $shouldMoveToCommentTab")

    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = this.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        Log.d(TAG,"onResume + $shouldMoveToCommentTab")
        lifecycleScope.launch {
            delay(300)
            getLike()
            getInfor()
            setupUI()
        }
    }

    private fun setupUI() {
        binding.btnExitBook.setOnClickListener {
            if (checkLogin) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }
        binding.btnFavoriteBook.setOnClickListener {
            supportFragmentManager.popBackStack()
            if (username == "0") {
                checkLogin = true
                val currentTab = binding.vpListToolbar.currentItem
                val prefs = getSharedPreferences("ReadBookPrefs", MODE_PRIVATE)
                prefs.edit().putInt("currentTabPosition", currentTab).apply()

                // Chuyển sang màn hình đăng nhập
                val loginIntent = Intent(this, LoginActivity::class.java)
                startActivityForResult(loginIntent, LOGIN_REQUEST_CODE)
            } else {
                handleFavoriteAction()
            }
        }
        binding.btnReading.setOnClickListener {
            if (tyle_book == "PDF") {
                val intent = Intent(this, ReadingActivity::class.java).apply {
                    putExtra("BOOK_ID", bookId)
                    putExtra("CHAP_NUMBER", chapNumber)
                    putExtra("CHAP_ID", chapId)
                    putExtra("TYPE", 1)
                    putExtra("TYLE_BOOK", "PDF")
                }
                startActivity(intent)
            } else {
                val intent = Intent(this, EpubActivity::class.java).apply {
                    putExtra("BOOK_ID", bookId)
                    putExtra("CHAP_NUMBER", chapNumber)
                    putExtra("CHAP_ID", chapId)
                    putExtra("TYPE", 1)
                    putExtra("TYLE_BOOK", "ePub")
                }
                startActivity(intent)
            }
        }
        binding.btnDetailLike.setOnClickListener {
            lifecycleScope.launch {
                binding.btnDetailLike.setImageResource(R.drawable.baseline_more_vert_yellow_24)
                delay(300)
                binding.btnDetailLike.setImageResource(R.drawable.baseline_more_vert_24)
                val fragment = ListLikeFragment.newInstance(bookId)
                replaceFragment(fragment)
            }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fmView2, fragment)
        fragmentTransaction.addToBackStack(null)
        binding.fmView2.visibility = View.VISIBLE

        fragmentTransaction.commit()
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.fmView2.visibility = View.GONE
            }
        }
    }
    private fun handleFavoriteAction() {
        Log.d(TAG,"handleFavoriteAction + $shouldMoveToCommentTab")
        val isFavorite = binding.btnFavoriteBook.tag == "filled"
        if (isFavorite) {
            binding.btnFavoriteBook.setImageResource(R.drawable.ic_heart_regular)
            binding.btnFavoriteBook.tag = "regular"
            deleteLike()
        } else {
            binding.btnFavoriteBook.setImageResource(R.drawable.ic_heart_solid_red)
            binding.btnFavoriteBook.tag = "filled"
            createLike()
        }
        updateFavorite(isFavorite)
        getInfor()
    }

    private fun getLike() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.findLike(bookId, username).enqueue(object : Callback<ResultResponse<Like>> {
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        checkLike = true
                        if (checkLike) {
                            binding.btnFavoriteBook.setImageResource(R.drawable.ic_heart_solid_red)
                            binding.btnFavoriteBook.tag = "filled"
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    private fun getInfor() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReadBook(bookId).enqueue(object : Callback<ResultResponse<Book>> {
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    val book = response.body()?.dataList?.firstOrNull()
                    book?.let {
                        if (username != "0") {
                            getReading()
                        }

                        favoriteId = book.favorite?.id!!
                        favorite_num = book.favorite.number
                        tyle_book = it.type_file.toString()
                        if (tyle_book.equals("ePub")){
                            binding.txtStyleBook.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@ReadBookActivity,
                                    R.color.text_organe
                                )
                            )
                        }
                        binding.txtStyleBook.text = it.type_file
                        binding.txtTitlleBook.text = it.title
                        binding.txtAuthorBook.text = it.listDetailAuthor?.joinToString { author -> author.author?.name ?: "" }
                        Glide.with(this@ReadBookActivity)
                            .load(it.cover_image)
                            .into(binding.imgBookPicture)
                        description = it.description ?: ""
                        binding.txtLikes.text = it.favorite?.number.toString()
                        binding.txtGenresBook.text =  getString(R.string.text_category_name)+"${it.listBookCategory?.joinToString { bookCategory -> bookCategory.category?.name.toString() }}"
                        setupFragments()
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun getReading() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReading2(username, bookId).enqueue(object : Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful && response.body()?.status==200) {
                    val reading = response.body()?.data
                    chapId = reading?.achapter?.id!!
                    binding.cvReading.visibility = View.VISIBLE
                }
                else{
                    binding.cvReading.visibility = View.GONE
                }
            }
            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun updateFavorite(isFavorite: Boolean) {
        if (isFavorite && favorite_num > 0) {
            favorite_num -= 1
        } else if (!isFavorite) {
            favorite_num += 1
        } else {
            favorite_num = 0
        }
        val favorite = Favorite(
            id = favoriteId,
            abook = Book(id = bookId),
            number = favorite_num,
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createFavorite(favorite).enqueue(object : Callback<ResultResponse<Favorite>> {
            override fun onResponse(
                call: Call<ResultResponse<Favorite>>,
                response: Response<ResultResponse<Favorite>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        checkFavorite = true
                        getInfor()
                    } else {
                        checkLike = true
                        Log.d(TAG,"Thêm yêu thích thất bại!")
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Favorite>>, t: Throwable) {
                Log.d("An error occurred:", "${t.message}")
            }
        })
    }

    private fun createLike() {
        val like = Like(
            id = null,
            abook = Book(id = bookId),
            auser = User(username = username)
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createLike(like).enqueue(object : Callback<ResultResponse<Like>> {
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        getInfor()
                    } else {
                        checkLike = true
                        Log.d(TAG,"Thêm yêu thích thất bại!")
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.d("An error occurred:", "${t.message}")
            }
        })
    }

    private fun deleteLike() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteLike(bookId,username).enqueue(object : Callback<ResultResponse<Like>> {
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {

                    } else {
                        Log.d(TAG,"Xóa yêu thích thất bại!")
                    }
                } else {
                    startActivity(Intent(this@ReadBookActivity, LoginActivity::class.java))
                }
            }
            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    private fun setupFragments() {
        val listFragment = listOf(
            IntroduceFragment.newInstance(description),
            ChapterFragment.newInstance(bookId, tyle_book, 0),
            ReadingCommentFragment.newInstance(bookId, -1, "readbook")
        )
        binding.vpListToolbar.offscreenPageLimit = listFragment.size
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle, listFragment)
        binding.vpListToolbar.adapter = adapter

        val tabTitles = arrayOf(
            getString(R.string.title_reading_introduce),
            getString(R.string.title_reading_chapter),
            getString(R.string.title_reading_commemt)
        )
        TabLayoutMediator(binding.tlReadBook, binding.vpListToolbar) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        if (shouldMoveToCommentTab) {
            binding.vpListToolbar.post {
                binding.vpListToolbar.setCurrentItem(2, false)  // Chuyển sang tab 2 (ReadingCommentFragment)
            }
        } else {
            // Kiểm tra giá trị lưu trong SharedPreferences
            val prefs = getSharedPreferences("ReadBookPrefs", MODE_PRIVATE)
            val savedTabPosition = prefs.getInt("currentTabPosition", 0)  // Mặc định là tab 0
            Log.d(TAG, "Saved Tab Position: $savedTabPosition")  // Kiểm tra giá trị đã lưu

            binding.vpListToolbar.setCurrentItem(savedTabPosition, false)
        }

    }


    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
    }

    override fun onLoginRequired() {
        shouldMoveToCommentTab = true
    }
}
