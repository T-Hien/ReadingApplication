package vn.example.readingapplication.adapter.admin.statics

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import vn.example.readingapplication.R
import vn.example.readingapplication.model.statics.AuthorFavorite

class CustomAuthorView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val data = it.data as? AuthorFavorite
            tvContent.text = "Tác giả: ${data?.author_name}\nLượt thích: ${it.y.toInt()}"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}
