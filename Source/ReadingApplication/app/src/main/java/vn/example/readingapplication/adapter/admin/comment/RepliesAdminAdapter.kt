package vn.example.readingapplication.adapter.admin.comment

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.CommentManagementActivity
import vn.example.readingapplication.databinding.LayoutItemBookCommentRepliesBinding
import vn.example.readingapplication.model.Replies
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class RepliesAdminAdapter(private val replies: List<Replies>) : RecyclerView.Adapter<RepliesAdminAdapter.ReplyViewHolder>() {

    inner class ReplyViewHolder(val binding: LayoutItemBookCommentRepliesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reply: Replies) {
            binding.txtContentReplies.text = reply.content
            Glide.with(itemView.context)
                .load(reply.auser?.image)
                .placeholder(R.drawable.img_account_girl)
                .error(R.drawable.img_account_boy)
                .into(binding.imgUserFaceReplies)
            binding.txtUsernameReplies.text = when {
                reply.auser == null -> "Unknown User"
                reply.auser.name.isNullOrEmpty() -> reply.auser.username ?: "No Username"
                else -> reply.auser.name
            }
            binding.txtTimeReplies.text = reply.createdAt
            binding.btnDelete.visibility = View.VISIBLE
            binding.btnDelete.setOnClickListener(){
                reply.auser?.let { it1 -> reply.note?.let { it2 ->
                    deleteReply(reply.id, it1.username,
                        it2,itemView.context
                    )
                } }
                Thread.sleep(1000)
                val intent = Intent(itemView.context, CommentManagementActivity::class.java)
                itemView.context.startActivity(intent)
            }
        }
    }
    private fun deleteReply(id:Int?,username:String,note:Int,context: Context) {
        val reply = Replies(
            id = id,
            createdAt = null,
            auser = User(username=username),
            note = note,
            content = ""
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteReplies(reply).enqueue(object : Callback<ResultResponse<Replies>> {
            override fun onResponse(
                call: Call<ResultResponse<Replies>>,
                response: Response<ResultResponse<Replies>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context,"Xóa phản hồi thành công!", Toast.LENGTH_LONG).show()
                    Log.d("Notification","Xóa thành công!")

                } else {
                    Log.d("Notification","Xóa thất bại!")
                }
            }
            override fun onFailure(call: Call<ResultResponse<Replies>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding = LayoutItemBookCommentRepliesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        holder.bind(replies[position])
    }

    override fun getItemCount() = replies.size
}
