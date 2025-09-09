package vn.example.readingapplication.retrofit

import com.google.gson.annotations.SerializedName

data class ResultResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
    val dataNum:Int?,
    @SerializedName("dataList")
    val dataList: List<T>?,


)
