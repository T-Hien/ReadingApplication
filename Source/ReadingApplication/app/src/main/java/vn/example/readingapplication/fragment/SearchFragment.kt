package vn.example.readingapplication.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import vn.android.myekyc.utils.extension.adaptViewForInserts
import vn.example.readingapplication.R
import vn.example.readingapplication.SharedViewModel
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.ViewPagerAdapter
import vn.example.readingapplication.adapter.search.ListShowBooksAdapter
import vn.example.readingapplication.adapter.search.SuggestionAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentSearchpageBinding
import vn.example.readingapplication.fragment.search.SearchAuthorFragment
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.fragment.search.SearchCategoriesFragment
import vn.example.readingapplication.model.search.BookSearch
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SearchFragment: BaseFragment(),
    ListShowBooksAdapter.OnItemClickListener{

    private lateinit var binding: FragmentSearchpageBinding
    private lateinit var viewModel: SharedViewModel
    private var keyword:String = ""
    private lateinit var suggestionAdapter: SuggestionAdapter
    private val suggestionList = mutableListOf<String>()
    private lateinit var booksAdapter: ListShowBooksAdapter
    private var isUpdateText:Boolean = false
    private var imageCapture: ImageCapture? = null
    private val bookList = mutableListOf<BookSearch>()
    private val TAG = "SEARCH_FRAGMENT"
    private val REQUEST_CAMERA_PERMISSION = 100
    private var isSearching = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchpageBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        viewModel.data.observe(viewLifecycleOwner) { data ->
            if(binding.edtSearch.text.isNotEmpty()){
                binding.btnRemove.visibility = View.VISIBLE
            }
        }
        binding.btnRemove.setOnClickListener{
            resetToInitialState()
            booksAdapter.submitList(emptyList())

        }
        binding.btnSearch.setOnClickListener(){
            if(binding.edtSearch.text.isNotEmpty()){
                keyword = binding.edtSearch.text.toString()
                viewModel.data.value = keyword
                binding.cvSuggestions.visibility = View.GONE
            }
        }
        binding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val input = s.toString().trim()
                if(isUpdateText){
                    isUpdateText = false
                    return
                }
                else if (input.isNotEmpty()) {
                    binding.btnRemove.visibility = View.VISIBLE
                    val history = getSearchHistory("search")
                    suggestionList.clear()
                    suggestionList.addAll(history.filter { it.contains(input, ignoreCase = true) })

                    suggestionAdapter.notifyDataSetChanged()
                    binding.cvSuggestions.visibility = if (suggestionList.isNotEmpty()) View.VISIBLE else View.GONE
                } else {
                    suggestionList.clear()
                    suggestionAdapter.notifyDataSetChanged()
                    binding.cvSuggestions.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.btnCamera.setOnClickListener {
            resetToInitialState()
            checkCameraPermission()
        }
        binding.btnImage.setOnClickListener(){
            resetToInitialState()
            booksAdapter.submitList(emptyList())
            fetchBooks()
            openGallery()
        }

        binding.btnCapture.setOnClickListener {
            booksAdapter.submitList(emptyList())
            fetchBooks()
            captureImage()
        }

        booksAdapter = ListShowBooksAdapter(this)
        binding.rvListSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListSearch.adapter = booksAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rlRoot.adaptViewForInserts()
        val listFragment = listOf(
            SearchBookFragment(),
            SearchAuthorFragment(),
            SearchCategoriesFragment()
        )

        binding.vpListSearch.offscreenPageLimit = listFragment.size

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, listFragment)
        binding.vpListSearch.adapter = adapter
        val tabTitles = arrayOf(getString(R.string.title_search_book),getString(R.string.title_search_author),getString(R.string.title_search_category))

        TabLayoutMediator(binding.tlListSearch, binding.vpListSearch) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        val rvSuggestions = binding.rvSuggestions
        suggestionAdapter = SuggestionAdapter(suggestionList) { selectedKeyword ->
            isUpdateText = true
            binding.edtSearch.setText(selectedKeyword)
            binding.edtSearch.setSelection(selectedKeyword.length)
            binding.cvSuggestions.visibility = View.GONE
            binding.btnSearch.performClick()
        }
        rvSuggestions.adapter = suggestionAdapter
        rvSuggestions.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getSearchHistory(type: String): List<String> {
        val sharedPreferences = requireContext().getSharedPreferences("SearchHistory", Context.MODE_PRIVATE)
        val history = sharedPreferences.getStringSet(type, mutableSetOf()) ?: mutableSetOf()
        return history.toList().distinct()
    }

    private val PICK_IMAGE_REQUEST = 1
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let {
                val imageFile = File(requireContext().cacheDir, "selected_image.jpg")
                val inputStream = requireContext().contentResolver.openInputStream(it)
                val outputStream = imageFile.outputStream()
                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                processImageWithGoogleVision(imageFile)
            }
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            openCameraPreview()
        }
    }

    private fun openCameraPreview() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = androidx.camera.core.Preview.Builder().build().apply {
                setSurfaceProvider(binding.cameraPreview.surfaceProvider)
            }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                binding.cameraPreview.visibility = View.VISIBLE
                binding.btnCapture.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Máy ảnh đã sẵn sàng!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e(TAG, "Camera binding failed: ${e.message}", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage() {
        val file = File(requireContext().externalCacheDir, "captured_image.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.cameraPreview.visibility = View.GONE
                    processImageWithGoogleVision(file)
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val rotatedBitmap = rotateImageIfRequired(file, bitmap)
                    binding.imgPicture.visibility = View.VISIBLE
                    binding.imgPicture.setImageBitmap(rotatedBitmap)
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                    Toast.makeText(requireContext(), "Chụp ảnh thất bại!", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun rotateImageIfRequired(file: File, bitmap: Bitmap): Bitmap {
        val exif = androidx.exifinterface.media.ExifInterface(file.absolutePath)
        val orientation = exif.getAttributeInt(
            androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
            androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }
    private fun rotateBitmap(bitmap: Bitmap, angle: Float): Bitmap {
        val matrix = android.graphics.Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun fetchBooks() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllBooksAd(0).enqueue(object : retrofit2.Callback<ResultResponse<BookSearch>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: retrofit2.Call<ResultResponse<BookSearch>>,
                response: retrofit2.Response<ResultResponse<BookSearch>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { books ->
                        bookList.clear()
                        bookList.addAll(books)
                        booksAdapter.notifyDataSetChanged()
                        books.forEach { book ->
                            book.cover_image?.let { imageUrl ->
                                book.labels = fetchImageLabelsFromUrl(imageUrl)
                            }
                        }
                    }
                    val imageLinks = response.body()?.dataList?.mapNotNull { it.cover_image } ?: emptyList()

                    if (imageLinks.isNotEmpty()) {
                        handleApiImageLinks(imageLinks)
                    } else {
                        Log.d(TAG, "No image links found in API response")
                    }
                } else {
                    Log.e(TAG, "Failed to fetch image links from API")
                }
            }

            override fun onFailure(call: retrofit2.Call<ResultResponse<BookSearch>>, t: Throwable) {
                Log.d(TAG, "Failed to fetch books: ${t.message}")
            }
        })
    }
    private suspend fun fetchLabelsFromUrls(imageUrls: List<String>): List<String> = suspendCoroutine { continuation ->
        val combinedLabels = mutableSetOf<String>()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (imageUrl in imageUrls) {
                    val labels = fetchImageLabelsFromUrl(imageUrl)
                    combinedLabels.addAll(labels)
                }
                continuation.resume(combinedLabels.toList())
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchLabelsFromUrls: ${e.message}", e)
                continuation.resumeWithException(e)
            }
        }
    }

    //XL ảnh API
    private fun fetchImageLabelsFromUrl(imageUrl: String): List<String> {
        val labels = mutableListOf<String>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val request = Request.Builder()
                    .url(imageUrl)
                    .header(
                        "User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
                    )
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val randomFileName = "temp_image_${System.currentTimeMillis()}.jpg"
                    val imageFile = File(requireContext().cacheDir, randomFileName)
                    response.body?.byteStream()?.use { inputStream ->
                        FileOutputStream(imageFile).use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                    }

                    if (imageFile.length() == 0L) {
                        Log.e(TAG, "Image file is empty")
                        return@launch
                    }

                    val imageBytes = FileInputStream(imageFile).use { it.readBytes() }
                    val base64EncodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
                    val visionLabels = fetchLabelsFromGoogleVision(base64EncodedImage)
                    labels.addAll(visionLabels)
                    withContext(Dispatchers.Main) {
                    }
                } else {
                    Log.e(TAG, "Failed to fetch image with response code: ${response.code}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception in fetchImageLabelsFromUrl: ${e.message}", e)
            }
        }

        return labels
    }
    private fun filterIgnoredLabels(labels: List<String>): List<String> {
        val ignoredLabels = listOf("poster", "book cover", "publication", "advertising")
        return labels.filterNot { it in ignoredLabels }
    }


    private fun fetchLabelsFromGoogleVision(base64Image: String): List<String> {
        val labels = mutableListOf<String>()

        try {
            val requestBody = JSONObject().apply {
                put("requests", JSONArray().put(
                    JSONObject().apply {
                        put("image", JSONObject().put("content", base64Image))
                        put("features", JSONArray().put(JSONObject().apply {
                            put("type", "LABEL_DETECTION")
                            put("maxResults", 10)
                        }))
                    }
                ))
            }
            val apiKey = "AIzaSyC7ikGjqh06QlJcvwNZ7oKUY1m6ycXgXsU"
            val visionApiUrl = "https://vision.googleapis.com/v1/images:annotate?key=$apiKey"
            val client = OkHttpClient()
            val body = RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString())
            val request = Request.Builder().url(visionApiUrl).post(body).build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body?.string() ?: "")
                val labelAnnotations = jsonResponse
                    .getJSONArray("responses")
                    .getJSONObject(0)
                    .optJSONArray("labelAnnotations")

                if (labelAnnotations != null) {
                    for (i in 0 until labelAnnotations.length()) {
                        val label = labelAnnotations.getJSONObject(i).getString("description").lowercase()
                        labels.add(label)
                    }
                } else {
                    Log.d(TAG, "No labels found in Vision API response")
                }
            } else {
                Log.e(TAG, "Vision API call failed with response code: ${response.code}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception in fetchLabelsFromGoogleVision: ${e.message}", e)
        }

        return labels
    }

    private fun handleApiImageLinks(imageLinks: List<String>) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val labels = fetchLabelsFromUrls(imageLinks)
                withContext(Dispatchers.Main) {
                    searchBooksByLabels(labels)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error handling API image links: ${e.message}", e)
            }
        }
    }

    //XL ảnh tải lên
    private fun processImageWithGoogleVision(file: File) {
        try {
            val imageBytes = FileInputStream(file).use { it.readBytes() }
            val base64EncodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP)

            val requestBody = JSONObject().apply {
                put(
                    "requests", JSONArray().put(
                        JSONObject().apply {
                            put("image", JSONObject().put("content", base64EncodedImage))
                            put("features", JSONArray().put(JSONObject().apply {
                                put("type", "LABEL_DETECTION")
                                put("maxResults", 10)
                            }))
                        }
                    )
                )
            }

            val apiKey = "AIzaSyC7ikGjqh06QlJcvwNZ7oKUY1m6ycXgXsU"
            val url = "https://vision.googleapis.com/v1/images:annotate?key=$apiKey"

            val client = OkHttpClient()
            val body = RequestBody.create("application/json".toMediaTypeOrNull(), requestBody.toString())
            val request = Request.Builder().url(url).post(body).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e(TAG, "API request failed: ${e.message}", e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()

                        val jsonResponse = JSONObject(responseData)
                        val labelAnnotations = jsonResponse
                            .getJSONArray("responses")
                            .getJSONObject(0)
                            .optJSONArray("labelAnnotations")

                        val labels = mutableListOf<String>()
                        if (labelAnnotations != null) {
                            for (i in 0 until labelAnnotations.length()) {
                                labels.add(labelAnnotations.getJSONObject(i).getString("description").lowercase())
                            }
                        }
                        isSearching = true
                        //XL ảnh tải lên
                        searchBooksByLabels(labels)
                    } else {
                        Log.e(TAG, "API request failed with code: ${response.code}")
                    }
                }
            })

        } catch (e: Exception) {
            Log.e(TAG, "Failed to process image with Google Vision: ${e.message}", e)
        }
    }

    //Kiểm tra nhãn
    private fun searchBooksByLabels(labels: List<String>) {
        if (bookList.isEmpty()) {
            resetToInitialState()
            Log.d(TAG, "Danh sách sách trống.")
            return
        }

        isSearching = true
        Log.d(TAG, "Tìm kiếm sách với nhãn: $labels")
        val filteredLabels = filterIgnoredLabels(labels)
        Log.d(TAG, "Nhãn sau khi lọc: $filteredLabels")

        CoroutineScope(Dispatchers.Default).launch {
            val rankedBooks = bookList.mapNotNull { book ->
                book.labels?.let { bookLabels ->
                    val filteredBookLabels = filterIgnoredLabels(bookLabels)
                    val matchedCount = filteredLabels.count { it in filteredBookLabels }
                    if (matchedCount.toDouble() / filteredLabels.size >= 0.7) {
                        book to matchedCount
                    } else null
                }
            }.sortedByDescending { it.second }.map { it.first }

            withContext(Dispatchers.Main) {
                isSearching = false
                if (rankedBooks.isNotEmpty()) {
                    Log.d(TAG, "Sách tìm kiếm theo độ liên quan: ${rankedBooks.map { it.title }}")
                    binding.btnNotifiSearch.visibility = View.GONE
                    booksAdapter.submitList(rankedBooks)
                    Log.d("INFO_DATA_OK","BOOK: $rankedBooks")

                    openVisible()
                } else {
                    Log.d(TAG, "Không tìm thấy sách phù hợp!")
                    showNoBooksFound()
                }
            }
        }
    }
    private fun showNoBooksFound() {
        if (!isSearching) {
            binding.btnNotifiSearch.visibility = View.VISIBLE
            binding.btnNotifiSearch.text = "Không tìm thấy sách!"
            openVisible()
        }
    }

    private fun openVisible() {
        binding.rlRoot.visibility = View.GONE
        binding.rvListSearch.visibility = View.VISIBLE
        binding.btnRemove.visibility = View.VISIBLE
        binding.imgPicture.visibility = View.GONE
        binding.btnCapture.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun resetToInitialState() {
        binding.rlRoot.visibility = View.VISIBLE
        binding.tlListSearch.visibility = View.VISIBLE
        binding.rvListSearch.visibility = View.GONE

        binding.edtSearch.text.clear()
        binding.edtSearch.hint = getString(R.string.text_write_find_v2)
        binding.btnRemove.visibility = View.GONE
    }

    override fun onItemClick(bookSearch: BookSearch) {
        val bookId = bookSearch.id
        val intent = Intent(requireContext(), ReadBookActivity::class.java)
        intent.putExtra("BOOK_ID", bookId)
        startActivity(intent)
    }


}

