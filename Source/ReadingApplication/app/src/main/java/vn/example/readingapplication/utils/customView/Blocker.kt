package vn.example.readingapplication.utils.customView

import android.os.Handler

class Blocker {
    private var isBlockClick: Boolean = false

    @JvmOverloads
    fun block(blockInMillis: Int = DEFAULT_BLOCK_TIME): Boolean {
        if (!isBlockClick) {
            isBlockClick = true
            Handler().postDelayed({ isBlockClick = false }, blockInMillis.toLong())
            return false
        }
        return true
    }

    companion object {
        const val DEFAULT_BLOCK_TIME = 1000
    }
}