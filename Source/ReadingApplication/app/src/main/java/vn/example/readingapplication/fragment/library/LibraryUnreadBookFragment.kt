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
import vn.example.readingapplication.adapter.library.LibraryUnreadBookAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentLibraryBookBinding
import vn.example.readingapplication.model.ReadingProgress
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class LibraryUnreadBookFragment : BaseFragment(),LibraryUnreadBookAdapter.OnItemClickListener {

    private lateinit var binding: FragmentLibraryBookBinding
    private lateinit var adapter: LibraryUnreadBookAdapter
    private val itemList = mutableListOf<ReadingProgress>()
    private val status = "Unread"
    private var username = ""

    override fun onResume() {
        super.onResume()
        getUnreadLibrary()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLibraryBookBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val username2 = sharedPreferences?.getString("username", null)
        if (username2 != null) {
            username = username2
        }else{
            binding.txtNotiLibrary.visibility = View.VISIBLE
        }
        adapter = LibraryUnreadBookAdapter(itemList,this,requireContext())
        binding.rcvLibraryBook.layoutManager = GridLayoutManager(requireActivity(), 3, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvLibraryBook.adapter = adapter
        getUnreadLibrary()
        return binding.root
    }

    fun getUnreadLibrary() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getBookReadingByStatus(username,status).enqueue(object :
            Callback<ResultResponse<List<ReadingProgress>>> {
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
                        adapter.notifyDataSetChanged()
                    }
                }
                else{
                    binding.txtNotiLibrary.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResultResponse<List<ReadingProgress>>>, t: Throwable) {
                binding.txtNotiLibrary.visibility = View.VISIBLE
                Log.d("ERROR_Unread", "${t.message}")
            }
        })
    }

    override fun onItemClick(position: Int) {

    }
}