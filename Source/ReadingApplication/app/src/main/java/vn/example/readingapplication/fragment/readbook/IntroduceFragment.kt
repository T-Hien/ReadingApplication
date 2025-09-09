package vn.example.readingapplication.fragment.readbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import vn.example.readingapplication.databinding.FragmentBookIntroduceBinding

class IntroduceFragment : Fragment() {

    private lateinit var binding: FragmentBookIntroduceBinding

    companion object {
        fun newInstance(data: String): IntroduceFragment {
            val fragment = IntroduceFragment()
            val args = Bundle()
            args.putString("KEY_DATA", data)
            fragment.arguments = args
            return fragment
        }
    }

    private var data: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getString("KEY_DATA")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookIntroduceBinding.inflate(inflater, container, false)
        binding.txtIntroduceBook.text = data.toString()
        return binding.root
    }
}
