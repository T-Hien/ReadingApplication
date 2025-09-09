package vn.example.readingapplication.adapter.admin.comment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.CommentManagementActivity
import vn.example.readingapplication.activity.admin.ShowDeleteDialog
import vn.example.readingapplication.databinding.LayoutItemAdminCommentBinding
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.Replies
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class CommentManagementAdapter(private val itemList: List<Note>) :
    RecyclerView.Adapter<CommentManagementAdapter.CardViewHolder>() {

    private var username = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemAdminCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        // Lấy username từ SharedPreferences
        val sharedPreferences =
            holder.itemView.context.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0") ?: ""

        val note = itemList[position]
        holder.binding.txtTimeComment.text = note.createdAt
        holder.binding.txtContentComment.text = note.content
        holder.binding.btnDelete.text = "Khóa"
        if(note.status==1){
            holder.binding.txtContentComment.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.background_delete))
            holder.binding.txtContentReply.visibility = View.VISIBLE
            holder.binding.txtContentReply.text = note.description
            holder.binding.btnReply.visibility = View.GONE
            holder.binding.btnDelete.text = "Mở"
            holder.binding.btnDelete.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.background_edit))
        }
        holder.binding.txtBookName.visibility = View.VISIBLE
        holder.binding.txtBookName.text = note.abook?.title.toString()

        holder.binding.txtChapter.text =
            if (note.chapternumber.toString().equals("") || note.chapternumber.toString()
                    .equals("null")
            ) {
                ""
            } else {
                "Chương ${note.chapternumber.toString() ?: ""}"
            }

        holder.binding.txtUsernameComment.text = when {
            note.auser == null -> "Unknown User"
            note.auser.name.isNullOrEmpty() -> note.auser.username ?: "No Username"
            else -> note.auser.name
        }

        Glide.with(holder.itemView.context)
            .load(note.auser?.image)
            .placeholder(R.drawable.img_account_girl)
            .error(R.drawable.img_account_boy)
            .into(holder.binding.imgUserFaceComment)

        val repliesAdapter = note.listReplies?.let { RepliesAdminAdapter(it) }
        holder.binding.rcvListReplies.layoutManager =
            LinearLayoutManager(holder.binding.root.context)
        holder.binding.rcvListReplies.adapter = repliesAdapter

        if(note.status==0){
            holder.binding.btnDelete.setOnClickListener {
                val reasons = arrayOf("Vi phạm thuần phong mỹ tục", "Vi phạm chuẩn mực", "Nội dung không phù hợp","Spam hoặc quảng cáo")
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Chọn nguyên nhân khóa bình luận")
                builder.setItems(reasons) { _, which ->
                    val selectedReason = reasons[which]
                    note.id?.let { noteId ->
                        val dialogFragment = ShowDeleteDialog.newInstance(4, noteId, "0",selectedReason)
                        dialogFragment.show(
                            (holder.itemView.context as AppCompatActivity).supportFragmentManager,
                            "ShowDeleteDialog"
                        )
                    }
                }
                builder.show()
            }
        }
        else {
            holder.binding.btnDelete.setOnClickListener {
                note.id?.let { noteId ->
                    val dialogFragment =
                        ShowDeleteDialog.newInstance(4, noteId, "1", "")
                    dialogFragment.show(
                        (holder.itemView.context as AppCompatActivity).supportFragmentManager,
                        "ShowDeleteDialog"
                    )
                }
            }
        }

        holder.binding.btnReply.setOnClickListener {
            getUserInfo(holder)
            holder.binding.lnReply.visibility = View.VISIBLE
        }

        holder.binding.btnAdminReply.setOnClickListener {
            createReply(holder, note.id)
        }
    }

    private fun createReply(holder: CardViewHolder, noteId: Int?) {
        val replyContent = holder.binding.edtContentReply.text.toString()
        if (replyContent.isNotEmpty() && noteId != null) {
            val reply = Replies(
                id = null,
                createdAt = null,
                auser = User(username = username),
                note = noteId,
                content = replyContent
            )
            val apiService = RetrofitClient.getClient().create(ApiService::class.java)
            apiService.createReplies(reply).enqueue(object : Callback<ResultResponse<Replies>> {
                override fun onResponse(
                    call: Call<ResultResponse<Replies>>,
                    response: Response<ResultResponse<Replies>>
                ) {
                    if (response.isSuccessful) {
                        val resultResponse = response.body()
                        if (resultResponse != null && resultResponse.status == 200) {
                            Toast.makeText(
                                holder.itemView.context,
                                "Thêm phản hồi thành công!",
                                Toast.LENGTH_LONG
                            ).show()
                            holder.binding.lnReply.visibility = View.GONE
                            // Refresh lại activity
                            val intent = Intent(
                                holder.itemView.context,
                                CommentManagementActivity::class.java
                            )
                            holder.itemView.context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                holder.itemView.context,
                                resultResponse?.message,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<ResultResponse<Replies>>, t: Throwable) {
                    Toast.makeText(
                        holder.itemView.context,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("ERROREditAccount: ", "${t.message}")
                }
            })
        } else {
            Toast.makeText(
                holder.itemView.context,
                "Vui lòng nhập nội dung phản hồi",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getUserInfo(holder: CardViewHolder) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val reply = response.body()?.data
                    holder.binding.txtUsernameReply.text = reply?.name
                    Glide.with(holder.itemView.context)
                        .load(reply?.image)
                        .placeholder(R.drawable.img_account_girl)
                        .error(R.drawable.img_account_boy)
                        .into(holder.binding.imgUserFaceReply)
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemAdminCommentBinding) :
        RecyclerView.ViewHolder(binding.root)
}
