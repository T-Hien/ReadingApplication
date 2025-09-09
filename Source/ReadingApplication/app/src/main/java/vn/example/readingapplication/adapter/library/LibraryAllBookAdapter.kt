package vn.example.readingapplication.adapter.library

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.user.ReadBookActivity
import vn.example.readingapplication.databinding.LayoutItemBooks1Binding
import vn.example.readingapplication.model.ReadingProgress

class LibraryAllBookAdapter(
    private val itemList: List<ReadingProgress>,
    private val listener: OnItemClickListener,
    private val context: Context
) : RecyclerView.Adapter<LibraryAllBookAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBooks1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val reading = itemList[position]
        Glide.with(holder.itemView.context)
            .load(reading.abook?.cover_image) // Load ảnh từ URL
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)
        val category = reading.abook?.listBookCategory?.firstOrNull()?.category?.name ?: "N/A"
        holder.binding.txtGenresBook.text = "${category}"
        holder.binding.txtBookName.text = reading.abook?.title?:"Null"

        // Thiết lập tiến độ ban đầu cho ProgressBar
        val inputPercent: Float = reading.progressPercentage?: 0f // Lấy giá trị phần trăm tiến độ từ dữ liệu
        val progressValue = inputPercent.toInt()
        holder.binding.progressBar.progress = progressValue

        // Giả lập việc cập nhật tiến độ (nếu cần)
        updateProgress(holder, progressValue) // Cập nhật theo dữ liệu thực tế

        holder.itemView.setOnClickListener {
            listener.onItemClick(position)
            val bookId = reading.abook?.id
            val intent = Intent(context, ReadBookActivity::class.java).apply {
                putExtra("BOOK_ID", bookId)
            }
            context.startActivity(intent)
        }
    }
    private fun updateProgress(holder: CardViewHolder, percent: Int) {
        holder.binding.progressBar.progress = percent
    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBooks1Binding) : RecyclerView.ViewHolder(binding.root)
}
