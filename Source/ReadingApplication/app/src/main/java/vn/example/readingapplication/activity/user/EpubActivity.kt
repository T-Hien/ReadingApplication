package vn.example.readingapplication.activity.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.android.myekyc.utils.extension.isVisible
import vn.android.myekyc.utils.extension.setSafeOnClickListener
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.base.BaseActivity
import vn.example.readingapplication.databinding.ActivityEpubV1Binding
import vn.example.readingapplication.fragment.reading.ListFragment
import vn.example.readingapplication.fragment.reading.ReadingCommentFragment
import vn.example.readingapplication.fragment.reading.ReadingModeFragment
import vn.example.readingapplication.fragment.reading.SearchReadingFragment
import vn.example.readingapplication.fragment.reading.list.BookmarkFragment
import vn.example.readingapplication.fragment.reading.list.NoteFragment
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Bookmarks
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.model.Setting
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

class EpubActivity : BaseActivity(),
    ReadingModeFragment.OnDataPassListener,
    BookmarkFragment.OnBackPressedListener,
    NoteFragment.OnBackPressedListener,
    SearchReadingFragment.OnSearchResultClickListener,
    ListFragment.OnDataPassListener{

    private lateinit var binding: ActivityEpubV1Binding
    private lateinit var textTitle: TextView
    private lateinit var textAuthor: TextView
    private lateinit var webViewContent: WebView
    private lateinit var bookResources: Map<String, nl.siegmann.epublib.domain.Resource>
    private val client = OkHttpClient()
    private val TAG = "EPUB_ACTIVITY"
    private var bookId = 0
    private var chapterId: Int = 0
    private var chapNumber: Int = 0
    private var chapter: Chapter? = null
    private var currentPage = 1
    private var isPageLoaded = false
    private var position: Int = 0
    private var checkReading = false
    private var readingpercent: Float = 0F
    private var totalContentHeight: Int = 0
    private var checkBefore: Boolean = false
    private var checkAfter: Boolean = false
    private var textSize: Int = 15
    private var linkChapter: String = ""
    private var readingProgress: ReadingProgress? = null
    private var checkAdd: Int = -1
    private var type = -1
    private var chapterUp = -1
    private var checkColor: Boolean = false
    private var textFont = ""
    private var tyle_book = ""
    private var indexAgain = "0"
    private var noteId:Int? = 0
    private var checkSetting:Boolean = false
    private var usernameLogin: String = 0.toString()
    private var checkList:Boolean = false
    private var checkLoad:Boolean = false


    @SuppressLint("JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEpubV1Binding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        binding.layoutreading.lnReading.adaptViewForInserts()
        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()

        bookId = intent.getIntExtra("BOOK_ID", -1)
        chapNumber = intent.getIntExtra("CHAP_NUMBER", -1)
        chapterId = intent.getIntExtra("CHAP_ID", -1)
        type = intent.getIntExtra("TYPE", 0)
        tyle_book = intent.getStringExtra("TYLE_BOOK").toString()
        if(!usernameLogin.equals("0") &&type == 1){
            checkList = true
            getSetting()
            getReading()
        }
        else{
            getChapter(chapNumber,bookId)
            if(!usernameLogin.equals("0")){
                getSetting()
                if(type !=3 &&checkReading){
                    showProgressDialog(1)
                }
            }
        }
        setupUI()
        webViewContent = findViewById(R.id.webViewContent)
        webViewContent.settings.javaScriptEnabled = true

    }
    private fun setupUI() {
        textTitle = binding.txtTitle
        textAuthor = binding.textAuthor
        webViewContent = binding.webViewContent
        webViewContent.settings.apply {
            javaScriptEnabled = true
            allowContentAccess = true
            allowFileAccess = true
            loadsImagesAutomatically = true
        }
        webViewContent.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                isPageLoaded = true
                webViewContent.evaluateJavascript(
                    "(function() { return document.body.scrollHeight; })();"
                ) { value ->
                    totalContentHeight = value.toIntOrNull() ?: 0
                    Log.d(TAG, "Total content height: $totalContentHeight")
                    calculateReadingProgress()
                }
                val savedScrollY = position

                if (savedScrollY != 0) {
                    webViewContent.scrollTo(0, savedScrollY)
                }

            }
        }
        webViewContent.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (scrollY != 0) {
                position = scrollY
                if (totalContentHeight > 0) {
                    calculateReadingProgress()
                }
            }
        }

        binding.layoutreading.lnReading.adaptViewForInserts()
        setupListeners()
        var checkSearch:Boolean = true
        binding.btnSearchText.setOnClickListener(){
            if(!binding.txtTextSearch.equals("")){
                val query = binding.txtTextSearch.text.toString()
                if (query.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.webViewContent.findAllAsync(query)
                        binding.webViewContent.setFindListener { activeMatchOrdinal, numberOfMatches, isDoneCounting ->
                            if (isDoneCounting && checkSearch) {
                                checkSearch = false
                                if (numberOfMatches > 0) {
                                    Toast.makeText(this@EpubActivity, getString(R.string.text_have)+" $numberOfMatches "+getString(R.string.text_refult_find), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@EpubActivity, getString(R.string.text_no_refult_find), Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
            else{
                binding.txtTextSearch.hint = getString(R.string.text_please_enter_find)
            }
        }
    }

    private fun setupListeners() {
        binding.layoutreading.btnBack.setSafeOnClickListener {
            if (checkReading &&checkAdd==1) {
                Log.d(TAG,"CHECK: DELETE $checkAdd")
                deleteReading()
            } else {
                getReading()
                lifecycleScope.launch {
                    delay(100)

                    if (checkReading) {
                        Log.d(TAG,"CHECK_2: DELETE")
                        deleteReading()
                    } else {
                        finish()
                    }
                }
            }
        }
        binding.layoutreading.btnNote.setOnClickListener(){
            if(usernameLogin.equals("0")){
                Toast.makeText(
                    this,
                    "Vui lòng đăng nhập để bình luận!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                lifecycleScope.launch {
                    binding.layoutreading.btnNote.setImageResource(R.drawable.baseline_edit_note_yellow_24)
                    delay(300)
                    binding.layoutreading.btnNote.setImageResource(R.drawable.baseline_edit_note_24)
                    showSelectionDialog()
                }
            }
        }
        binding.layoutreading.btnSearch.setOnClickListener() {
            if(!binding.lnSearch.isVisible()){
                lifecycleScope.launch {
                    binding.layoutreading.btnSearch.setImageResource(R.drawable.baseline_search_yellow_24)
                    delay(300)
                    binding.layoutreading.btnSearch.setImageResource(R.drawable.baseline_search_24)
                    binding.lnSearch.visibility = View.VISIBLE
                }
            }
            else{
                binding.lnSearch.visibility = View.GONE
            }
        }
        binding.layoutreading.btnBookmark.setOnClickListener() {
            if(usernameLogin.equals("0")){
                Toast.makeText(
                    this,
                    "Vui lòng đăng nhập!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
            else{
                lifecycleScope.launch {
                    binding.layoutreading.btnBookmark.setImageResource(R.drawable.baseline_bookmark_yellow_24)
                    delay(300)
                    binding.layoutreading.btnBookmark.setImageResource(R.drawable.baseline_bookmark_24)
                    createBookmark("1")
                }
            }
        }
        binding.layoutreading.btnComment.setOnClickListener() {
            lifecycleScope.launch {
                binding.layoutreading.btnComment.setImageResource(R.drawable.baseline_message_yellow_24)
                delay(300)
                binding.layoutreading.btnComment.setImageResource(R.drawable.baseline_message_24)
                val fragment = ReadingCommentFragment.newInstance(bookId, chapNumber,"epub")
                replaceFragment(fragment)
            }
        }
        binding.layoutreading.btnSetting.setOnClickListener() {
            lifecycleScope.launch {
                binding.layoutreading.btnSetting.setImageResource(R.drawable.baseline_settings_yellow_24)
                delay(300)
                binding.layoutreading.btnSetting.setImageResource(R.drawable.baseline_settings_24)
                val fragment = ReadingModeFragment()
                replaceFragment(fragment)
            }
        }
        binding.layoutreading.btnList.setOnClickListener() {
            var status = "Unread"
            if (readingpercent > 25) {
                status = "Reading"
            }
            chapter?.let { chap ->
                val chapterNumber = chap.chapternumber!!
                val title = chap.title
                readingProgress = ReadingProgress(
                    status = status,
                    auser = User(username = usernameLogin),
                    abook = Book(id = bookId),
                    achapter = Chapter(id = chapterUp),
                    progressPath = "$position",
                    createdAt = checkAdd.toString(),
                    progressPercentage = readingpercent
                )
                if (chapterNumber != null && title != null) {
                    val fragment = ListFragment.newInstance(
                        bookId,indexAgain, chapNumber, title,
                        readingProgress!!, tyle_book
                    )
                    replaceFragment(fragment)
                }
            }
        }
        binding.btnBeforeChap.setSafeOnClickListener {
            if (chapNumber > 0) {
                checkBefore = true
                getChapter(chapNumber - 1, bookId)
            } else {
                Toast.makeText(this, getString(R.string.text_no_chapter_before), Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAfterChap.setSafeOnClickListener {
            checkAfter = true
            getChapter(chapNumber + 1, bookId)
        }



    }
    override fun onResume() {
        super.onResume()
        val sharedPreferences = getSharedPreferences("SaveAccount", MODE_PRIVATE)
        usernameLogin = sharedPreferences.getString("username", "0").toString()
        if(!usernameLogin.equals("0"))
        {
            getSetting()
        }
    }

    private fun handleChapterError() {
        if (checkAfter) {
            Toast.makeText(this, getString(R.string.text_no_chapter_after), Toast.LENGTH_SHORT).show()
            checkAfter = false
        }
        if (checkBefore) {
            Toast.makeText(this, getString(R.string.text_no_chapter_before), Toast.LENGTH_SHORT).show()
            checkBefore = false
        }
    }

    private fun calculateReadingProgress() {
        if (totalContentHeight > 0) {
            val progress = (position.toFloat() / totalContentHeight.toFloat()) * 100
            readingpercent = progress
        }
    }

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
    private fun getChapter(requestedChapterNum: Int, bookId: Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getChapterByIdAndBookId(requestedChapterNum, bookId)
            .enqueue(object : Callback<ResultResponse<Chapter>> {
                override fun onResponse(
                    call: Call<ResultResponse<Chapter>>,
                    response: Response<ResultResponse<Chapter>>
                ) {
                    if (response.isSuccessful && response.body()?.dataList?.isNotEmpty() == true) {
                        chapter = response.body()?.dataList?.firstOrNull()
                        chapter?.let {
                            linkChapter = it.file_path.toString()
                            chapterUp = it.id ?: -1
                            chapNumber = it.chapternumber ?: 0
                            checkAfter = false
                            checkBefore = false
                            binding.txtTitle.text = "Chương ${it.chapternumber} ${it.title ?: ""}"
                            binding.textAuthor.text = it.createdAt ?: ""

                            if(checkList){
                                checkList = false
                                loadEpub(linkChapter,1)
                            }
                            else{
                                loadEpub(linkChapter,0)
                            }
                        }
                    } else {
                        handleChapterError()
                    }
                }

                override fun onFailure(call: Call<ResultResponse<Chapter>>, t: Throwable) {
                    Toast.makeText(this@EpubActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getReading() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReading2(usernameLogin, bookId).enqueue(object : Callback<ResultResponse<ReadingProgress>> {
            override fun onResponse(
                call: Call<ResultResponse<ReadingProgress>>,
                response: Response<ResultResponse<ReadingProgress>>
            ) {
                if (response.isSuccessful && response.body()?.status == 200) {
                    val reading = response.body()?.data
                    chapterId = reading?.achapter?.id!!
                    position = reading.progressPath?.toInt()?:0
                    checkReading = true
                    linkChapter = reading.achapter?.file_path ?: ""

                    checkAdd = 1
                    indexAgain = reading.progressPath.toString()

                    if (type == 1 || checkList) {
                        checkList = true
                        reading.achapter?.chapternumber?.let { it1 -> getChapter(it1,bookId) }
                    }
                } else {
                    checkReading = false
                    checkAdd = 0
                }
            }
            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }
    private fun getSetting() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getSetting(usernameLogin).enqueue(object : Callback<ResultResponse<Setting>> {
            override fun onResponse(
                call: Call<ResultResponse<Setting>>,
                response: Response<ResultResponse<Setting>>
            ) {
                if (response.isSuccessful) {
                    val setting = response.body()?.data
                    if (setting != null) {
                        textSize = setting.font_size
                        textFont = setting.font.toString()
                        if (setting.readingMode.equals("Ban đêm")) {
                            Night()
                        } else {
                            Day()
                        }
                        if(!checkList){
                            if(linkChapter!=null){
                                if(!checkSetting)
                                {
                                    loadEpub(linkChapter,0)
                                }
                                else{
                                    loadEpub(linkChapter,1)
                                }
                                checkSetting = false
                            }
                        }
                    }
                }
                else{
                    if(!checkList) {
                        if (linkChapter != null) {
                            if (!checkSetting) {
                                loadEpub(linkChapter, 0)
                            } else {
                                loadEpub(linkChapter, 1)
                            }
                            checkSetting = false
                        }
                    }

                }
            }

            override fun onFailure(call: Call<ResultResponse<Setting>>, t: Throwable) {
                Log.d("SettingMode:", "${t.message}")
            }
        })
    }

    private fun deleteReading() {
        var status: String? = ""
        if (readingpercent > 25f) {
            status = "Reading"
        } else {
            status = "Unread"
        }
        val reading = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterId),
            progressPath = currentPage.toString(),
            progressPercentage = readingpercent
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
                        checkReading = false
                        createReading()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                Toast.makeText(
                    this@EpubActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    //*********CREATE*********
    private fun createReading() {
        var status = "Unread"
        if (readingpercent > 25) {
            status = "Reading"
        }
        val reading = ReadingProgress(
            abook = Book(id = bookId),
            auser = User(username = usernameLogin),
            createdAt = null,
            status = status,
            achapter = Chapter(id = chapterUp),
            progressPath = "$position",
            progressPercentage = readingpercent
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createReading(reading)
            .enqueue(object : Callback<ResultResponse<ReadingProgress>> {
                override fun onResponse(
                    call: Call<ResultResponse<ReadingProgress>>,
                    response: Response<ResultResponse<ReadingProgress>>
                ) {
                    if (response.isSuccessful) {
                        val resultResponse = response.body()
                        if (resultResponse != null && resultResponse.status == 200) {
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<ResultResponse<ReadingProgress>>, t: Throwable) {
                    Toast.makeText(
                        this@EpubActivity,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("ERRORSign: ", "${t.message}")
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
                                this@EpubActivity,
                                getString(R.string.text_write_note_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            noteId = resultResponse.dataNum!!
                            createBookmark("2")
                        } else {
                            Log.d("CREATE_NOTE:", " 1=> ${resultResponse.message}")
                            Toast.makeText(
                                this@EpubActivity,
                                resultResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Log.d("CREATE_NOTE:", " 2=> ${response.body()}")
                        Toast.makeText(
                            this@EpubActivity,
                            "Null response body",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Toast.makeText(
                    this@EpubActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("CREATE_NOTE:", "3=>${t.message}")
            }
        })
    }
    private fun createBookmark(type:String) {
        noteId = if(type.equals("1")) null else noteId
        val bookmark = Bookmarks(
            id = null,
            auser = User(username = usernameLogin),
            createdAt = null,
            abook = Book(id = bookId),
            note = Note(id = noteId) ?: null,
            type = type,
            chapternumber = chapNumber,
            progress_percentage = readingpercent.toString(),
            position = position
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
                                this@EpubActivity,
                                getString(R.string.text_write_bookmark_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Bookmarks>>, t: Throwable) {
                Toast.makeText(
                    this@EpubActivity,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    private fun loadEpub(fileUrl: String,po:Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val epubFile = withContext(Dispatchers.IO) { downloadFile(fileUrl) }
            epubFile?.let {
                val book = withContext(Dispatchers.IO) { readEpubFile(it) }
                book?.let {
                    bookResources = it.resources.all.associateBy { resource -> resource.href }
                    displayEpubContent(it)

                    if(po==0){
                        position = 0
                        webViewContent.scrollTo(0, 0)
                    }

                }
            }
        }
    }

    private fun displayEpubContent(book: nl.siegmann.epublib.domain.Book) {
        val backgroundColor = if (checkColor) "000000" else "FFFFFF"
        val textColor = if (checkColor) "FFFFFF" else "000000"
        val textFont = this.textFont

        val fontLinks = mapOf(
            "Roboto" to "https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap",
            "Open Sans" to "https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;700&display=swap"
        )
        val selectedFontLink = fontLinks[textFont] ?: fontLinks["Roboto"]

        saveImagesToCache(book)

        val htmlContent = StringBuilder()
        htmlContent.append("<html><head>")
        htmlContent.append("<link href='$selectedFontLink' rel='stylesheet'>")
        htmlContent.append("<style>")
        htmlContent.append("body { font-size: ${textSize}px; color: #$textColor; background-color: #$backgroundColor; font-family: '$textFont'; }")
        htmlContent.append("img { max-width: 100%; height: auto; }")
        htmlContent.append("</style></head><body>")

        for (resource in book.contents) {
            val content = readResourceContent(resource)
            if (content.isNotEmpty()) {
                htmlContent.append(content).append("<br><br>")
            }
        }

        htmlContent.append("</body></html>")

        val baseUrl = cacheDir.toURI().toString()
        webViewContent.loadDataWithBaseURL(baseUrl, htmlContent.toString(), "text/html", "UTF-8", null)

        webViewContent.post {
            webViewContent.scrollTo(0, position)
        }
    }
    private fun saveImagesToCache(book: nl.siegmann.epublib.domain.Book) {
        for (resource in book.resources.all) {
            if (resource.mediaType == nl.siegmann.epublib.service.MediatypeService.JPG ||
                resource.mediaType == nl.siegmann.epublib.service.MediatypeService.PNG ||
                resource.mediaType == nl.siegmann.epublib.service.MediatypeService.GIF) {

                val fileName = resource.href.substringAfterLast("/")
                val file = File(cacheDir, fileName)

                try {
                    file.outputStream().use { output ->
                        resource.inputStream.use { input ->
                            input.copyTo(output)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error saving image: ${e.message}")
                }
            }
        }
    }

    private fun downloadFile(fileUrl: String): File? {
        Log.d("DownloadFile2", "URL: $fileUrl")
        return try {
            val request = Request.Builder().url(fileUrl).build()
            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()
            val epubFile = File(cacheDir, "downloadedBook.epub")
            inputStream?.let {
                FileOutputStream(epubFile).use { output ->
                    it.copyTo(output)
                }
                epubFile
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading file: ${e.message}", e)
            null
        }
    }

    private fun readEpubFile(file: File): nl.siegmann.epublib.domain.Book? {
        return try {
            val epubReader = EpubReader()
            epubReader.readEpub(file.inputStream())
        } catch (e: Exception) {
            Log.e(TAG, "Error reading EPUB file: ${e.message}", e)
            null
        }
    }

    private fun readResourceContent(resource: nl.siegmann.epublib.domain.Resource): String {
        return try {
            val inputStream = resource.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            val originalContent = reader.use { it.readText() }

            val updatedContent = originalContent.replace(
                "(<img[^>]*src=\")(?!http)([^\">]*)".toRegex(),
                "$1file://${cacheDir.path}/$2"
            )

            updatedContent
        } catch (e: Exception) {
            Log.e(TAG, "Error reading resource: ${e.message}", e)
            "Failed to read resource"
        }
    }

    private fun scrollToPosition() {
        if (isPageLoaded) {
            webViewContent.scrollTo(0, position)
        } else {
            webViewContent.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    webViewContent.scrollTo(0, position)
                }
            }
        }
    }

    private var checkDialog:Boolean = false
    private fun showSelectionDialog() {
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
            if(edtContent.equals("")){
                Toast.makeText(this,getString(R.string.text_write_infor_note),Toast.LENGTH_SHORT).show()
            }
            else{
                createNote(edtContent.text.toString())
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }
    private fun showTextDialog(content:String) {
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
    private fun showProgressDialog(data:Int) {
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
            checkList = true
            getReading()
            lifecycleScope.launch {
                delay(400)
                if (indexAgain != "0") {
                    position = indexAgain.toIntOrNull() ?: 0
                    scrollToPosition()
                }
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }
    override fun onDataPass(data: Int) {
        Log.d(TAG,"onDataPass: $data ")
        if (data != 0 &&data>0) {
            textSize = data
            getSetting()
            supportFragmentManager.popBackStack()
            if (data!= position){
                showProgressDialog(0)
            }
        }
        else if(data<0){
            checkSetting = true
            getSetting()
        }
    }
    override fun onBackPressed2(data: String) {
        position = data.toIntOrNull() ?: position
        supportFragmentManager.popBackStack()
        scrollToPosition()
    }
    override fun onBackPressed3(data: String,position2:String) {
        if (data != null) {
            Log.d(TAG, "onBackPressed3: $data")
            position = position2.toInt()
        }
        supportFragmentManager.popBackStack()
        scrollToPosition()
        showTextDialog(data)
    }

    override fun onSearchResultClick(position: Int) {
    }
    override fun onDataPassListFragment(data: String, chapId: Int,check:Boolean) {
        checkAdd = if(check) 1 else 0
        Log.d(TAG, "onDataPassListFragment: $data + $position + $chapId + $check")
        if (data != "-1") {
            supportFragmentManager.popBackStack()
            if (chapterId != chapId) {
                checkList = true
                chapterId = chapId
                showProgressDialog(0)
            } else if (data.toIntOrNull() != position) {
                showProgressDialog(0)
            }
        }
    }
    private fun Day(){
        binding.btnAfterChap.setBackgroundResource(R.drawable.bg_button_reading_right)
        binding.btnBeforeChap.setBackgroundResource(R.drawable.bg_button_reading_left)
        binding.lnTitle.setBackgroundColor(ContextCompat.getColor(this@EpubActivity,R.color.white))
        binding.txtTitle.setTextColor(ContextCompat.getColor(this@EpubActivity,R.color.black))
        binding.textAuthor.setTextColor(ContextCompat.getColor(this@EpubActivity,R.color.black))
        textFont = "Ban ngày"
        checkColor = false
    }
    private fun Night(){
        binding.btnAfterChap.setBackgroundResource(R.drawable.bg_button_reading_right_v2)
        binding.btnBeforeChap.setBackgroundResource(R.drawable.bg_button_reading_left_v2)
        binding.lnTitle.setBackgroundColor(ContextCompat.getColor(this@EpubActivity,R.color.black))
        binding.txtTitle.setTextColor(ContextCompat.getColor(this@EpubActivity,R.color.white))
        binding.textAuthor.setTextColor(ContextCompat.getColor(this@EpubActivity,R.color.white))
        checkColor = true
    }
}