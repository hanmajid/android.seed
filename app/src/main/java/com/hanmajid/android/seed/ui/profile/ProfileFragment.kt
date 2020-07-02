package com.hanmajid.android.seed.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentProfileBinding
import com.hanmajid.android.seed.ui.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupViewModel()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        val rootFragments = setOf(
            R.id.home_fragment,
            R.id.explore_fragment,
            R.id.chat_fragment,
            R.id.profile_fragment
        )

        val adapter = ProfilePagerAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Posts"
                1 -> "Photos"
                else -> "About"
            }
        }.attach()

        // Scroll RecyclerView to top if Home icon is reselected.
        val bottomNav: BottomNavigationView? =
            requireActivity().findViewById(R.id.bottom_nav)
        bottomNav?.setOnNavigationItemReselectedListener {
            binding.nestedScrollView.smoothScrollTo(0, 0, 1000)
        }
    }

    private fun setupViewModel() {
        authViewModel.loggedInUser.observe(viewLifecycleOwner, Observer {
            it?.apply {
                binding.user = this
            }
        })
        authViewModel.loggedInUserAvatarPalette.observe(viewLifecycleOwner, Observer {
            it?.apply {
                val vibrantSwatch = this.darkVibrantSwatch
                binding.banner.setBackgroundColor(
                    vibrantSwatch?.rgb ?: (ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    ))
                )
                binding.name.setTextColor(
                    vibrantSwatch?.bodyTextColor ?: (ContextCompat.getColor(
                        requireContext(),
                        R.color.material_on_primary_emphasis_high_type
                    ))
                )
                binding.username.setTextColor(
                    vibrantSwatch?.titleTextColor ?: (ContextCompat.getColor(
                        requireContext(),
                        R.color.material_on_primary_emphasis_medium
                    ))
                )
            }
        })
    }


    companion object {
        private const val TAG = "ProfileFragment"
    }
}