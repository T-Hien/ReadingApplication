package vn.example.readingapplication.adapter.admin.statics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutAdminItemStaticAuthorBinding
import vn.example.readingapplication.fragment.admin.statics.StaticBookFragment
import vn.example.readingapplication.model.Favorite
import vn.example.readingapplication.model.Search

class StaticBookAdapter(private val itemList: MutableList<Favorite>,
                        private val listener: StaticBookFragment) :
    RecyclerView.Adapter<StaticBookAdapter.CardViewHolder>() {
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
        holder.binding.txtName.text = search.abook?.title
        holder.binding.txtNum.text = search.number.toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminItemStaticAuthorBinding) : RecyclerView.ViewHolder(binding.root)
}
