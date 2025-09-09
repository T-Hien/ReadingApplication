package vn.example.readingapplication.fragment.reading.list

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import vn.example.readingapplication.databinding.LayoutItemReadingListBookmarkBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PositionFragment : Fragment(){

    private lateinit var binding: LayoutItemReadingListBookmarkBinding
    private var username = ""
    private var tyle_book :String =""
    private var chapter_num = 0
    private var percent = ""
    private var bookId = 0
    private var position = "0"


    companion object {
        fun newInstance(percent:String,position:String,bookId:Int,chapnum:Int,tyle_book:String): PositionFragment {
            val fragment = PositionFragment()
            val args = Bundle()
            args.putString("PERCENT", percent)
            args.putInt("BOOK_ID",bookId)
            args.putInt("CHAP_NUM",chapnum)
            args.putString("TYLE_BOOK",tyle_book)
            args.putString("POSITON",position)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            chapter_num = it.getInt("CHAP_NUM",-1)
            tyle_book = it.getString("TYLE_BOOK").toString()
            percent = it.getString("PERCENT").toString()
            bookId = it.getInt("BOOK_ID",-1)
            position = it.getString("POSITON").toString()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    val now = LocalDateTime.now()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedNow = now.format(formatter)
        binding = LayoutItemReadingListBookmarkBinding.inflate(inflater, container, false)
        binding.txtPosition.text = "Hiện đọc đến: $percent%"
        binding.txtPercent.text = "Vị trí: $position"
        Log.d("INFOR:","POSITION_FRAGMENT:$percent")
        binding.txtTime.text = formattedNow
        binding.lnPosition.setOnClickListener {
            handleBackPressed()
        }
        return binding.root
    }
    interface OnBackPressedListener {
        fun onBackPressed(data: String) // Thay đổi để gửi dữ liệu về
    }

    private var backPressedListener: OnBackPressedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressedListener = context as? OnBackPressedListener
    }

    override fun onDetach() {
        super.onDetach()
        backPressedListener = null
    }

    fun handleBackPressed() {
        val dataToSend = position
        backPressedListener?.onBackPressed(dataToSend)
    }
}
