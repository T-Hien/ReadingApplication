package vn.example.readingapplication.adapter.readbook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.LayoutItemBookChapBinding
import vn.example.readingapplication.fragment.readbook.ChapterFragment
import vn.example.readingapplication.model.Chapter

class ChapterAdapter(private val itemList: MutableList<Chapter>,
                     private val listener: ChapterFragment
) : RecyclerView.Adapter<ChapterAdapter.CardViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(chapter: Chapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutItemBookChapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val chapter = itemList[position]
        if(chapter.title!=null || !chapter.title.equals("")){
            holder.binding.txtChapter.text =holder.itemView.context.getString(R.string.text_chapter_name_v2)+" ${chapter.chapternumber} ${chapter.title?:""}"
        }
        else{
            holder.binding.txtChapter.text =holder.itemView.context.getString(R.string.text_chapter_name_v2)+" ${chapter.chapternumber}"
        }
        holder.itemView.setOnClickListener {
            listener.onItemClick(chapter)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutItemBookChapBinding) : RecyclerView.ViewHolder(binding.root)
}
