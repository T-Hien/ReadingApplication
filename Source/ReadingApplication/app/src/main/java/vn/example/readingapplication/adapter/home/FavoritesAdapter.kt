package vn.example.readingapplication.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemFavoritesbookBinding
import vn.example.readingapplication.fragment.home.FavoritesFragment
import vn.example.readingapplication.model.Favorite

class FavoritesAdapter(private val itemList: MutableList<Favorite>,
                       private val listener: FavoritesFragment) :
    RecyclerView.Adapter<FavoritesAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(fav: Favorite)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemFavoritesbookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val favorite = itemList[position]
        Glide.with(holder.itemView.context)
            .load(favorite.abook?.cover_image)
            .placeholder(R.drawable.bg_library_admin)
            .error(R.drawable.img_book_conan)
            .into(holder.binding.imgBook)

        holder.binding.txtBook.text = favorite.abook?.title
        holder.binding.txtGenresBook.text = favorite.abook?.listBookCategory?.firstOrNull()?.category?.name
        holder.binding.txtLike.text = favorite.number.toString()
        holder.binding.txtTypeBook.text = favorite.abook?.type_file
        holder.itemView.setOnClickListener {
            listener.onItemClick(favorite)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemFavoritesbookBinding) : RecyclerView.ViewHolder(binding.root)
}
