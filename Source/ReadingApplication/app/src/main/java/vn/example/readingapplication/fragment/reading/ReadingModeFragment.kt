package vn.example.readingapplication.fragment.reading

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import vn.example.readingapplication.R
import vn.example.readingapplication.activity.LoginActivity
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentReadingSettingBinding
import vn.example.readingapplication.model.Setting
import vn.example.readingapplication.model.User
import vn.example.readingapplication.retrofit.ApiService
import vn.example.readingapplication.retrofit.ResultResponse
import vn.example.readingapplication.retrofit.RetrofitClient

class ReadingModeFragment : BaseFragment() {

    private lateinit var binding: FragmentReadingSettingBinding
    private var username = "0"
    private var dataPassListener: OnDataPassListener? = null
    private var font_size:Int =0
    private var settingId:Int = 0
    private var textFont = ""
    private var mode =""
    private var checkLogin:Boolean = false
    private val TAG = "READING_MODE"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    interface OnDataPassListener {
        fun onDataPass(data: Int)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPassListener = context as? OnDataPassListener
    }

    private fun sendData() {
        val data = binding.txtFontSize.text.toString().toInt()?:0
        dataPassListener?.onDataPass(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReadingSettingBinding.inflate(inflater, container, false)
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        if (!username.equals("0")) {
            getSetting()
        }
        else{
            settingId = -1
            textFont = "Roboto"
            mode = "Ban ngày"
            font_size = 15
            binding.txtFontSize.setText(font_size.toString())
        }
        getSpinnerMode()
        getSpinnerFont()
        binding.btnExitMode.setOnClickListener {
            parentFragmentManager.popBackStack()
            if(checkLogin) {
                checkLogin = false
                sendData()
            }
        }
        binding.btnExit.setOnClickListener {
            parentFragmentManager.popBackStack()
            if(checkLogin) {
                checkLogin = false
                sendData()
            }
        }
        binding.btnComplete.setOnClickListener(){
            if(username.equals("0")){
                checkLogin = true
                Toast.makeText(context, getString(R.string.text_please_write_reading_mode), Toast.LENGTH_SHORT).show()
                val intent = Intent(context,LoginActivity::class.java)
                startActivity(intent)
            }
            updateReadingMode()
        }
        binding.btnAddSize.setOnClickListener(){
            context?.let { it1 -> ContextCompat.getColor(it1,R.color.bg_btn_size_v2) }
                ?.let { it2 -> binding.btnAddSize.setBackgroundColor(it2) }
            lifecycleScope.launch {
                delay(100)
                font_size = binding.txtFontSize.text.toString().toInt() + 1
                binding.txtFontSize.setText(font_size.toString())
                context?.let { it1 -> ContextCompat.getColor(it1,R.color.bg_btn_size) }
                    ?.let { it2 -> binding.btnAddSize.setBackgroundColor(it2) }
            }
        }
        binding.btnMinusSize.setOnClickListener(){
            context?.let { it1 -> ContextCompat.getColor(it1,R.color.bg_btn_size_v2) }
                ?.let { it2 -> binding.btnMinusSize.setBackgroundColor(it2) }
            lifecycleScope.launch {
                delay(100)
                font_size = binding.txtFontSize.text.toString().toInt() - 1
                binding.txtFontSize.setText(font_size.toString())
                context?.let { it1 -> ContextCompat.getColor(it1,R.color.bg_btn_size) }
                    ?.let { it2 -> binding.btnMinusSize.setBackgroundColor(it2) }
            }
        }

        binding.btnDefault1.setOnClickListener(){
            binding.txtFont.setSelection(0)
        }
        binding.btnDefault2.setOnClickListener(){
            binding.txtFontSize.setText("15")
        }
        binding.btnDefault3.setOnClickListener(){
            binding.snReadingMode.setSelection(0)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences = context?.getSharedPreferences("SaveAccount", MODE_PRIVATE)
        username = sharedPreferences?.getString("username", "0").toString()
        if (!username.equals("0")) {
            getSetting()
        }
    }
    private fun getSetting() {
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.getSetting(username).enqueue(object : Callback<ResultResponse<Setting>> {
            override fun onResponse(call: Call<ResultResponse<Setting>>, response: Response<ResultResponse<Setting>>) {
                if (response.isSuccessful) {
                    val setting = response.body()?.data
                    textFont = setting?.font.toString()
                    mode = setting?.readingMode.toString()
                    font_size = setting?.font_size!!
                    settingId = setting.id!!
                    updateSpinners()
                    binding.txtFontSize.setText(setting.font_size.toString())
                    Log.d(TAG, "DATA: ${setting}")
                }
            }

            override fun onFailure(call: Call<ResultResponse<Setting>>, t: Throwable) {
                Log.d(TAG, "${t.message}")
            }
        })
    }
    private fun updateReadingMode() {
        val setting =Setting(
            id = settingId,
            user = User(username = "$username"),
            font_size = binding.txtFontSize.text.toString().toInt(),
            font = textFont,
            readingMode = mode
        )
        val apiService = RetrofitClient.getClient().create(ApiService::class.java)
        apiService.createSetting(setting).enqueue(object : Callback<ResultResponse<Setting>> {
            override fun onResponse(
                call: Call<ResultResponse<Setting>>,
                response: Response<ResultResponse<Setting>>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(context,getString(R.string.text_write_setting_successfully),Toast.LENGTH_SHORT).show()
                    dataPassListener?.onDataPass(-10)
                }
            }

            override fun onFailure(call: Call<ResultResponse<Setting>>, t: Throwable) {
                Log.d("ERRORSign: ", "${t.message}")
            }
        })
    }
    private fun updateSpinners() {
        getSpinnerMode()
        getSpinnerFont()
    }

    private fun getSpinnerMode() {
        val items = listOf("Ban ngày", "Ban đêm")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.snReadingMode.adapter = adapter

        binding.snReadingMode.post {
            val position = when (mode) {
                "Ban ngày" -> 0
                "Ban đêm" -> 1
                else -> -1
            }
            if (position >= 0) {
                binding.snReadingMode.setSelection(position)
            }
        }

        binding.snReadingMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                mode = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun getSpinnerFont() {
        val items = listOf(
            "Roboto", "Open Sans", "Lora", "Merriweather", "Playfair Display",
            "Montserrat", "Oswald", "Raleway", "Poppins", "Nunito"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.txtFont.adapter = adapter

        // Đặt giá trị của Spinner dựa trên dữ liệu từ Setting
        binding.txtFont.post {
            val position = when (textFont) {
                "Roboto" -> 0
                "Open Sans" -> 1
                "Lora" -> 2
                "Merriweather" -> 3
                "Playfair Display" -> 4
                "Montserrat" -> 5
                "Oswald" -> 6
                "Raleway" -> 7
                "Poppins" -> 8
                "Nunito" -> 9
                else -> -1
            }
            if (position >= 0) {
                binding.txtFont.setSelection(position)
            }
        }

        binding.txtFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                textFont = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
    override fun onDetach() {
        super.onDetach()
        dataPassListener = null
    }

}
