package vn.example.readingapplication.fragment.reading

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nl.siegmann.epublib.epub.EpubReader
import okhttp3.OkHttpClient
import okhttp3.Request
import vn.example.readingapplication.adapter.reading.SearchResultAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentReadingSearchTextBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class SearchReadingFragment : BaseFragment() {
    private lateinit var binding: FragmentReadingSearchTextBinding
    private var link = ""
    private val client = OkHttpClient()
    private var book: nl.siegmann.epublib.domain.Book? = null
    private var listener: OnSearchResultClickListener? = null

    companion object {
        private const val ARG_PARAM1 = "param1"

        fun newInstance(linkChap: String): SearchReadingFragment {
            val fragment = SearchReadingFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, linkChap)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSearchResultClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSearchResultClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            link = it.getString(ARG_PARAM1).toString()
        }
        binding = FragmentReadingSearchTextBinding.inflate(inflater, container, false)
        binding.webViewContent.visibility = View.GONE
        setupWebView()
        setupRecyclerView()
        setupSearchButton()

        return binding.root
    }

    private fun setupWebView() {
        binding.webViewContent.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }
        loadEpub(link)
    }

    private fun setupRecyclerView() {
        binding.recyclerViewResults.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadEpub(fileUrl: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val epubFile = withContext(Dispatchers.IO) {
                downloadFile(fileUrl)
            }
            epubFile?.let {
                book = withContext(Dispatchers.IO) {
                    readEpubFile(it)
                }
                book?.let {
                    displayEpubContent(it)
                } ?: run {
                    Toast.makeText(requireContext(), "Failed to read book", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Failed to download book", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayEpubContent(book: nl.siegmann.epublib.domain.Book) {
        val htmlContent = StringBuilder()
        htmlContent.append("<html><head>")
        htmlContent.append("<style>")
        htmlContent.append("body { font-size: 15px; color: #000; background-color: #FFFF; }")
        htmlContent.append("</style></head><body>")

        for (resource in book.contents) {
            val content = readResourceContent(resource)
            if (content.isNotEmpty()) {
                htmlContent.append(content).append("<br><br>")
            }
        }

        htmlContent.append("</body></html>")

        Log.d("SearchF", "Loading all HTML content: ${htmlContent.toString().take(1000)}...")

        binding.webViewContent.loadDataWithBaseURL(null, htmlContent.toString(), "text/html", "UTF-8", null)
    }

    private fun downloadFile(fileUrl: String): File? {
        return try {
            val request = Request.Builder().url(fileUrl).build()
            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()
            val epubFile = File(requireContext().cacheDir, "downloadedBook.epub")
            inputStream?.let {
                FileOutputStream(epubFile).use { output ->
                    it.copyTo(output)
                }
                epubFile
            }
        } catch (e: Exception) {
            Log.e("SearchReadingFragment", "Error downloading file: ${e.message}", e)
            null
        }
    }

    private fun readEpubFile(file: File): nl.siegmann.epublib.domain.Book? {
        return try {
            val epubReader = EpubReader()
            epubReader.readEpub(FileInputStream(file))
        } catch (e: Exception) {
            Log.e("SearchReadingFragment", "Error reading EPUB file: ${e.message}", e)
            null
        }
    }

    private fun readResourceContent(resource: nl.siegmann.epublib.domain.Resource): String {
        return try {
            val inputStream = resource.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            reader.use { it.readText() }
        } catch (e: Exception) {
            Log.e("SearchReadingFragment", "Error reading resource: ${e.message}", e)
            "Failed to read resource"
        }
    }

    private fun setupSearchButton() {
        binding.btnSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString()
            if (query.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    val results = withContext(Dispatchers.IO) {
                        searchInBook(query)
                    }
                    displaySearchResults(results)
                }
            }
        }
    }

    private suspend fun searchInBook(query: String): List<Pair<String, Int>> {
        val results = mutableListOf<Pair<String, Int>>()
        book?.contents?.forEachIndexed { index, resource ->
            val content = readResourceContent(resource)
            val lines = content.split("\n")
            for (line in lines) {
                if (line.contains(query, true)) {
                    results.add(Pair(line, index))
                }
            }
        }
        return results
    }

    private fun displaySearchResults(results: List<Pair<String, Int>>) {
        val adapter = SearchResultAdapter(results) { position ->
            listener?.onSearchResultClick(position)
        }
        binding.recyclerViewResults.adapter = adapter
    }

    interface OnSearchResultClickListener {
        fun onSearchResultClick(position: Int)
    }
}
