package vn.example.readingapplication.adapter.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.example.readingapplication.fragment.search.SearchAuthorFragment
import vn.example.readingapplication.fragment.search.SearchBookFragment
import vn.example.readingapplication.fragment.search.SearchCategoriesFragment

class SearchViewPagerAdapter(
    fm: FragmentManager,
    life: Lifecycle
) : FragmentStateAdapter(fm, life) {

    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchBookFragment()
            1 -> SearchAuthorFragment()
            else -> SearchCategoriesFragment()
        }
    }
}
