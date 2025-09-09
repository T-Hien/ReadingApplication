package vn.example.readingapplication.activity.admin

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.databinding.DailogAdminCategoryDeleteBinding
import vn.example.readingapplication.model.Author
import vn.example.readingapplication.model.Book
import vn.example.readingapplication.model.Category
import vn.example.readingapplication.model.Note
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ShowDeleteDialog : DialogFragment() {

    private lateinit var binding: DailogAdminCategoryDeleteBinding
    companion object {
        const val ID = "ID"
        const val NAME = "NAME"
        const val DESCRIPTION = "DESCRIPTION"
        const val P = "P"
        const val username = "Username"
        const val status = "status"



        fun newInstance(p:Int,id: Int, name: String, description: String): ShowDeleteDialog {
            val fragment = ShowDeleteDialog()
            val args = Bundle()
            args.putInt(P,p)
            args.putInt(ID, id)
            args.putString(NAME, name)
            args.putString(DESCRIPTION, description)
            fragment.arguments = args
            return fragment
        }
        fun newInstance2(p:Int,id: Int, description: String): ShowDeleteDialog {
            val fragment = ShowDeleteDialog()
            val args = Bundle()
            args.putInt(P,p)
            args.putInt(ID, id)
            args.putString(DESCRIPTION, description)
            fragment.arguments = args
            return fragment
        }
        fun newInstance3(id: String,s:Int): ShowDeleteDialog {
            val fragment = ShowDeleteDialog()
            val args = Bundle()
            args.putString(username, id)
            args.putInt(status,s)
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
        val p = arguments?.getInt(P)
        val id = arguments?.getInt(ID)
        val name = arguments?.getString(NAME)
        val description = arguments?.getString(DESCRIPTION)
        val username = arguments?.getString(username)
        val status = arguments?.getInt(status)
        val sharedPreferences =
            context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        val username2 = sharedPreferences?.getString("username", "") ?: ""
        if(username!=null){
            if(status == 1){
                binding.txtContent.text = "Bạn có muốn khóa tài khoản này không?"
                binding.btnDelete2.text = "Khóa"
            }
            else{
                binding.txtContent.text = "Bạn có muốn mở khóa tài khoản này không?"
                binding.btnDelete2.text = "Mở"
            }
        }
        if(p==4){
            binding.txtContent.text =
                if(name.equals("0")){ "Bạn có muốn khóa bình luận này vì lý do: $description không?"}
                else{ "Bạn có muốn mở khóa bình luận này không?"}
            binding.btnDelete2.text = if(name.equals("0")){"Khóa"}
            else{"Mở"}
        }

        binding.btnDelete2.setOnClickListener {
            if(username !=null){
                getInforUser(username)
            }
            if(p==2){
                deleteInfAuthor(id)
                val intent = Intent(context, AuthorManagementActivity::class.java)
                context?.startActivity(intent)
            }
            else if(p==3){
                deleteInfCategory(id, name, description)
                val intent = Intent(context, CategoryManagementActivity::class.java)
                context?.startActivity(intent)
            }
            else if(p==4){
                if (id != null) {
                    if (description != null) {
                        if (username2 != null) {
                            getNote(id,username2,description)
                        }
                    }
                }


            }
        }
        binding.btnCancel2.setOnClickListener(){
            dismiss()
        }
    }
    private fun deleteInfCategory(id:Int?,name:String?,des:String?) {
        val category = Category(
            id = id,
            name = name,
            description = des,
            listBookCategory = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteCategory(category).enqueue(object : Callback<ResultResponse<Category>> {
            override fun onResponse(
                call: Call<ResultResponse<Category>>,
                response: Response<ResultResponse<Category>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireActivity(),
                        "Xóa thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                        // Làm mới Adapter
                        (activity as? AppCompatActivity)?.runOnUiThread {
                        }
                    dismiss()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Xóa thất bại!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Log.d("ERROR_DELETECATEGORY1: ", "${response.message()}")
            }
            override fun onFailure(call: Call<ResultResponse<Category>>, t: Throwable) {
                Toast.makeText(requireActivity(), "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROR_DELETECATEGORY2: ", "${t.message}")
            }
        })
    }

    //*****************GET*****************
    private fun getInforUser(username:String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getUser(username).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(call: Call<ResultResponse<User>>, response: Response<ResultResponse<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()?.data
                    if (users != null) {
                        createAccount(users)
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
            }
        })
    }

    private fun createAccount(user: User) {
        var lock = if(user.status == 0) 1 else 0
        val user = User(
            username = user.username,
            name = user.name,
            role = user.role,
            listLike = emptyList(),
            listReadingProgress = emptyList(),
            email = user.email,
            image = user.image,
            phone = user.phone,
            status = lock,
            listNote = emptyList(),
            password = user.password,
            setting = null,
            listBookmark = emptyList(),
            listSearch = emptyList(),
            listReplies = emptyList()

        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createUser(user).enqueue(object : Callback<ResultResponse<User>> {
            override fun onResponse(
                call: Call<ResultResponse<User>>,
                response: Response<ResultResponse<User>>
            ) {
                if (response.isSuccessful) {
                    if(lock==0){
                        Toast.makeText(requireActivity(), "Tài khoản bị khóa!", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(requireActivity(), "Tài khoản đã được mở khóa!", Toast.LENGTH_LONG).show()
                    }
                    var stop = if(user.role==1) 1 else 0
                    val intent = Intent(context, UserManagementActivity::class.java)
                    intent.putExtra("CURRENT_TAB", stop) // Tab 1 (Bình luận đã khóa)
                    context?.startActivity(intent)
                    dismiss()
                } else {
                    Toast.makeText(
                        context,
                        "Thất bạI!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<User>>, t: Throwable) {
                Toast.makeText(context, "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun getNote(id: Int,username:String, description: String) {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getNoteById(id).enqueue(object : Callback<ResultResponse<Note>> {
            override fun onResponse(
                call: Call<ResultResponse<Note>>,
                response: Response<ResultResponse<Note>>
            ) {
                if (response.isSuccessful) {
                    // Giả sử `response` là đối tượng phản hồi từ API
                    val notes = response.body()?.dataList
                    if (!notes.isNullOrEmpty()) {
                        for (note in notes) {
                                createNote(
                                    note,username,
                                    description,
                                )
                        }
                    } else {
                        Log.d("INFO", "No notes found in response")
                    }

                } else {
                        Log.d("ERROR", "Response data is null")

                }
                }
            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Log.d("ERROR", "API call failed: ${t.message}")
            }
        })
    }

    private fun createNote(note:Note,username:String,description: String) {
        val status1 = if(note.status==0){
            1
        }else{0}
        val user = note.auser?.username?.let {
            User(
                username = it
            )
        }
        val book = Book(
            id = note.abook?.id
        )
        var chap: Int? = null
        if(note.chapternumber!=0){
            chap = note.chapternumber
        }
        val note = Note(
            id = note.id,
            content = note.content,
            chapternumber = chap,
            abook = book,
            auser = user,
            type = note.type,
            createdAt = note.createdAt,
            status = status1,
            description = description,
            bookmark = null,
            listReplies = listOf()
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createNote(note).enqueue(object : Callback<ResultResponse<Note>> {
            override fun onResponse(
                call: Call<ResultResponse<Note>>,
                response: Response<ResultResponse<Note>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    if (resultResponse != null) {
                        if (resultResponse.status == 200) {
                            var stop = 0
                            if(note.status==0){
                                stop = 1
                                Toast.makeText(requireActivity(), "Bình luận đã được mở khóa!", Toast.LENGTH_LONG).show()
                            }
                            else{
                                Toast.makeText(requireActivity(), "Bình luận đã bị khóa!", Toast.LENGTH_LONG).show()
                            }
                            val intent = Intent(context, CommentManagementActivity::class.java)
                            intent.putExtra("CURRENT_TAB", stop) // Tab 1 (Bình luận đã khóa)
                            context?.startActivity(intent)
                            dismiss()

                        } else {
                            Toast.makeText(requireActivity(), resultResponse.message, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Null response body", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<ResultResponse<Note>>, t: Throwable) {
                Toast.makeText(requireActivity(), "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.d("ERROREditAccount: ", "${t.message}")
            }
        })
    }

    private fun deleteInfAuthor(id:Int?) {
        val author = Author(
            id = id,
            name = "",
            birth_date = null,
            death_date = null
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.deleteAuthor2(author).enqueue(object : Callback<ResultResponse<Author>> {
            override fun onResponse(
                call: Call<ResultResponse<Author>>,
                response: Response<ResultResponse<Author>>
            ) {
                if (response.isSuccessful) {
                    val resultResponse = response.body()
                    Toast.makeText(
                        requireActivity(),
                        "Xóa thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                    (activity as? AppCompatActivity)?.runOnUiThread {
                    }
                    dismiss()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Xóa thất bại!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            override fun onFailure(call: Call<ResultResponse<Author>>, t: Throwable) {
                Toast.makeText(requireActivity(), "An error occurred: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
