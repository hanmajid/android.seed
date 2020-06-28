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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NavigationUIFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NavigationUIFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val rootFragments = setOf(
        R.id.home_fragment,
        R.id.about_fragment
    )

    private lateinit var binding: FragmentNavigationUIBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NavigationUIFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NavigationUIFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}