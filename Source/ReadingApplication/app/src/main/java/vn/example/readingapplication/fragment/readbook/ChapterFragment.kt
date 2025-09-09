package vn.example.readingapplication.fragment.readbook

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.EpubActivity
import vn.example.readingapplication.activity.user.ReadingActivity
import vn.example.readingapplication.adapter.readbook.ChapterAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentBookChapterBinding
import vn.example.readingapplication.model.Chapter
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ChapterFragment : BaseFragment(), ChapterAdapter.OnItemClickListener {

    private lateinit var binding: FragmentBookChapterBinding
    private lateinit var adapter: ChapterAdapter
    private val itemList = mutableListOf<Chapter>()

    private var bookId: Int = 0
    private var tyle :String =""
    private val TAG = "ChapterFragment"
    private var type:Int = 0


    companion object {
        fun newInstance(bookId: Int,style:String,type:Int): ChapterFragment {
            val fragment = ChapterFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", bookId)
            args.putString("KEY_TYLE", style)
            args.putInt("TYPE", type)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt("KEY_DATA", 2)
            tyle = it.getString("KEY_TYLE").toString()
            type = it.getInt("TYPE", 0)
            Log.d("DATA:", "$bookId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookChapterBinding.inflate(inflater, container, false)

        adapter = ChapterAdapter(itemList, this)
        binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
        binding.rcvBookChapter.adapter = adapter
        getListChapter()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter = ChapterAdapter(itemList, this)
        binding.rcvBookChapter.layoutManager = GridLayoutManager(requireActivity(), 1)
        binding.rcvBookChapter.adapter = adapter
        getListChapter()
    }

    private fun getListChapter() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getChapter(bookId).enqueue(object : Callback<ResultResponse<Chapter>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Chapter>>,
                response: Response<ResultResponse<Chapter>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { chapters ->
                        itemList.clear()
                        itemList.addAll(chapters)
                        adapter.notifyDataSetChanged()
                    } ?: run {
                        Toast.makeText(context, "No chapters found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, getString(R.string.text_no_chapter), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<Chapter>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }
    override fun onItemClick(chapter: Chapter) {
        if(tyle.equals("PDF")){
            val intent = Intent(requireContext(), ReadingActivity::class.java)
            intent.putExtra("BOOK_ID", bookId)
            intent.putExtra("CHAP_NUMBER", chapter.chapternumber)
            intent.putExtra("CHAP_ID", chapter.id)
            intent.putExtra("TYPE",type)
            intent.putExtra("TYLE_BOOK","PDF")
            startActivity(intent)
        }
        else{
            val intent = Intent(requireContext(), EpubActivity::class.java)
            intent.putExtra("BOOK_ID", bookId)
            intent.putExtra("CHAP_NUMBER", chapter.chapternumber)
            intent.putExtra("CHAP_ID", chapter.id)
            intent.putExtra("TYPE",type)
            intent.putExtra("TYLE_BOOK","ePub")
            startActivity(intent)
        }

    }
}
