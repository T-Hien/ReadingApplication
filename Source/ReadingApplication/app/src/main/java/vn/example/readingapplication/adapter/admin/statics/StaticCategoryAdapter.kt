package vn.example.readingapplication.adapter.admin.statics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutAdminItemStaticAuthorBinding
import vn.example.readingapplication.fragment.admin.statics.StaticCategoryFragment
import vn.example.readingapplication.model.Search
import vn.example.readingapplication.model.statics.CategoryFavorite

class StaticCategoryAdapter(private val itemList: MutableList<CategoryFavorite>,
                            private val listener: StaticCategoryFragment) :
    RecyclerView.Adapter<StaticCategoryAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(search: Search)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminItemStaticAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val search= itemList[position]
        holder.binding.txtId.text = search.categoryId.toString()
        holder.binding.txtName.text = search.categoryName
        holder.binding.txtNum.text = search.totalFavorites.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminItemStaticAuthorBinding) : RecyclerView.ViewHolder(binding.root)
}
