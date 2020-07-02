package com.hanmajid.android.seed.ui.profile

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hanmajid.android.seed.ui.chat.ChatFragment

class ProfilePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return ChatFragment()
    }
}