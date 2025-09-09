package vn.example.readingapplication.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemSearchBookBinding
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.model.Book

class SearchBookKeywordAdapter(private val itemList: MutableList<Book>,
                               private val listener: SearchBookFragment) :
    RecyclerView.Adapter<SearchBookKeywordAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemSearchBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val book = itemList[position]
//        holder.binding.txtBookSearch.text = search.keyword
        val imageUrl:String? = book.cover_image
        Glide.with(holder.itemView.context)
            .load(imageUrl) // Load ảnh từ URL
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)

        holder.binding.txtBookTitle.text = book.title

        val authors = book.listDetailAuthor?.joinToString { it.author?.name.toString() }
        holder.binding.txtAuthorBook.text = holder.itemView.context.getString(R.string.text_author_name)+" ${authors}"

        val category = book.listBookCategory?.joinToString { it.category?.name.toString() }
        holder.binding.txtGenreBook.text = holder.itemView.context.getString(R.string.text_category_name)+" ${category}"

        val favorite = book.favorite?.number.toString()
        holder.binding.txtLikeBook.text = "${favorite}"
        holder.itemView.setOnClickListener {
            listener.onItemClick(book)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemSearchBookBinding) : RecyclerView.ViewHolder(binding.root)
}
