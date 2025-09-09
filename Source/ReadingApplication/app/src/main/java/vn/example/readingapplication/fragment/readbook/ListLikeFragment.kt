package vn.example.readingapplication.fragment.readbook

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
import vn.example.readingapplication.adapter.readbook.LikesAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentReadbookListlikeBinding
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ListLikeFragment : BaseFragment() {

    private lateinit var binding: FragmentReadbookListlikeBinding
    private var username = "0"
    private lateinit var adapter: LikesAdapter
    private val list = mutableListOf<Like>()
    private var bookId:Int = 0
    companion object {
        private const val BOOKID = "param1"

        fun newInstance(param1: Int): ListLikeFragment {
            val fragment = ListLikeFragment()
            val args = Bundle()
            args.putInt(BOOKID, param1)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            bookId = it.getInt(BOOKID)
        }

        binding = FragmentReadbookListlikeBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()

        binding.lnRoot.setOnClickListener(){
            parentFragmentManager.popBackStack()
        }
        binding.lnRoot2.setOnClickListener(){
            parentFragmentManager.popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            bookId = it.getInt(BOOKID)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter = LikesAdapter(list)
        binding.rvList.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)
        binding.rvList.adapter = adapter
        getListLike()
    }

    private fun getListLike() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.findListLikeByBook(bookId).enqueue(object : Callback<ResultResponse<Like>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {

                    response.body()?.dataList?.let { books ->

                        list.clear()
                        list.addAll(books)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.d("ERROR:","Like:${t.message}")
            }
        })
    }

}
