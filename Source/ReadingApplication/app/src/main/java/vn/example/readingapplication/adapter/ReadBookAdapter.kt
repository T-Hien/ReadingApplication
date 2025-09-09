package vn.example.readingapplication.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.example.readingapplication.fragment.home.CategoryFragment
import vn.example.readingapplication.fragment.home.DiscoverFragment
import vn.example.readingapplication.fragment.home.FavoritesFragment
import vn.example.readingapplication.fragment.home.LatestFragment
import vn.example.readingapplication.fragment.readbook.ChapterFragment
import vn.example.readingapplication.fragment.readbook.IntroduceFragment

class ReadBookAdapter (
    fm: FragmentManager,
    life: Lifecycle,
    private val listFragment: List<Fragment>
) : FragmentStateAdapter(fm, life) {

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroduceFragment()
            1 -> ChapterFragment()
            else -> FavoritesFragment()
        }
    }
}
