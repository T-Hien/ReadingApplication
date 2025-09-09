package vn.example.readingapplication.activity.admin.book

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.activity.admin.BookManagementActivity
import vn.example.readingapplication.databinding.DailogAdminCategoryDeleteBinding
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ShowDeleteBookDialog : DialogFragment() {

    private lateinit var binding: DailogAdminCategoryDeleteBinding
    private var bookId:Int = 0
    private lateinit var books:Book

    companion object {
        const val ID = "ID"
        const val ACTIVE = "Active"
        fun newInstance(id: Int,active:Int): ShowDeleteBookDialog {
            val fragment = ShowDeleteBookDialog()
            val args = Bundle()
            args.putInt(ID, id)
            args.putInt(ACTIVE,active)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DailogAdminCategoryDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt(ID)

        val active = arguments?.getInt(ACTIVE)
        if (id != null) {
            bookId = id
        }
        if (id != null) {
            getInfor(id)
        }
        binding.txtContent.text = if (active == 0) {
            "Bạn có thật muốn khóa cuốn sách này?"
        } else {
            "Bạn có thật muốn mở khóa cuốn sách này?"
        }
        binding.btnDelete2.text =if (active == 0) "Khóa" else "Mở"

        binding.btnDelete2.setOnClickListener {
            updateBook(books)
            Thread.sleep(1000)
            val intent = Intent(context, BookManagementActivity::class.java)
            context?.startActivity(intent)
        }
        binding.btnCancel2.setOnClickListener(){
            dismiss()
        }



    }

    private fun getInfor(bookId: Int) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getReadBook(bookId).enqueue(object : Callback<ResultResponse<Book>> {
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    val fetchedBook = response.body()?.dataList?.firstOrNull()
                    fetchedBook?.let { fetchedBook ->
                        // Gán fetchedBook vào biến book của lớp
                        books = fetchedBook
                    }
                } else {
                    Log.d("ERROR", "Response không thành công!")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun updateBook(books: Book) {
        val bookActive = if (books.active == 0) 1 else 0
        val book = Book(
            id = bookId,
            title = books.title,
            createdAt = books.createdAt,
            description = books.description,
            cover_image = books.cover_image,
            type_file = books.type_file,
            status = books.status,
            active = bookActive,
            listReadingProgress = emptyList(),
            favorite = null,
            listNotification = emptyList(),
            listDetailAuthor = emptyList(),
            listBookCategory = emptyList(),
            listChapter = emptyList(),
            listNote = emptyList(),
            listBookmark = emptyList(),
            listLike = emptyList()
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createBook(book).enqueue(object : Callback<ResultResponse<Book>> {
            override fun onResponse(
                call: Call<ResultResponse<Book>>,
                response: Response<ResultResponse<Book>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null && resultResponse.status == 200) {
                        var stop = 0
                        if(books.active==0){
                            stop = 0
                            Toast.makeText(context, "Sách ${books.title} đã bị khóa!", Toast.LENGTH_LONG).show()
                        }
                        else{
                            stop = 1
                            Toast.makeText(context, "Sách ${books.title} đã được mở khóa!", Toast.LENGTH_LONG).show()
                        }

                        val intent = Intent(context, BookManagementActivity::class.java)
                        intent.putExtra("CURRENT_TAB", stop) // Tab 1 (Bình luận đã khóa)
                        context?.startActivity(intent)
                        dismiss()
                    } else {
                        Toast.makeText(context, "Không thành công!", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, "Không thành công!", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResultResponse<Book>>, t: Throwable) {
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
}
