package vn.example.readingapplication.adapter.readbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemBookCommentBinding
import vn.example.readingapplication.model.Note

class CommentAdapter(private val itemList: MutableList<Note>) :
    RecyclerView.Adapter<CommentAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBookCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val note = itemList[position]
        holder.binding.txtTimeComment.text = note.createdAt
        holder.binding.txtContentComment.text = note.content
        holder.binding.txtChapter.text =if(note.chapternumber.toString().equals("")||note.chapternumber.toString().equals("null")){
            ""
        }
        else{
            holder.itemView.context.getString(R.string.text_chapter_name_v2)+" ${note.chapternumber.toString()?:""}"
        }
        holder.binding.txtUsernameComment.text = when {
            note.auser== null -> "Unknown User"
            note.auser.name.isNullOrEmpty() -> note.auser.username ?: "No Username"
            else -> note.auser.name
        }
        Glide.with(holder.itemView.context)
            .load(note.auser?.image) // Load ảnh từ URL
            .placeholder(R.drawable.img_account_girl)
            .error(R.drawable.img_account_boy)
            .into(holder.binding.imgUserFaceComment)
        if(note.status==1){
            holder.binding.txtDescriptionStatus.visibility = View.VISIBLE
            holder.binding.txtContentComment.text = note.description
        }


        val repliesAdapter = note.listReplies?.let { RepliesAdapter(it) }
        holder.binding.rcvListReplies.layoutManager = LinearLayoutManager(holder.binding.root.context)
        holder.binding.rcvListReplies.adapter = repliesAdapter
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBookCommentBinding) : RecyclerView.ViewHolder(binding.root)
}
