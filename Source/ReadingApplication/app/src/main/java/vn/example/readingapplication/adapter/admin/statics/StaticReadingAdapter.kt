package vn.example.readingapplication.adapter.admin.statics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutAdminItemStaticAuthorBinding
import vn.example.readingapplication.fragment.admin.statics.StaticReadingFragment
import vn.example.readingapplication.model.Search
import vn.example.readingapplication.model.statics.ReadingStatic

class StaticReadingAdapter(private val itemList: MutableList<ReadingStatic>,
                           private val listener: StaticReadingFragment) :
    RecyclerView.Adapter<StaticReadingAdapter.CardViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(search: Search)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminItemStaticAuthorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val search= itemList[position]
        holder.binding.txtId.text = search.username
        holder.binding.txtName.text = search.name
        holder.binding.txtNum.text = search.reading_count.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminItemStaticAuthorBinding) : RecyclerView.ViewHolder(binding.root)
}
