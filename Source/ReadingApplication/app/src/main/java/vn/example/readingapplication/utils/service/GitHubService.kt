package vn.example.readingapplication.utils.service

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface GitHubService {
    @Multipart
    @POST("repos/{owner}/{repo}/contents/{path}")
    fun uploadFile(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Part("message") message: RequestBody,
        @Part("content") content: RequestBody,
        @Part("branch") branch: RequestBody
    ): Call<GitHubResponse>
}

data class GitHubResponse(val content: GitHubContent?)
data class GitHubContent(val download_url: String)

fun createGitHubService(): GitHubService {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(GitHubService::class.java)
}
