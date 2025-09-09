package vn.example.readingapplication.utils.extention

import android.app.Activity
import android.content.Context
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun EditText?.hideSoftKeyboardClickOutSide(context: Context?) {
    if (context == null || this == null) {
        return
    }

    // https://stackoverflow.com/a/19828165
    this.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
        if (!hasFocus) {
            val inputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }
}