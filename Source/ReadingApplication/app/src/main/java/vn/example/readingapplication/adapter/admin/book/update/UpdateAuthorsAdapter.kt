package vn.example.readingapplication.adapter.admin.book.update

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.activity.admin.book.UpdateBookActivity
import vn.example.readingapplication.databinding.LayoutAdminAddbookItemCategoryBinding
import vn.example.readingapplication.model.Author

class UpdateAuthorsAdapter(
    private val itemList: MutableList<Author>,
    private val onItemUpdateListener2: UpdateBookActivity
) : RecyclerView.Adapter<UpdateAuthorsAdapter.CardViewHolder>() {

    interface OnItemUpdateListener2 {
        fun onItemUpdated2(data: List<Int?>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminAddbookItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val author = itemList[position]
        holder.binding.txtCategoryName.text = author.name // Assuming 'name' is a property of Category
        Log.d("AddCategoriesAdapter", "Binding category: ${author.name} at position $position")

        holder.binding.btnDeleteCategory.setOnClickListener {
            // Xóa danh mục khỏi danh sách
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
        }
        itemList.map { it.id}?.let { onItemUpdateListener2.onItemUpdated2(it) }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminAddbookItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
