package vn.example.readingapplication.adapter.admin.book

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.book.ShowDeleteBookDialog
import vn.example.readingapplication.activity.admin.book.UpdateBookActivity
import vn.example.readingapplication.databinding.LayoutItemAdminBookBinding
import vn.example.readingapplication.model.Book

class BookManagementAdapter(private val itemList: List<Book>) :
    RecyclerView.Adapter<BookManagementAdapter.CardViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemAdminBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val book = itemList[position]
        Log.d("Adapter", "Categories: ${book}")
        if(book.active==1){
            holder.binding.txtLock.text = "Mở"
        }
        val imageUrl:String? = book.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Load ảnh từ URL
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)

        holder.binding.txtBook.text = book.title

        val authors = book.listDetailAuthor?.joinToString { it.author?.name.toString() }
        holder.binding.txtAuthor.text = "Tác giả: ${authors}"

//        holder.itemView.setOnClickListener {
//            listener.onItemClick(book)
//        }
        holder.binding.btnEditBook.setOnClickListener(){
            Log.d("INFF","$book")
            val intent = Intent(holder.itemView.context,UpdateBookActivity::class.java)
            intent.putExtra("bookid", book.id)
            intent.putExtra("status", book.status)
            intent.putExtra("type_file", book.type_file)

            holder.itemView.context.startActivity(intent)
        }
        holder.binding.btnDeleteBook.setOnClickListener(){
            val dialogFragment =
                book.id?.let { it1 ->
                    book.active?.let { it2 -> ShowDeleteBookDialog.newInstance(it1, it2) }
                }

            if (dialogFragment != null) {
                dialogFragment.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "UpdateCategoryDialog")
            }

        }

    }



    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemAdminBookBinding) : RecyclerView.ViewHolder(binding.root)
}
