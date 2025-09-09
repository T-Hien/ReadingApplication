package vn.example.readingapplication.utils.extention

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log

fun Context.convertDpToPixel(dp: Float): Float {
    val resources = this.resources
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.goToFeatureWithDeeplink(deeplink: String?) {
    if (!TextUtils.isEmpty(deeplink)) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(deeplink)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Context?.getStringResourceFromEC(errorCode: String?): String? {
    if (this == null || TextUtils.isEmpty(errorCode)) {
        return ""
    }
    return try {
        val packageName = this.packageName
        val resId = resources.getIdentifier(errorCode, "string", packageName)
        Log.d("getStringResourceFromEC", "getStringResourceFromEC ${getString(resId)}")
        getString(resId)
    } catch (e: Exception) {
        Log.e("getMessageError", e.toString())
        ""
    }
}