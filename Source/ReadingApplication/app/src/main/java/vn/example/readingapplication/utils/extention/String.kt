package vn.example.readingapplication.utils.extention

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import org.w3c.dom.Element


fun String?.removeCharacter(): String {
    return if (this.isNullOrBlank()) {
        ""
    } else {
        replace(Regex("\\\\+"), "")
    }
}

fun String?.convertBase64ToBitmap(): Bitmap? {
    if (TextUtils.isEmpty(this)) {
        return null
    }
    try {
        val imageAsBytes = Base64.decode(this!!.toByteArray(), Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun Element.outerHtml(): String {
    val transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer()
    val result = javax.xml.transform.stream.StreamResult(java.io.StringWriter())
    val source = javax.xml.transform.dom.DOMSource(this)
    transformer.transform(source, result)
    return result.writer.toString()
}