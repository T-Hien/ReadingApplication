package vn.example.readingapplication.adapter.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.admin.ShowDeleteDialog
import vn.example.readingapplication.activity.admin.account.UpdateAcountActivity
import vn.example.readingapplication.databinding.LayoutAdminAccountUserBinding
import vn.example.readingapplication.model.User

class UserManagementAdapter(private val itemList: List<User>) :
    RecyclerView.Adapter<UserManagementAdapter.CardViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = LayoutAdminAccountUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val user = itemList[position]
        holder.binding.txtName.text = user.name
        val imageUrl:String? = user.image.toString()
        if(user.status == 0){
            holder.binding.txtLock.text = "Mở"
        }
        else if(user.status == 1){
            holder.binding.txtLock.text = "Khóa"
        }
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.img_account_boy)
            .error(R.drawable.img_account_girl)
            .into(holder.binding.imgUser)
//
        holder.binding.btnEdit.setOnClickListener(){
            val intent = Intent(holder.itemView.context, UpdateAcountActivity::class.java)
            intent.putExtra("username",user.username)
            intent.putExtra("role",user.role)
            holder.itemView.context.startActivity(intent)
        }
        holder.binding.btnDelete.setOnClickListener(){
//            showImageSelectionDialog(holder)
            val dialogFragment =
                user.username?.let { it1 ->
                    user.status?.let { it2 -> ShowDeleteDialog.newInstance3(it1,it2) }
                }
            if (dialogFragment != null) {
                dialogFragment.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "UpdateCategoryDialog")
            }

        }

    }
    override fun getItemCount(): Int {
        return itemList.size
    }

    class CardViewHolder(val binding: LayoutAdminAccountUserBinding) : RecyclerView.ViewHolder(binding.root)
}
