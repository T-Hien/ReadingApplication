package vn.example.readingapplication.adapter.admin.book.add

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.activity.admin.book.AddBookActivity
import vn.example.readingapplication.databinding.LayoutAdminAddbookItemChapterBinding
import vn.example.readingapplication.model.Chapter

class AddChapterAdapter(
    private val itemList: MutableList<Chapter>,
    private val onItemUpdateListener3: AddBookActivity
) : RecyclerView.Adapter<AddChapterAdapter.CardViewHolder>() {

    interface OnItemUpdateListener3 {
        fun onItemUpdated3(data: List<Chapter>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminAddbookItemChapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val chapter = itemList[position]
        holder.binding.txtChapterName.text = chapter.title
        holder.binding.txtChapterNum.text = "Chapter ${position+1}:"

        Log.d("AddCategoriesAdapter", "Binding category: ${chapter} at position $position")

        holder.binding.btnDeleteCategory.setOnClickListener {
            // Xóa danh mục khỏi danh sách
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
//            val filePath = chapter.file_path.toString()
//            val sha = chapter.createdAt.toString()
//            deleteFileFromGitHub(filePath,sha)
        }
        onItemUpdateListener3.onItemUpdated3(itemList.map {it})
    }
//    private fun deleteFileFromGitHub(filePath: String, fileSha: String) {
//        val apiService = RetrofitClient.getClient2().create(ApiService::class.java)
//        val token = "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"  // Set your token here
//
//        val jsonPayload = """
//        {
//            "message": "delete file",
//            "sha": "$fileSha"
//        }
//    """.trimIndent()
//
//        val body = jsonPayload.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
//
//        apiService.deleteFile("T-Hien", "ReadingApplication", filePath, body, "token $token")
//            .enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                    if (response.isSuccessful) {
//                        Log.e("DeleteFileSucces", "File deleted successfully")
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("DeleteFileError", "Response code: ${response.code()}, Error body: $errorBody")
//                    }
//                }
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    Log.e("DeleteFileFailure", "Error: ${t.message}", t)
//                }
//            })
//    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminAddbookItemChapterBinding) : RecyclerView.ViewHolder(binding.root)
}
