package vn.example.readingapplication.activity.admin.account

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import vn.example.readingapplication.R
import vn.example.readingapplication.databinding.ActivityIntroduceBinding

class IntroduceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityIntroduceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroduceBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_introduce)
    }
}