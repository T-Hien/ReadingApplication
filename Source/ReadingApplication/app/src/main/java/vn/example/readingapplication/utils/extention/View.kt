package vn.android.myekyc.utils.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import android.os.SystemClock
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import vn.example.readingapplication.utils.customView.Blocker
import kotlin.math.roundToInt


fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)

fun ImageView.loadImage(redId: Int) =
    Glide.with(this.context).load(redId).into(this)

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

class SafeClickListener(
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < Blocker.DEFAULT_BLOCK_TIME) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }
}

fun View.absX(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[0]
}

fun View.absY(): Int {
    val location = IntArray(2)
    this.getLocationOnScreen(location)
    return location[1]
}

/*
    https://stackoverflow.com/a/57799999
 */
fun Drawable.overrideColor(context: Context, colorInt: Int) {
    when (val muted = this.mutate()) {
        is GradientDrawable -> muted.setColor(ContextCompat.getColor(context, colorInt))
        is ShapeDrawable -> muted.paint.color = ContextCompat.getColor(context, colorInt)
        is ColorDrawable -> muted.color = ContextCompat.getColor(context, colorInt)
        else -> Log.d("Drawable", "Not a valid background type")
    }
}

fun View.setColorDrawable(context: Context, color: Int) {
    try {
        this.background?.overrideColor(context, color)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun View.setGradientColor(
    startColor: Int,
    endColor: Int,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.TOP_BOTTOM
) {
    val gradient = GradientDrawable(orientation, intArrayOf(startColor, endColor))
    this.background = gradient
}

fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun View.startAnimShake() {
    val shake = TranslateAnimation(0F, 10F, 0F, 0F)
    shake.duration = 500
    shake.interpolator = CycleInterpolator(7F)
    startAnimation(shake)
}

@SuppressLint("ObjectAnimatorBinding")
fun View.startAnimProgress(
    progressStart: Int = 0,
    progressEnd: Int,
    duration: Long = 2000,
    onAnimationEnd: () -> Unit = {},
) {
    val progressAnimator = ObjectAnimator.ofInt(
        this,
        "progress",
        progressStart,
        progressEnd
    )
    progressAnimator.duration = duration
    progressAnimator.setAutoCancel(true)
    progressAnimator.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
            onAnimationEnd()
        }
    })
    return progressAnimator.start()
}

fun View.adaptViewForInserts() {
    val originalTopPadding = this.paddingTop
    Log.d("adaptViewForInserts", "originalTopPadding: $originalTopPadding")
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
        this.setOnApplyWindowInsetsListener { _, windowInsets -> // Update view's top padding to accommodate system window top inset
            val systemTopPadding = windowInsets.systemWindowInsetTop + originalTopPadding
            Log.d("adaptViewForInserts", "onApplyWindowInsets: $systemTopPadding")
            this.setPadding(0, systemTopPadding, 0, 0)
            windowInsets
        }
    }
}

fun View?.removeSelf() {
    this ?: return
    val parentView = parent as? ViewGroup ?: return
    parentView.removeView(this)
}

fun ImageView.loadFromBase64(base64: String) {
    val imageByteArray: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    Glide.with(this.context)
        .asBitmap()
        .load(imageByteArray)
        .into(this)
}

fun TextView.setTextWithParam(value: String, stringId: Int? = null) {
    if (stringId != null) {
        val formattedText = this.context.getString(stringId, value)
        this.text = formattedText
    } else {
        this.text = value
    }
}

fun TextView.setTextAutoVisibleGone(value: String?, stringId: Int? = null) {
    if (value.isNullOrEmpty()) {
        this.gone()
    } else {
        this.setTextWithParam(value, stringId)
        this.visible()
    }
}