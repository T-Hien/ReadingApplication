package vn.example.readingapplication.adapter.admin.statics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutAdminItemStaticAuthorBinding
import vn.example.readingapplication.fragment.admin.statics.StaticAuthorFragment
import vn.example.readingapplication.model.Search
import vn.example.readingapplication.model.statics.AuthorFavorite

class StaticAuthorAdapter(private val itemList: MutableList<AuthorFavorite>,
                          private val listener: StaticAuthorFragment) :
    RecyclerView.Adapter<StaticAuthorAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(search: Search)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminItemStaticAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val search= itemList[position]
        holder.binding.txtId.text = search.id.toString()
        holder.binding.txtName.text = search.author_name
        holder.binding.txtNum.text = search.total_favorites.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminItemStaticAuthorBinding) : RecyclerView.ViewHolder(binding.root)
}
