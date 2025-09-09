package vn.example.readingapplication.adapter.library

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import vn.example.readingapplication.fragment.library.LibraryAllBookFragment
import vn.example.readingapplication.fragment.library.LibraryReadBookFragment
import vn.example.readingapplication.fragment.library.LibraryUnreadBookFragment

class LibraryViewPagerAdapter(
    fm: FragmentManager,
    life: Lifecycle
) : FragmentStateAdapter(fm, life) {

    override fun getItemCount(): Int {
        return 3
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LibraryAllBookFragment()
            1 -> LibraryUnreadBookFragment()
            else -> LibraryReadBookFragment()
        }
    }
}
