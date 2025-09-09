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
import vn.example.readingapplication.adapter.readbook.ListBookLikesAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentReadbookListlikeUserBinding
import vn.example.readingapplication.model.Like
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ListLikeByUserFragment : BaseFragment(),ListBookLikesAdapter.OnItemClickListener {

    private lateinit var binding: FragmentReadbookListlikeUserBinding
    private var username = "0"
    private lateinit var adapter: ListBookLikesAdapter
    private val list = mutableListOf<Like>()
    private var bookId:Int = 0
    companion object {
        private const val BOOKID = "param1"

        fun newInstance(param1: Int): ListLikeByUserFragment {
            val fragment = ListLikeByUserFragment()
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

        binding = FragmentReadbookListlikeUserBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        adapter = ListBookLikesAdapter(this,list)
        binding.rvList.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)
        binding.rvList.adapter = adapter
        if(!username.equals("0")){
            getListLike()
        }
        else{
            binding.txtNotification.visibility = View.VISIBLE

        }
        binding.btnExit.setOnClickListener(){
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
    private fun getListLike() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.findListLikeByUser(username).enqueue(object : Callback<ResultResponse<Like>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Like>>,
                response: Response<ResultResponse<Like>>
            ) {
                if (response.isSuccessful) {
                    if(response.body()?.dataList?.size!! >0){
                        response.body()?.dataList?.let { books ->
                            list.clear()
                            list.addAll(books)
                            adapter.notifyDataSetChanged()
                        }
                    }
                    else{
                        binding.txtNotification.visibility = View.VISIBLE
                    }

                }
            }

            override fun onFailure(call: Call<ResultResponse<Like>>, t: Throwable) {
                Log.d("ERROR:","Like:${t.message}")
            }
        })
    }

    override fun onItemClick(position: Int) {

    }

}
