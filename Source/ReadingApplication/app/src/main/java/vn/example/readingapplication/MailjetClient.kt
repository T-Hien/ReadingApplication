package vn.example.readingapplication

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MailjetClient(private val apiKey: String, private val secretKey: String) {

    private val client = OkHttpClient()

    fun sendEmail(to: String, subject: String, text: String, callback: (Boolean, String?) -> Unit) {
        val url = "https://api.mailjet.com/v3.1/send"

        // Tạo JSON một cách chính xác và an toàn
        val json = JSONObject().apply {
            put("Messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("From", JSONObject().apply {
                        put("Email", "n20dccn100@student.ptithcm.edu.vn")
                        put("Name", "student.ptithcm.edu.vn")
                    })
                    put("To", JSONArray().apply {
                        put(JSONObject().apply {
                            put("Email", to)
                        })
                    })
                    put("Subject", subject)
                    put("TextPart", text)
                    put("HTMLPart", "<html><body><p>$text</p></body></html>")
                })
            })
        }

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, json.toString())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .addHeader("Authorization", Credentials.basic(apiKey, secretKey))
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false, "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, "Error: ${response.code} - ${response.message}, Response Body: ${response.body?.string()}")
                }
            }
        })
    }
}
