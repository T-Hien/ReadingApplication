package vn.example.readingapplication.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemCategoryBinding
import vn.example.readingapplication.fragment.home.CategoryFragment
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.model.Search

class CategoriesAdapter(private val itemList: MutableList<Category>, private val listener: CategoryFragment) :
    RecyclerView.Adapter<CategoriesAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(category: Category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val categories = itemList[position]
        holder.binding.txtCategoryName.text = categories.name
        holder.itemView.setOnClickListener {
            listener.onItemClick(categories)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
