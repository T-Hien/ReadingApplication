package vn.example.readingapplication.activity.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.android.myekyc.utils.extension.setSafeOnClickListener
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityReadingBinding
import vn.example.readingapplication.fragment.reading.ListFragment
import vn.example.readingapplication.fragment.reading.ReadingCommentFragment
import vn.example.readingapplication.fragment.reading.ReadingModeFragment
import vn.example.readingapplication.fragment.reading.list.BookmarkFragment
import vn.example.readingapplication.fragment.reading.list.NoteFragment
import vn.example.readingapplication.fragment.reading.list.PositionFragment
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class ReadingActivity : BaseActivity(),
    ReadingModeFragment.OnDataPassListener,
    PositionFragment.OnBackPressedListener,
    BookmarkFragment.OnBackPressedListener,
    NoteFragment.OnBackPressedListener,
    ListFragment.OnDataPassListener{
    private lateinit var binding: ActivityReadingBinding

    private var file: String = ""
    private var chapterNumTam: Int = 0
    private var chapterId:Int = 0
    private var bookId: Int = 0
    private var checkBefore: Boolean = false
    private var checkAfter: Boolean = false
    private var chapNumber: Int = 0
    private var chapter: Chapter? = null
    private var usernameLogin: String = 0.toString()
    private var currentPage: Int = 0
    private var currentPageTam: Int = 0
    private var percent: Float = 0F
    private var readingProgress: ReadingProgress? = null
    private var checkAdd: Boolean = false
    private var type: Int = -1
    private var chapterUp = -1
    private var tyle_book = ""
    private var noteId: Int? = 0
    private var indexAgain = ""
    private var checkList: Boolean = false
    private val TAG = "READING_ACTIVITY"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()

        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()
        bookId = intent.getIntExtra("BOOK_ID", -1)
        chapterNumTam = intent.getIntExtra("CHAP_NUMBER", -1)
        chapterId = intent.getIntExtra("CHAP_ID", -1)
        type = intent.getIntExtra("TYPE", -1)
        tyle_book = intent.getStringExtra("TYLE_BOOK").toString()

        if(type == 0 && !usernameLogin.equals("0")){
            getReading()
            lifecycleScope.launch {
                delay(300)
                showProgressDialog("$currentPageTam")
            }
        }
        if (chapterNumTam != -1) {
            getChapter(chapterNumTam, bookId)
        } else {
            Toast.makeText(this, "Invalid Chapter ID", Toast.LENGTH_SHORT).show()
        }

        binding.layoutreading.lnReading.adaptViewForInserts()
        getFunction()
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()
        getReading()
    }


    private fun getFunction() {
        binding.layoutreading.btnSearch.visibility = View.GONE
        binding.layoutreading.btnBack.setSafeOnClickListener {
            if (checkAdd) {
                deleteReading()
            }
            finish()
        }

        binding.btnBeforeChap.setOnClickListener {
            if (chapterNumTam > 1) {
                chapterNumTam -= 1
                currentPage = 0
                getChapter(chapterNumTam, bookId)
            } else {
                Toast.makeText(this, getString(R.string.text_no_chapter_before), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAfterChap.setOnClickListener {
            chapterNumTam += 1
            currentPage = 0
            getChapter(chapterNumTam, bookId)
        }

        binding.btnInZoom.setOnClickListener {
            val currentZoom = binding.pdfView.zoom

            binding.pdfView.zoomTo(currentZoom + 0.25f)
        }

        binding.btnOutZoom.setOnClickListener {
            val currentZoom = binding.pdfView.zoom
            if (currentZoom > 1.0f) {
                binding.pdfView.zoomTo(currentZoom - 0.25f)
            }
        }
        binding.layoutreading.btnNote.setOnClickListener() {
            if (usernameLogin.equals("0")) {
                Toast.makeText(
                    this@ReadingActivity,
                    "Vui lòng đăng nhập để bình luận!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@ReadingActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                lifecycleScope.launch {
                    binding.layoutreading.btnNote.setImageResource(R.drawable.baseline_edit_note_yellow_24)
                    delay(300)
                    binding.layoutreading.btnNote.setImageResource(R.drawable.baseline_edit_note_24)
                    showNoteDialog()
                }
            }
        }
        binding.layoutreading.btnBookmark.setOnClickListener() {
            if (usernameLogin.equals("0")) {
                Toast.makeText(
                    this@ReadingActivity,
                    "Vui lòng đăng nhập để đánh dấu!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@ReadingActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                lifecycleScope.launch {
                    binding.layoutreading.btnBookmark.setImageResource(R.drawable.baseline_bookmark_yellow_24)
                    delay(300)
                    binding.layoutreading.btnBookmark.setImageResource(R.drawable.baseline_bookmark_24)

                    createBookmark("1")
                }
            }
        }

        binding.layoutreading.btnSetting.visibility = View.GONE

        binding.layoutreading.btnComment.setOnClickListener() {
            lifecycleScope.launch {
                binding.layoutreading.btnComment.setImageResource(R.drawable.baseline_comment_yellow_24)
                delay(300)
                binding.layoutreading.btnComment.setImageResource(R.drawable.baseline_message_24)
                val fragment = ReadingCommentFragment.newInstance(bookId, chapNumber,"reading")
                replaceFragment(fragment)
            }
        }
        binding.layoutreading.btnList.setOnClickListener {
            chapter?.let { chap ->
                var chapterNumber = chap.chapternumber
                val title = chap.title
                if (chapterNumber != null && title != null) {
                    readingProgress = ReadingProgress(
                        status = "1",
                        auser = User(username = usernameLogin),
                        abook = Book(id = bookId),
                        achapter = Chapter(id = chapterId),
                        progressPath = "$currentPage",
                        createdAt = checkAdd.toString(),
                        progressPercentage = percent
                    )
                    val fragment = readingProgress?.let { it1 ->
                        ListFragment.newInstance(
                            bookId, indexAgain, chapNumber, title,
                            it1, tyle_book
                        )
                    }
                    if (fragment != null) {
                        replaceFragment(fragment)
                    }
                }
            }
        }
    }

    private var checkDialog: Boolean = false
    private fun showNoteDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout_note, null)
        dialogBuilder.setView(dialogView)
        val edtContent = dialogView.findViewById<EditText>(R.id.edtContent)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)
        val alertDialog = dialogBuilder.create()
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        okButton.setOnClickListener {
            checkDialog = true
            if (edtContent.text.toString().equals("")) {
                Toast.makeText(this, getString(R.string.text_write_infor_note), Toast.LENGTH_SHORT)
                    .show()
            } else {
                createNote(edtContent.text.toString())
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    private fun showTextDialog(content: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_layout_note_v2, null)
        dialogBuilder.setView(dialogView)
        val txtContent = dialogView.findViewById<TextView>(R.id.txtContent)
        val cancelButton = dialogView.findViewById<Button>(R.id.cancelButton)
        val alertDialog = dialogBuilder.create()
        txtContent.text = content

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    @SuppressLint("MissingInflatedId")
    private fun showProgressDialog(data:String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dailog_notification_infor, null)
        dialogBuilder.setView(dialogView)
        val cancelButton = dialogView.findViewById<Button>(R.id.btnCancel3)
        val okButton = dialogView.findViewById<Button>(R.id.btnOk)
        val alertDialog = dialogBuilder.create()
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        okButton.setOnClickListener {
            currentPage = data.toInt()
            alertDialog.dismiss()
            downloadPdfFromGithub(file)
        }

        alertDialog.show()
    }


    //*********** CREATE *******************
    private fun createReading() {
        var status: String? = ""
        if (percent > 25f) {
            status = "Reading"
        } else {
            status = "Unread"
        }
        val readinh = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterUp),
            progressPath = currentPage.toString(),
            progressPercentage = percent
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createReading(readinh).enqueue(object :
            Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Toast.makeText(
                    this@ReadingActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "ERR_CREATE: ${t.message}")
            }
        })
    }

    private fun deleteReading() {
        var status: String? = ""
        if (percent > 25f) {
            status = "Reading"
        } else {
            status = "Unread"
        }
        val reading = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterNumTam),
            progressPath = currentPage.toString(),
            progressPercentage = percent
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
                        createReading()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Toast.makeText(
                    this@ReadingActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "ERR_DELE:${t.message}")
            }
        })
    }

    private fun createNote(content: String) {
        val user = User(
            username = usernameLogin
        )
        val book = Book(
            id = bookId
        )
        var chap: Int? = null
        if (chapNumber != 0) {
            chap = chapNumber
        }
        val note = Note(
            id = null,
            content = content,
            chapternumber = chap,
            abook = book,
            auser = user,
            type = "2",
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
                                this@ReadingActivity,
                                getString(R.string.text_write_note_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            noteId = resultResponse.dataNum!!
                            createBookmark("2")
                        } else {
                            Log.d(TAG, "CREATE 1=> ${resultResponse.message}")
                            Toast.makeText(
                                this@ReadingActivity,
                                resultResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.d(TAG, " 2=> ${response.body()}")
                        Toast.makeText(
                            this@ReadingActivity,
                            "Null response body",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Toast.makeText(
                    this@ReadingActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "3=>${t.message}")
            }
        })
    }

    private fun createBookmark(type:String) {
        val bookmark = Bookmarks(
            id = null,
            auser = User(username = usernameLogin),
            createdAt = null,
            abook = Book(id = bookId),
            note = Note(id = noteId) ?: null,
            type = type,
            chapternumber = chapNumber,
            progress_percentage = percent.toString(),
            position = currentPage
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createBookmark(bookmark).enqueue(object : Callback<ResultResponse<Bookmarks>> {
            override fun onResponse(
                call: Call<ResultResponse<Bookmarks>>,
                response: Response<ResultResponse<Bookmarks>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        if (type.equals("1")) {
                            Toast.makeText(
                                this@ReadingActivity,
                                getString(R.string.text_write_bookmark_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Bookmarks>>, t: Throwable) {
                Toast.makeText(
                    this@ReadingActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "${t.message}")
            }
        })
    }

    private fun getChapter(chapterNum: Int, bookId: Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getChapterByIdAndBookId(chapterNum, bookId)
            .enqueue(object : Callback<ResultResponse<Chapter>> {
                override fun onResponse(
                    call: Call<ResultResponse<Chapter>>,
                    response: Response<ResultResponse<Chapter>>
                ) {
                    if (response.isSuccessful) {
                        chapter = response.body()?.dataList?.firstOrNull()
                        chapter?.let {
                            file = it.file_path ?: ""
                            chapNumber = it.chapternumber ?: 0
                            chapterUp = it.id ?: -1
                            chapterId = it.id!!
                            binding.txtChapter.text = "Chương ${it.chapternumber} ${it.title ?: ""}"
                            binding.txtTime.text = it.createdAt ?: ""
                            downloadPdfFromGithub(file)
                        }
                    } else {
                        Toast.makeText(
                            this@ReadingActivity,
                            getString(R.string.text_no_chapter),
                            Toast.LENGTH_SHORT
                        ).show()
                        if (checkAfter) this@ReadingActivity.chapterNumTam -= 1
                        if (checkBefore) this@ReadingActivity.chapterNumTam += 1
                    }
                    checkAfter = false
                    checkBefore = false
                }

                override fun onFailure(call: Call<ResultResponse<Chapter>>, t: Throwable) {
                    Toast.makeText(
                        this@ReadingActivity,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun getReading() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReading2(usernameLogin, bookId)
            .enqueue(object : Callback<ResultResponse<ReadingProgress>> {
                override fun onResponse(
                    call: Call<ResultResponse<ReadingProgress>>,
                    response: Response<ResultResponse<ReadingProgress>>
                ) {
                    if (response.isSuccessful && response.body()?.data != null) {
                        val readings = response.body()?.data

                        if(type==0&& !usernameLogin.equals("0"))
                        {
                            currentPageTam = readings?.progressPath?.toInt()!!
                        }
                        if (type == 1) {
                            currentPage = readings?.progressPath?.toInt()!!
                            checkAdd = true
                        }

                        else if(readings?.progressPath !=null){
                            checkAdd = true

                        }
                    } else {
                        checkAdd = false
                    }

                }

                override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                    checkAdd = false
                    Toast.makeText(
                        this@ReadingActivity,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "${t.message}")
                }
            })
    }

    //*********************
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fmView, fragment)
        binding.fmView.visibility = View.VISIBLE
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                binding.fmView.visibility = View.GONE
            }
        }
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }

    private fun downloadPdfFromGithub(pdfUrl: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(pdfUrl)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@ReadingActivity,
                        "Failed to download PDF",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let { body ->
                        val inputStream: InputStream = body.byteStream()
                        val pdfFile = File(
                            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                            "downloaded.pdf"
                        )
                        val outputStream = FileOutputStream(pdfFile)
                        val buffer = ByteArray(1024)
                        var read: Int

                        while (inputStream.read(buffer).also { read = it } != -1) {
                            outputStream.write(buffer, 0, read)
                        }
                        outputStream.close()
                        inputStream.close()

                        runOnUiThread {
                            displayPdf(pdfFile)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@ReadingActivity,
                            getString(R.string.text_failed_to_download),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }

    private fun displayPdf(pdfFile: File) {
        binding.pdfView.fromFile(pdfFile)
            .defaultPage(currentPage)
            .onPageChange { page, pageCount ->
                currentPage = page
                val percentage = ((page + 1).toFloat() / pageCount) * 100
                percent = percentage
            }
            .onLoad {
                binding.pdfView.zoomTo(1.0f)
            }
            .load()
    }



    override fun onBackPressed(data: String) {
        if (data != null) {
            Log.d(TAG, "$data")
            currentPage = data.toInt()
        }
        supportFragmentManager.popBackStack()
        downloadPdfFromGithub(file)
    }

    override fun onDataPass(data: Int) {
    }
    override fun onBackPressed2(data: String) {
        if (data != null) {
            Log.d(TAG, "$data")
            currentPage = data.toInt()
        }
        supportFragmentManager.popBackStack()
        downloadPdfFromGithub(file)
    }

    override fun onBackPressed3(data: String, position2: String) {
        if (data != null) {
            currentPage = position2.toInt()
        }
        supportFragmentManager.popBackStack()
        downloadPdfFromGithub(file)
        showTextDialog(data)
    }

    override fun onDataPassListFragment(data: String,chapId:Int,check:Boolean) {
        Log.d(TAG,"DATA: $data + $currentPage + $chapId")
        checkAdd = if(check) true else false
        if (data != "-1") {
            val progress = data
            supportFragmentManager.popBackStack()
            if(chapterId!=chapId){
                checkList = true
                chapterId = chapId
                showProgressDialog(progress)
            }
            if (data.toIntOrNull() != currentPage){
                showProgressDialog(progress)
            }
        }

    }


}
