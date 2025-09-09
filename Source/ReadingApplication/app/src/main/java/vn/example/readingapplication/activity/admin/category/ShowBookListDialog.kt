package vn.example.readingapplication.activity.admin.category

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ShowBookListDialog : DialogFragment() {
    private lateinit var bookList: String

    companion object {
        fun newInstance(bookList: String): ShowBookListDialog {
            val dialog = ShowBookListDialog()
            val args = Bundle()
            args.putString("BOOK_LIST", bookList)
            dialog.arguments = args
            return dialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookList = arguments?.getString("BOOK_LIST") ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle("Danh sách sách của thể loại:")
            .setMessage(bookList)
            .setPositiveButton("Đóng") { dialog, _ -> dialog.dismiss() }
            .create()
    }
}
