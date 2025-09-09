package vn.example.readingapplication.adapter.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.example.readingapplication.fragment.home.CategoryFragment
import vn.example.readingapplication.fragment.home.DiscoverFragment
import vn.example.readingapplication.fragment.home.FavoritesFragment
import vn.example.readingapplication.fragment.home.LatestFragment

class HomeViewPagerAdapter(
    fm: FragmentManager,
    life: Lifecycle
) : FragmentStateAdapter(fm, life) {

    override fun getItemCount(): Int {
        return 4
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DiscoverFragment()
            1 -> CategoryFragment()
            2 -> FavoritesFragment()
            else -> LatestFragment()
        }
    }
}
