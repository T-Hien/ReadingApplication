package vn.example.readingapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import vn.example.readingapplication.R
import vn.example.readingapplication.adapter.home.HomeViewPagerAdapter
import vn.example.readingapplication.base.BaseFragment
import vn.example.readingapplication.databinding.FragmentHomeBinding

class HomePageFragment: BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HomeViewPagerAdapter(childFragmentManager, lifecycle)
        binding.vpListToolbarHome.adapter = adapter
        val tabTitles = arrayOf(getString(R.string.title_home_v1),getString(R.string.title_home_v2),getString(R.string.title_home_v3), getString(R.string.title_home_v4))

        TabLayoutMediator(binding.tlListBookHome, binding.vpListToolbarHome) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
        binding.vpListToolbarHome.isUserInputEnabled = false
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}