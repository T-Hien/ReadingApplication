package vn.example.readingapplication.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.adapter.library.LibraryAllBookAdapter.OnItemClickListener
import vn.example.readingapplication.databinding.LayoutItemNotificationBinding
import vn.example.readingapplication.fragment.NotificationFragment
import vn.example.readingapplication.model.Notification

class NotificationAdapter(private val itemList: MutableList<Notification>,
                          private val listener: NotificationFragment,
                          private val context: Context
) :
    RecyclerView.Adapter<NotificationAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val notifi = itemList[position]
        Glide.with(holder.itemView.context)
            .load(notifi.abook?.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgNotification)
        holder.binding.txtTimeNotification.text = notifi.createdAt.toString()
        holder.binding.txtNotification.text = notifi.message?:"Unknown Title"
        holder.binding.txtTitlleNotification.text = notifi.abook?.title?:"Unknown Title"
        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
            val bookId = notifi.abook?.id
            val intent = Intent(context, ReadBookActivity::class.java).apply {
                putExtra("BOOK_ID", bookId)
            }
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}
