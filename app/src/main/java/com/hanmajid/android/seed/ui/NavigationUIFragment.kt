package com.hanmajid.android.seed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentNavigationUIBinding

class NavigationUIFragment : Fragment() {

    private val rootFragments = setOf(
        R.id.home_fragment,
        R.id.explore_fragment,
        R.id.chat_fragment,
        R.id.profile_fragment
    )

    private lateinit var binding: FragmentNavigationUIBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNavigationUIBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupNavigation()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setupNavigation() {
        val navController =
            Navigation.findNavController(requireActivity(), R.id.navui_nav_host_fragment)
        val drawerLayout = binding.drawerLayout
        drawerLayout?.apply {
            navController.addOnDestinationChangedListener { _, destination, _ ->
                if (rootFragments.contains(destination.id)) {
                    setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                } else {
                    setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
        val appbarConfiguration = AppBarConfiguration(rootFragments, drawerLayout)

        // Setup Toolbar.
        binding.toolbar.setupWithNavController(navController, appbarConfiguration)

        // Setup Bottom Navigation.
        binding.bottomNav?.apply {
            setupWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                visibility = if (rootFragments.contains(destination.id)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        // Setup Drawer
        binding.navView?.apply {
            setupWithNavController(navController)
        }
    }

    companion object {
        const val TAG = "NavigationUIFragment"
    }
}