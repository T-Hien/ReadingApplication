package vn.example.readingapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import vn.example.readingapplication.R
import vn.example.readingapplication.adapter.library.LibraryViewPagerAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentLibraryBinding

class LibraryFragment: BaseFragment() {

    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = LibraryViewPagerAdapter(childFragmentManager, lifecycle)
        binding.vpListLibrary.adapter = adapter

       val tabTitles = arrayOf(getString(R.string.title_library_all), getString(R.string.title_library_unread_read), getString(R.string.title_library_read))
 //       val tabTitles = arrayOf(getString(R.string.title_library_all))
        binding.tlListLibrary.visibility = View.GONE
        TabLayoutMediator(binding.tlListLibrary, binding.vpListLibrary) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}