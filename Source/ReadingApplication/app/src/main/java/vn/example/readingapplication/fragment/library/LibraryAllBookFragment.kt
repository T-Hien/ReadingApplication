package vn.example.readingapplication.fragment.library

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.library.LibraryAllBookAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentLibraryBookBinding
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient


class LibraryAllBookFragment : BaseFragment(), LibraryAllBookAdapter.OnItemClickListener {

    private lateinit var binding: FragmentLibraryBookBinding
    private lateinit var libraryadapter: LibraryAllBookAdapter
    private val itemList = mutableListOf<ReadingProgress>()
    private var username = ""
    override fun onResume() {
        super.onResume()
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        binding.txtNotiLibrary.visibility = View.VISIBLE
        getAllLibrary()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBookBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()

        libraryadapter = LibraryAllBookAdapter(itemList, this,requireContext())
        binding.rcvLibraryBook.layoutManager = GridLayoutManager(requireActivity(), 3)
        binding.rcvLibraryBook.adapter = libraryadapter
        getAllLibrary()
        return binding.root
    }
    fun getAllLibrary() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookReadingAll(username).enqueue(object : Callback<ResultResponse<List<ReadingProgress>>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<List<ReadingProgress>>>,
                response: Response<ResultResponse<List<ReadingProgress>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { nestedList ->
                        itemList.clear()
                        nestedList.forEach { readings ->
                            itemList.addAll(readings)
                        }
                        binding.txtNotiLibrary.visibility = View.GONE
                        libraryadapter.notifyDataSetChanged()
                    }
                }
                else{
                    binding.txtNotiLibrary.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<ResultResponse<List<ReadingProgress>>>, t: Throwable) {
                Log.d("ERRORLibraryAll", "${t.message}")
                binding.txtNotiLibrary.visibility = View.VISIBLE
            }
        })
    }


    override fun onItemClick(position: Int) {
//        // Bắt đầu Activity mới
//        val intent = Intent(requireContext(), ReadBookActivity::class.java)
//        intent.putExtra("ITEM_POSITION", position)
//        startActivity(intent)
    }
}
