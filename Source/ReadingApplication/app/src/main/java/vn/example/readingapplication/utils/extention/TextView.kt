package vn.example.readingapplication.utils.extention

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String?.toHtml(): Spanned? {
    if (this.isNullOrBlank()) {
        return null
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}

///*
//Stackoverflow: https://stackoverflow.com/a/65714637
// */
//fun TextView.addImage(atText: String, @DrawableRes imgSrc: Int, imgWidth: Int, imgHeight: Int) {
//    val ssb = SpannableStringBuilder(this.text)
//
//    val drawable = ContextCompat.getDrawable(this.context, imgSrc) ?: return
//    drawable.mutate()
//    drawable.setBounds(0, 0, imgWidth, imgHeight)
//    val start = text.indexOf(atText)
//    ssb.setSpan(
//        VerticalImageSpan(drawable),
//        start,
//        start + atText.length,
//        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
//    )
//    this.setText(ssb, TextView.BufferType.SPANNABLE)
//}