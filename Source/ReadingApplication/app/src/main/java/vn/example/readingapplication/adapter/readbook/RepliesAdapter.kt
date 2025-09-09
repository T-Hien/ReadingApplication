package vn.example.readingapplication.adapter.readbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemBookCommentRepliesBinding
import vn.example.readingapplication.model.Replies

class RepliesAdapter(private val replies: List<Replies>) : RecyclerView.Adapter<RepliesAdapter.ReplyViewHolder>() {

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
        }
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
