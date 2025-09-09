package vn.example.readingapplication.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.adapter.NotificationAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentNotificationBinding
import vn.example.readingapplication.model.Notification
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class NotificationFragment: BaseFragment(),NotificationAdapter.OnItemClickListener {

    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notifiadapter: NotificationAdapter
    private val notifiList = mutableListOf<Notification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        notifiadapter = NotificationAdapter(notifiList,this,requireContext())
        binding.rcvNotification.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.VERTICAL, false)// Set layout manager to horizontal
        binding.rcvNotification.adapter = notifiadapter
        getNotification()
        return binding.root
    }

    private fun getNotification() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getNotification().enqueue(object : Callback<ResultResponse<Notification>> {

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResultResponse<Notification>>,
                response: Response<ResultResponse<Notification>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.dataList?.let { notifications ->
                        notifiList.clear()
                        notifiList.addAll(notifications)
                        notifiadapter.notifyDataSetChanged()
                    }
                } else {
                    binding.txtNotification.visibility = View.VISIBLE
                }
            }
            override fun onFailure(call: Call<ResultResponse<Notification>>, t: Throwable) {
                Log.d("ERRO","${t.message}")
            }
        })
    }
    override fun onItemClick(position: Int) {
    }
}