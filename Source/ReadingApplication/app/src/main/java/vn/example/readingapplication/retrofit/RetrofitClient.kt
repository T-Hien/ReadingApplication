package vn.example.readingapplication.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private const val BASE_URL = "http://192.168.1.59:8080/api/"
 //   private const val BASE_URL = "http://192.168.1.18:8080/api/"
//    private const val BASE_URL = "http://192.168.100.113:8080/api/"
    private var retrofit: Retrofit? = null
    const val CONNECTION_TIMEOUT_IN_SECONDS = 30L

    private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .connectTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(CONNECTION_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .build()

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
    private const val GIT_URL = "https://api.github.com/"

    fun getClient2(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GIT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
