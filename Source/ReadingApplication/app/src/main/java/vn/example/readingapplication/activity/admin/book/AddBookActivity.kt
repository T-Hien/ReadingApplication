package vn.example.readingapplication.activity.admin.book

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.BookManagementActivity
import vn.example.readingapplication.adapter.admin.book.add.AddAuthorsAdapter
import vn.example.readingapplication.adapter.admin.book.add.AddCategoriesAdapter
import vn.example.readingapplication.adapter.admin.book.add.AddChapterAdapter
import vn.example.readingapplication.databinding.ActivityAdminBookAddBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.DetailAuthor
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.Notification
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.io.File
import kotlin.random.Random

class AddBookActivity : AppCompatActivity(),
    AddCategoriesAdapter.OnItemUpdateListener,
    AddAuthorsAdapter.OnItemUpdateListener2,
    AddChapterAdapter.OnItemUpdateListener3,
    AddAuthorsAdapter.OnItemUpdateListener4

{
    private lateinit var binding: ActivityAdminBookAddBinding
    private lateinit var categoryAdapter: AddCategoriesAdapter
    private lateinit var authorAdapter: AddAuthorsAdapter
    private lateinit var chapterAdapter: AddChapterAdapter
    private var selectedCategory: Category? = null
    private var selectedAuthor: Author? = null
    private val categoryList = mutableListOf<Category>()
    private val authorList = mutableListOf<Author>()
    private val chapterList = mutableListOf<Chapter>()
    private var categoryItemList = mutableListOf<String>()
    private var authorItemList = mutableListOf<String>()
    private var chapterItemList = mutableListOf<Chapter>()
    private var bookId:Int?=0
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri:Uri?=null
    private var linkImage:String = ""
    private var linkFile:String = ""
    private var linkSha:String = ""
    private val PICK_FILE_REQUEST = 2
    private var checkUpload:Boolean = false
    private var Progress:String =""
    private var stylebook:String =""
    private var listCate:MutableList<String> = mutableListOf()
    private var listAuth:MutableList<String> = mutableListOf()

    private var messageNoti:String = ""
    fun ByteArray.toBase64(): String {
        return Base64.encodeToString(this, Base64.DEFAULT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBookAddBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)

        getSpinnerBook()
        getListCategories()
        getListAuthor()
        getSpinnerReading()

        categoryAdapter = AddCategoriesAdapter(categoryList, this)

        val categoryLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rcvCategory.layoutManager = categoryLayoutManager
        binding.rcvCategory.adapter = categoryAdapter

        var check = false
        binding.btnAddCategory.setOnClickListener {
            check = false
            binding.rcvCategory.visibility = View.VISIBLE
            selectedCategory?.let { category ->
                for (item in listCate) {
                    if (category.name.equals(item)) {
                        check = true
                        break
                    } else {
                        check = false
                    }
                }
                if (!check) {
                    category.name?.let { it1 -> listCate.add(it1) }
                    categoryList.add(category)
                    categoryAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AddBookActivity, "Đã chọn thể loại này!", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        authorAdapter = AddAuthorsAdapter(authorList, this,this)

        val authorLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rcvAuthor.layoutManager = authorLayoutManager
        binding.rcvAuthor.adapter = authorAdapter
        binding.btnAddAuthor.setOnClickListener {
            check = false
            binding.rcvAuthor.visibility = View.VISIBLE
            selectedAuthor?.let { author ->
                for (item in listAuth) {
                    if (author.name.equals(item)) {
                        check = true
                        break
                    }
                }
                if (!check) {
                    check = false
                    author.name?.let { it1 -> listAuth.add(it1) }
                    authorList.add(author)
                    authorAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@AddBookActivity, "Đã chọn tác giả này!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

    }


        chapterAdapter = AddChapterAdapter(chapterList, this)
        val chapterLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rcvUpload.layoutManager = chapterLayoutManager
        binding.rcvUpload.adapter = chapterAdapter
        binding.btnUploadFile.setOnClickListener {
            binding.rcvUpload.visibility = View.VISIBLE
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent,PICK_FILE_REQUEST)
        }

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, BookManagementActivity::class.java)
            startActivity(intent)
        }

        binding.btnAddBook.setOnClickListener {

            if(authorItemList.size<1){
                Toast.makeText(this@AddBookActivity,"Vui lòng chọn ít nhất 1 tác giả",Toast.LENGTH_SHORT).show()
            }
            else if(categoryItemList.size<1){
                Toast.makeText(this@AddBookActivity,"Vui lòng chọn ít nhất 1 thể loại",Toast.LENGTH_SHORT).show()
            }
            else if(chapterItemList.size<1){
                Toast.makeText(this@AddBookActivity,"Vui lòng upload ít nhất 1 thể loại",Toast.LENGTH_SHORT).show()
            }
            else{
                selectedImageUri?.let {
                    uploadImageToGitHub(it)
                } ?: Toast.makeText(this, "Vui lòng chọn ảnh cho sách", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnCoverImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            binding.imgBook.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                binding.imgBook.setImageURI(it) // Hiển thị ảnh
//                uploadImageToGitHub(it) // Tải ảnh lên GitHub
            }
        }
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val selectedFileUri = data.data
            selectedFileUri?.let { uri ->
                val fileType = getFileType(uri)
                uploadFileChapterToGitHub(uri, fileType)
            }
        }
    }
    private fun addNewChapter() {
        val chapterName = binding.txtChapName.text.toString()
        val newChapter = Chapter(
            id = null,
            abook = Book(id = bookId),
            title = chapterName,
            chapternumber = null,
            createdAt = linkSha,
            file_path = linkFile,
            listReadingProgress = null
        )
        checkUpload = false
        chapterList.add(newChapter)
        chapterAdapter.notifyDataSetChanged()
    }
    fun generateRandomSixDigits(): String {
        val min = 100000
        val max = 999999
        val randomNumber = Random.nextInt(min, max + 1)
        return randomNumber.toString()
    }
    private fun uploadImageToGitHub(imageUri: Uri) {
        val randomSixDigits = generateRandomSixDigits()
        val inputStream = contentResolver.openInputStream(imageUri)
        val file = File(cacheDir, "image$randomSixDigits.jpg")
        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        // Chuyển đổi tệp thành base64
        val base64Content = file.readBytes().let { Base64.encodeToString(it, Base64.NO_WRAP) }
        val jsonPayload = """
        {
            "message": "upload image",
            "content": "$base64Content"
        }
    """.trimIndent()
        val body = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val path = "Image/${file.name}"
        val apiService = RetrofitClient.getClient2().create(ApiService::class.java)
        val token = "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"

        apiService.uploadImage("T-Hien", "ReadingApplication", path, body, "token $token")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        val jsonObject = JSONObject(responseBody)
                        val contentObject = jsonObject.getJSONObject("content")
                        val downloadUrl = contentObject.getString("download_url")
                        linkImage = downloadUrl.toString()
//                        Toast.makeText(this@AddBookActivity, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                        createBook()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@AddBookActivity, "Failed to upload image: ${response.code()} $errorBody", Toast.LENGTH_LONG).show()
                        Log.e("UploadImageError", "Response code: ${response.code()}, Error body: $errorBody")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@AddBookActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadImageFailure", "Error: ${t.message}", t)
                }
            })
    }

    private fun getFileType(uri: Uri): String {
        val contentResolver = contentResolver
        val mimeType = contentResolver.getType(uri)
        return when (mimeType) {
            "application/pdf" -> "pdf"
            "application/epub+zip" -> "epub"
            "application/x-mobipocket-ebook" -> "mobi"
            else -> "unknown"
        }
    }
    private fun uploadFileChapterToGitHub(fileUri: Uri, fileType: String) {
        val inputStream = contentResolver.openInputStream(fileUri)
        val randomSixDigits = generateRandomSixDigits()

        // Lấy tên file gốc từ URI
        val fileNameWithoutExtension = getFileNameWithoutExtension(fileUri)

        val fileExtension = when (fileType) {
            "pdf" -> ".pdf"
            "epub" -> ".epub"
            "mobi" -> ".mobi"
            else -> throw IllegalArgumentException("Unsupported file type")
        }
        val fileName = "${fileNameWithoutExtension}_$randomSixDigits$fileExtension"
        val file = File(cacheDir, fileName)

        file.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        // Convert file to base64
        val base64Content = file.readBytes().let { Base64.encodeToString(it, Base64.NO_WRAP) }
        val jsonPayload = """
        {
            "message": "upload $fileType file",
            "content": "$base64Content"
        }
    """.trimIndent()

        val body = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val path = "Files/${file.name}"
        val apiService = RetrofitClient.getClient2().create(ApiService::class.java)
        val token = "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"

        apiService.uploadImage("T-Hien", "ReadingApplication", path, body, "token $token")
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()?.string()
                        val jsonObject = JSONObject(responseBody)
                        val contentObject = jsonObject.getJSONObject("content")
                        val downloadUrl = contentObject.getString("download_url")
                        val sha = contentObject.getString("sha")  // Lấy SHA của file
                        checkUpload = true
                        linkFile = downloadUrl.toString()
                        linkSha = sha.toString()
                        Toast.makeText(this@AddBookActivity, "$fileType file uploaded successfully", Toast.LENGTH_SHORT).show()
                        addNewChapter()                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@AddBookActivity, "Failed to upload $fileType file: ${response.code()} $errorBody", Toast.LENGTH_LONG).show()
                        Log.e("UploadFileError", "Response code: ${response.code()}, Error body: $errorBody")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@AddBookActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadFileFailure", "Error: ${t.message}", t)
                }
            })
    }

    // Hàm trích xuất tên file không có phần mở rộng
    private fun getFileNameWithoutExtension(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        var fileName = ""
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                fileName = it.getString(nameIndex)
            }
        }
        // Loại bỏ phần mở rộng
        return fileName.substringBeforeLast(".")
    }

    private fun getListCategories() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getListCategory().enqueue(object : Callback<ResultResponse<Category>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        updateSpinnerCategory(categories)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getListAuthor() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllAuthor().enqueue(object : Callback<ResultResponse<Author>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Author>>,
                response: Response<ResultResponse<Author>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { categories ->
                        updateSpinnerAuthor(categories)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Author>>, t: Throwable) {
                Log.d("AddBook","${t.message}")
            }
        })
    }
    @SuppressLint("SuspiciousIndentation")
    private fun createBook() {
        val name = binding.edtBookName.text.toString()
        val descrip = binding.edtDiscriptionBook.text.toString()
            if(linkImage.isNotEmpty()){
                val book = Book(
                    id = null,
                    title = name,
                    createdAt = null, // Note=String type is used for date
                    description = descrip,
                    cover_image = linkImage.toString(),
                    type_file = stylebook,
                    status = Progress,
                    listReadingProgress = emptyList(),
                    favorite = null,
                    listNotification = emptyList(),
                    listDetailAuthor = emptyList(),
                    listBookCategory = emptyList(),
                    listChapter = emptyList(),
                    listNote = emptyList(),
                    listBookmark = emptyList(),
                    listLike = emptyList()
                )
                val apiService = RetrofitClient.getClient().create(ApiService::class.java)
                apiService.createBook(book).enqueue(object : Callback<ResultResponse<Book>> {
                    override fun onResponse(
                        call: Call<ResultResponse<Book>>,
                        response: Response<ResultResponse<Book>>
                    ) {
                        if (response.isSuccessful) {
                            val resultResponse = response.body()
                            if (resultResponse != null && resultResponse.status == 200) {
                                bookId = resultResponse.dataNum
                                Toast.makeText(
                                    this@AddBookActivity,
                                    "Đăng tải sách thành công!",
                                    Toast.LENGTH_LONG
                                ).show()
                                createFavorite()
                                createNotification()
                                createChapter()
                                messageNoti = "Sách ${resultResponse.message} mới đăng tải"
                            } else {
                                Toast.makeText(
                                    this@AddBookActivity,
                                    "Đăng tải sách không thành công!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@AddBookActivity,
                                "Đăng tải sách không thành công!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                        Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ERRORSign: ", "${t.message}")
                    }
                })
            }
    }
    private fun createFavorite() {
        val favorite = Favorite(
            id = null,
            abook = Book(id=bookId),
            number = 0
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
//                        Toast.makeText(
//                            this@AddBookActivity,
//                            "Thêm yêu thích thành công!",
//                            Toast.LENGTH_LONG
//                        ).show()
                        createCategory()
                        createAuthor()
                    } else {
                        Toast.makeText(
                            this@AddBookActivity,
                            "Thêm yêu thích thất bại!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            override fun onFailure(call: Call<ResultResponse<Favorite>>, t: Throwable) {
                Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun createNotification() {
        messageNoti = "Sách ${binding.edtBookName.text.toString()} vừa đăng tải!"
        val notification = Notification(
            id = null,
            type = "Add",
            abook = Book(id=bookId),
            createdAt = null,
            message = messageNoti.toString()

        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createNotification(notification).enqueue(object : Callback<ResultResponse<Notification>> {
            override fun onResponse(
                call: Call<ResultResponse<Notification>>,
                response: Response<ResultResponse<Notification>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                    }
                }
                else {
                    Toast.makeText(
                        this@AddBookActivity,
                        "Thêm thông báo thất !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Notification>>, t: Throwable) {
                Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun createCategory() {
        for (item in categoryItemList){
            val category = BookCategory(
                id = null,
                abook = Book(id = bookId),
                category = Category(id = item.toInt())
            )
            val apiService = RetrofitClient.getClient().create(ApiService::class.java)
            apiService.createBookCategory(category).enqueue(object : Callback<ResultResponse<BookCategory>> {
                override fun onResponse(
                    call: Call<ResultResponse<BookCategory>>,
                    response: Response<ResultResponse<BookCategory>>
                ) {
                    if (response.isSuccessful) {
                        val resultResponse = response.body()
                        if (resultResponse != null && resultResponse.status == 200) {
//                                Toast.makeText(
//                                    this@AddBookActivity,
//                                    "Thêm book category thành công",
//                                    Toast.LENGTH_LONG
//                                ).show()
                            }
                    } else {
                        Toast.makeText(
                            this@AddBookActivity,
                            "Thêm book category không thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResultResponse<BookCategory>>, t: Throwable) {
                    Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ERRORSign: ", "${t.message}")
                }
            })
        }
    }
    private fun createAuthor() {
        for (item in authorItemList){
            val author = DetailAuthor(
                id = null,
                abook = bookId,
                author = Author(id = item.toInt())
            )
            val apiService = RetrofitClient.getClient().create(ApiService::class.java)
            apiService.createDetailAuthor(author).enqueue(object : Callback<ResultResponse<DetailAuthor>> {
                override fun onResponse(
                    call: Call<ResultResponse<DetailAuthor>>,
                    response: Response<ResultResponse<DetailAuthor>>
                ) {
                    if (response.isSuccessful) {
                        val resultResponse = response.body()
                        if (resultResponse != null && resultResponse.status == 200) {
//                            Toast.makeText(
//                                this@AddBookActivity,
//                                "Thêm detail author thành công",
//                                Toast.LENGTH_LONG
//                            ).show()
                        }
//                    } else {
//                        Toast.makeText(
//                            this@AddBookActivity,
//                            "Thêm không detail author thất bại!",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResultResponse<DetailAuthor>>, t: Throwable) {
                    Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ERRORSign: ", "${t.message}")
                }
            })
        }
    }
    private fun createChapter() {
        if(chapterItemList.size>=1){
            var i:Int = 1
            for (item in chapterItemList){

                val chapter = Chapter(
                    id= null,
                    abook= Book(id = bookId),
                    title= item.title.toString(),
                    chapternumber= i,
                    createdAt= null,
                    file_path= item.file_path.toString(),
                    listReadingProgress= null
                )
                i ++
                val apiService = RetrofitClient.getClient().create(ApiService::class.java)
                apiService.createChapter(chapter).enqueue(object : Callback<ResultResponse<Chapter>> {
                    override fun onResponse(
                        call: Call<ResultResponse<Chapter>>,
                        response: Response<ResultResponse<Chapter>>
                    ) {
                        if (response.isSuccessful) {
                            val resultResponse = response.body()
                            if (resultResponse != null && resultResponse.status == 200) {
//                                Toast.makeText(
//                                    this@AddBookActivity,
//                                    "Thêm chapter thành công",
//                                    Toast.LENGTH_LONG
//                                ).show()
                                val intent = Intent(this@AddBookActivity,BookManagementActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(
                                this@AddBookActivity,
                                "Thêm chapter thất bại!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResultResponse<Chapter>>, t: Throwable) {
                        Toast.makeText(this@AddBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ERRORSign: ", "${t.message}")
                    }
                })
            }
        }
    }
    private fun getSpinnerBook() {
        val items = listOf("PDF", "ePub")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.snStyleBook.adapter = adapter
        binding.snStyleBook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                stylebook = selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }
    private fun getSpinnerReading() {
        val items = listOf("Đang ra", "Hoàn thành")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.snReading.adapter = adapter
        binding.snReading.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Progress = selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }


    private fun updateSpinnerAuthor(author: List<Author>) {
        val items = author.map { it.name } // Assuming 'name' is a property of Category
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.snAuthor.adapter = adapter
        binding.snAuthor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedAuthor = author[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedAuthor = null
            }
        }
    }
    private fun updateSpinnerCategory(categories: List<Category>) {
        val items = categories.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.snCategory.adapter = adapter
        binding.snCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectedCategory = categories[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCategory = null
            }
        }
    }


    private fun setStatusBarColor(color: Int) {
        window?.statusBarColor = color
    }

    override fun onItemUpdated(data: List<Int?>) {
        val dataString = data.joinToString(",")
        val dataList = dataString.split(",")

        categoryItemList.clear()

        for (item in dataList) {
            categoryItemList.add(item.trim())
        }

        Log.d("UpdatedList", "Updated categoryItemList: $categoryItemList")
    }

    //DetailAuthor
    override fun onItemUpdated2(data: List<Int?>) {
        val dataString = data.joinToString(",")
        val dataList = dataString.split(",")

        authorItemList.clear()


        for (item in dataList) {
            authorItemList.add(item.trim())

            Log.d("UpdatedList", "Updated categoryItemList: $authorItemList")
        }
    }

    override fun onItemUpdated3(data: List<Chapter>) {
        chapterItemList.clear()
        for (item in data) {
            chapterItemList.add(item)
            Log.d("CHAPTER: ", "$item")

        }
    }

    override fun onItemUpdated4(data: List<String?>) {
        val dataString = data.joinToString(",")
        val dataList = dataString.split(",")
        listAuth.clear()

        if(dataList!=null){
            for (item in dataList) {
                listAuth.add(item)
                Log.d("UpdatedList", "Updated categoryItemList: $authorItemList")
            }
        }

    }
}
