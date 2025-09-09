package vn.example.readingapplication.adapter.admin.comment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.databinding.FragmentHomeFavoritesBinding
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CommentFragment : Fragment(){

    private lateinit var binding: FragmentHomeFavoritesBinding
    private val listUser = mutableListOf<Note>()
    private var status:Int = 0
    private lateinit var adapter: CommentManagementAdapter
    private val itemList = mutableListOf<Note>()

    companion object {
        fun newInstance(status: Int): CommentFragment {
            val fragment = CommentFragment()
            val args = Bundle()
            args.putInt("KEY_DATA", status)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            status = it.getInt("KEY_DATA", -1)
        }
        binding = FragmentHomeFavoritesBinding.inflate(inflater, container, false)

        adapter = CommentManagementAdapter(itemList)
        binding.rcvFavoritesBook.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvFavoritesBook.adapter = adapter
        getListComment()
        binding.lnAdd.visibility = View.VISIBLE
        binding.lnTxtAdd.visibility = View.GONE
        return binding.root
    }

    fun reloadData() {
        // Gọi API để lấy dữ liệu mới và cập nhật RecyclerView
        getListComment()
    }

    private fun getListComment() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getAllNoteByStatus("1",status).enqueue(object : Callback<ResultResponse<Note>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Note>>,
                response: Response<ResultResponse<Note>>
            ) {
                if (response.isSuccessful) {
                    val comment = response.body()?.dataList
                    comment?.let { notes ->
                        itemList.clear()
                        itemList.addAll(notes)
                        adapter.notifyDataSetChanged()

                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
            }
        })
    }
}
