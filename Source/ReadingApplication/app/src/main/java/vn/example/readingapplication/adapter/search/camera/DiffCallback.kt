package vn.example.readingapplication.adapter.search.camera

import androidx.recyclerview.widget.DiffUtil
import vn.example.readingapplication.model.Book

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id // So sánh ID của hai sách
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem // So sánh toàn bộ nội dung
    }
}
