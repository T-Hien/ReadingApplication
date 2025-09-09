package vn.example.readingapplication.activity.admin.book

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
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
import vn.example.readingapplication.adapter.admin.book.add.AddAuthorsAdapter
import vn.example.readingapplication.adapter.admin.book.add.AddChapterAdapter
import vn.example.readingapplication.adapter.admin.book.update.UpdateAuthorsAdapter
import vn.example.readingapplication.adapter.admin.book.update.UpdateCategoriesAdapter
import vn.example.readingapplication.adapter.admin.book.update.UpdateChapterAdapter
import vn.example.readingapplication.databinding.ActivityAdminBookUpdateBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.BookCategory
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.model.DetailAuthor
import vn.example.readingapplication.model.Notification
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.io.File
import kotlin.random.Random

class UpdateBookActivity : AppCompatActivity(),
    UpdateCategoriesAdapter.OnItemUpdateListener,
    AddAuthorsAdapter.OnItemUpdateListener2,
    AddChapterAdapter.OnItemUpdateListener3
{
    private lateinit var binding: ActivityAdminBookUpdateBinding
    private lateinit var categoryAdapter: UpdateCategoriesAdapter
    private lateinit var authorAdapter: UpdateAuthorsAdapter
    private lateinit var chapterAdapter: UpdateChapterAdapter
    private var selectedCategory: Category? = null
    private var selectedAuthor: Author? = null
    private val categoryList = mutableListOf<Category>()
    private val authorList = mutableListOf<Author>()
    private val chapterList = mutableListOf<Chapter>()
    private var categoryItemList = mutableListOf<String>()
    private var authorItemList = mutableListOf<String>()
    private var chapterItemList = mutableListOf<Chapter>()
    private var bookId:Int=0
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri:Uri?=null
    private var linkImage:String = ""
    private var linkFile:String = ""
    private var linkSha:String = ""
    private val PICK_FILE_REQUEST = 2
    private var checkUpload:Boolean = false
    private var Progress:String =""
    private var stylebook:String =""
    private var reading:String =""
    private var type_file:String =""
    private var checkImg:Boolean = false
    private var listCate:MutableList<String> = mutableListOf()
    private var listAuth:MutableList<String> = mutableListOf()
    private var listChap:MutableList<String> = mutableListOf()


    private var messageNoti:String = ""
    fun ByteArray.toBase64(): String {
        return Base64.encodeToString(this, Base64.DEFAULT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBookUpdateBinding.inflate(layoutInflater)
        setStatusBarColor(ContextCompat.getColor(this, R.color.black))
        supportActionBar?.hide()
        setContentView(binding.root)
        bookId = intent.getIntExtra("bookid",-1)
        reading = intent.getStringExtra("status").toString()
        type_file = intent.getStringExtra("type_file").toString()
        getSpinnerReading()
        getSpinnerBook()
        getInfor(bookId)


        getListCategories()
        getListAuthor()

        setAdapter()
        linner()

    }
    private fun linner(){
        binding.btnUpBook.setOnClickListener(){
            if(checkImg){
                selectedImageUri?.let {
                    uploadImageToGitHub(it)
                }
            }
            else{
                updateBook()
            }
        }
        binding.btnUpCategory.setOnClickListener(){
            deleteBookCategory()
            Toast.makeText(
                this@UpdateBookActivity,
                "Cập nhật thành công",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.btnUpAuthor.setOnClickListener(){
            deleteDetailAuthor()
            Toast.makeText(
                this@UpdateBookActivity,
                "Cập nhật thành công",
                Toast.LENGTH_LONG
            ).show()

        }
        var check = false
        binding.btnAddCategory.setOnClickListener {
            selectedCategory?.let { category ->
                for(item in listCate){
                    if(category.name.equals(item)){
                        check = true
                        break
                    }
                    else{
                        check = false
                    }
                }
                if(!check){
                    category.name?.let { it1 -> listCate.add(it1) }
                    categoryList.add(category)
                    categoryAdapter.notifyDataSetChanged()
                }
                else{
                    Toast.makeText(this@UpdateBookActivity,"Đã chọn thể loại này!",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnAddAuthor.setOnClickListener {
            check = false
            selectedAuthor?.let { author ->
                for(item in listAuth){
                    if(author.name.equals(item)){
                        check = true
                        break
                    }
                    else{
                        check = false
                    }
                }
                if(!check){
                    author.name?.let { it1 -> listAuth.add(it1) }
                    authorList.add(author)
                    authorAdapter.notifyDataSetChanged()
                }
                else{
                    Toast.makeText(this@UpdateBookActivity,"Đã chọn tác giả này!",Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnUpChapter.setOnClickListener(){
            deleteChapter()
            Toast.makeText(
                this@UpdateBookActivity,
                "Cập nhật thành công",
                Toast.LENGTH_LONG
            ).show()

        }
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnCoverImage.setOnClickListener {
            checkImg = true
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
            binding.imgBook.visibility = View.VISIBLE
        }
    }
    private fun setAdapter(){
        categoryAdapter = UpdateCategoriesAdapter(categoryList, this)

        val categoryLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rcvCategory.layoutManager = categoryLayoutManager
        binding.rcvCategory.adapter = categoryAdapter

        authorAdapter = UpdateAuthorsAdapter(authorList, this)

        val authorLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        binding.rcvAuthor.layoutManager = authorLayoutManager
        binding.rcvAuthor.adapter = authorAdapter

        chapterAdapter = UpdateChapterAdapter(chapterList, this)
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
    }
    //*******************   GET  *********
    private fun getInfor(bookId:Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReadBook(bookId).enqueue(object : Callback<ResultResponse<Book>> {
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    val book = response.body()?.dataList?.firstOrNull()
                    book?.let {
                        Log.d("INFO_READBOOK", "$book")
                        binding.edtBookName.setText(book.title)
                        binding.edtDiscriptionBook.setText(book.description)
                        linkImage = book.cover_image.toString()
                        Glide.with(this@UpdateBookActivity)
                            .load(book.cover_image)
                            .placeholder(R.drawable.img_account_girl)
                            .error(R.drawable.img_account_boy)
                            .into(binding.imgBook)
                        book.listBookCategory?.let { categories ->
                            for (category in categories) {
                                category.category?.name?.let { it1 -> listCate.add(it1) }
                                category.category?.let { it1 -> categoryList.add(it1) }
                            }
                        }
                        book.listDetailAuthor?.let { authors ->
                        for (author in authors) {
                            author.author?.name?.let { it1 -> listAuth.add(it1) }
                            author.author?.let { it1 -> authorList.add(it1) }

                            }
                        }
                        book.listChapter?.let { chapters ->
                            for (chapter in chapters) {
                                chapter.title?.let { it1 -> listChap.add(it1) }
                                chapterList.add(chapter)

                            }
                        }
                        categoryAdapter.notifyDataSetChanged()
                        authorAdapter.notifyDataSetChanged()
                        chapterAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
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
                Log.d("ERRO","${t.message}")
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
                Log.d("ERRO","${t.message}")
            }
        })
    }

    //******************* UPLOAD  *********
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
                        Toast.makeText(this@UpdateBookActivity, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                        updateBook()
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@UpdateBookActivity, "Failed to upload image: ${response.code()} $errorBody", Toast.LENGTH_LONG).show()
                        Log.e("UploadImageError", "Response code: ${response.code()}, Error body: $errorBody")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@UpdateBookActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadImageFailure", "Error: ${t.message}", t)
                }
            })
    }

    private fun uploadFileChapterToGitHub(fileUri: Uri, fileType: String) {
        val randomSixDigits = generateRandomSixDigits()
        val inputStream = contentResolver.openInputStream(fileUri)
        val fileExtension = when (fileType) {
            "pdf" -> ".pdf"
            "epub" -> ".epub"
            "mobi" -> ".mobi"
            else -> throw IllegalArgumentException("Unsupported file type")
        }
        val fileName = "file$randomSixDigits$fileExtension"
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
                        Toast.makeText(this@UpdateBookActivity, "$fileType file uploaded successfully", Toast.LENGTH_SHORT).show()
                        addNewChapter()                    } else {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(this@UpdateBookActivity, "Failed to upload $fileType file: ${response.code()} $errorBody", Toast.LENGTH_LONG).show()
                        Log.e("UploadFileError", "Response code: ${response.code()}, Error body: $errorBody")
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@UpdateBookActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.e("UploadFileFailure", "Error: ${t.message}", t)
                }
            })
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

    //****************** UPDATE ***************
    @SuppressLint("SuspiciousIndentation")
    private fun updateBook() {
        val name = binding.edtBookName.text.toString()
        val descrip = binding.edtDiscriptionBook.text.toString()
            if(linkImage.isNotEmpty()){
                val book = Book(
                    id = bookId,
                    title = name,
                    createdAt = null,
                    description = descrip,
                    cover_image = linkImage,
                    type_file = Progress,
                    status = stylebook,
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
                                Toast.makeText(
                                    this@UpdateBookActivity,
                                    "Cập nhật thành công!",
                                    Toast.LENGTH_LONG
                                ).show()
                                messageNoti = "Sách ${resultResponse.message} mới đăng tải"
                            } else {
                                Toast.makeText(
                                    this@UpdateBookActivity,
                                    "Đăng tải sách không thành công!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@UpdateBookActivity,
                                "Đăng tải sách không thành công!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                        Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ERRORSign: ", "${t.message}")
                    }
                })
            }
    }
    private fun updateCategory() {
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
                            }
                    } else {
                        Toast.makeText(
                            this@UpdateBookActivity,
                            "Thêm book category không thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResultResponse<BookCategory>>, t: Throwable) {
                    Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ERRORSign: ", "${t.message}")
                }
            })
        }
    }

    private fun updateAuthor() {
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
//                                this@UpdateBookActivity,
//                                "Thêm detail author thành công",
//                                Toast.LENGTH_LONG
//                            ).show()
                        }
                    } else {
//                        Toast.makeText(
//                            this@UpdateBookActivity,
//                            "Thêm không detail author thất bại!",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResultResponse<DetailAuthor>>, t: Throwable) {
                    Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("ERRORSign: ", "${t.message}")
                }
            })
        }
    }
    private fun updateChapter() {
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
                                createNotification()
                            }
                        } else {
                            Toast.makeText(
                                this@UpdateBookActivity,
                                "Thêm chapter thất bại!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResultResponse<Chapter>>, t: Throwable) {
                        Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                        Log.d("ERRORSign: ", "${t.message}")
                    }
                })
            }
        }
    }
    private fun createNotification() {
        messageNoti = "Sách  ${binding.edtBookName.text} vừa đã cập nhật danh sách chương!"
        val notification = Notification(
            id = null,
            type = "Update",
            abook = Book(id=bookId),
            createdAt = null,
            message = messageNoti

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
                }
            }
            override fun onFailure(call: Call<ResultResponse<Notification>>, t: Throwable) {
                Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    //************* DELETE ********
    private fun deleteBookCategory() {
        val id: Int? = bookId
        if (id == null) {
            Toast.makeText(this@UpdateBookActivity, "ID is null", Toast.LENGTH_LONG).show()
            return
        }
        // Tạo body cho yêu cầu DELETE
        val requestBody = mapOf("id" to id)
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteAllBookCategory(requestBody).enqueue(object : Callback<ResultResponse<Void>> {
            override fun onResponse(
                call: Call<ResultResponse<Void>>,
                response: Response<ResultResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
//                        Toast.makeText(
//                            this@UpdateBookActivity,
//                            "Xóa book category thành công",
//                            Toast.LENGTH_LONG
//                        ).show()
                        updateCategory()
                    } else {
                        Toast.makeText(
                            this@UpdateBookActivity,
                            "Xóa book category không thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UpdateBookActivity,
                        "Xóa book category không thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<Void>>, t: Throwable) {
                Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun deleteDetailAuthor() {
        val id: Int? = bookId
        if (id == null) {
            Toast.makeText(this@UpdateBookActivity, "ID is null", Toast.LENGTH_LONG).show()
            return
        }
        // Tạo body cho yêu cầu DELETE
        val requestBody = mapOf("id" to id)
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteAllDetailAuthor(requestBody).enqueue(object : Callback<ResultResponse<Void>> {
            override fun onResponse(
                call: Call<ResultResponse<Void>>,
                response: Response<ResultResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
//                        Toast.makeText(
//                            this@UpdateBookActivity,
//                            "Xóa thành công",
//                            Toast.LENGTH_LONG
//                        ).show()
                        updateAuthor()
                    } else {
//                        Toast.makeText(
//                            this@UpdateBookActivity,
//                            "Xóa không thành công!",
//                            Toast.LENGTH_LONG
//                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UpdateBookActivity,
                        "Xóa book category không thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<Void>>, t: Throwable) {
                Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }

    private fun deleteChapter() {
        val id: Int? = bookId
        if (id == null) {
            Toast.makeText(this@UpdateBookActivity, "ID is null", Toast.LENGTH_LONG).show()
            return
        }
        val requestBody = mapOf("id" to id)
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteAllChapterByBook(requestBody).enqueue(object : Callback<ResultResponse<Void>> {
            override fun onResponse(
                call: Call<ResultResponse<Void>>,
                response: Response<ResultResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
//                        Toast.makeText(
//                            this@UpdateBookActivity,
//                            "Xóa thành công",
//                            Toast.LENGTH_LONG
//                        ).show()
                        updateChapter()
                    } else {
                        Toast.makeText(
                            this@UpdateBookActivity,
                            "Xóa không thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@UpdateBookActivity,
                        "Xóa book category không thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("ERRODELETE:","${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Void>>, t: Throwable) {
                Toast.makeText(this@UpdateBookActivity, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    // ********** SPINNER *********
    private fun getSpinnerBook() {
        val items = listOf("PDF", "ePub")

        // Tạo adapter cho Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Đặt adapter cho Spinner
        binding.snStyleBook.adapter = adapter

        // Đặt giá trị của Spinner dựa trên biến reading
        binding.snStyleBook.post {
            val position = when (type_file) {
                "PDF" -> 0
                "ePub" -> 1
                "MOBI" -> 2
                else -> -1 // Hoặc bạn có thể xử lý trường hợp giá trị không hợp lệ ở đây
            }
            // Đảm bảo rằng position hợp lệ
            if (position >= 0) {
                binding.snStyleBook.setSelection(position)
            }
        }

        // Thiết lập sự kiện chọn mục cho Spinner
        binding.snStyleBook.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Progress = selectedItem // Hoặc xử lý theo nhu cầu của bạn
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Xử lý nếu cần
            }
        }
    }
    private fun getSpinnerReading() {
        val items = listOf("Đang ra", "Hoàn thành")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.snReading.adapter = adapter

        binding.snReading.post {
            val position = when (reading) {
                "Đang ra" -> 0
                "Hoàn thành" -> 1
                else -> -1
            }

            if (position >= 0) {
                binding.snReading.setSelection(position)
            }
//            } else {
//                Toast.makeText(this, "Status không hợp lệ", Toast.LENGTH_LONG).show()
//            }
        }

        binding.snReading.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                stylebook = selectedItem
//                Toast.makeText(this@UpdateBookActivity, "$stylebook", Toast.LENGTH_LONG).show()
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


    override fun onItemUpdated3(data: List<Chapter>) {
        chapterItemList.clear()
        for (item in data) {
            chapterItemList.add(item)
            Log.d("CHAPTER: ", "$item")

        }
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

    override fun onItemUpdated2(data: List<Int?>) {
        val dataString = data.joinToString(",")
        val dataList = dataString.split(",")

        authorItemList.clear()

        for (item in dataList) {
            authorItemList.add(item.trim())


            Log.d("UpdatedList", "Updated categoryItemList: $authorItemList")
        }    }
}
