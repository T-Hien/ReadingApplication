package vn.example.readingapplication.adapter.reading

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vn.example.readingapplication.databinding.LayoutItemReadingSearchBinding

class SearchResultAdapter(
    private val results: List<Pair<String, Int>>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutItemReadingSearchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Pair<String, Int>) {
            binding.txtPosition.text = result.second.toString()
            binding.txtContent.text = result.first
            binding.root.setOnClickListener {
//                onClick(result.second)
                Log.d("INFF","${result.second}")
//                backPressedListener?.onBackPressed4(result.second.toString())
            }
        }
    }
    interface OnBackPressedListener {
        fun onBackPressed4(data: String)
    }

    private var backPressedListener: OnBackPressedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemReadingSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount() = results.size
}
