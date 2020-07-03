package com.hanmajid.android.seed.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupViewModel()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner

        // RecyclerView
        adapter = PostAdapter(findNavController())
        binding.recyclerView.adapter = adapter

        // Retry button
//        binding.btnRetry.setOnClickListener {
//        }

        // Scroll RecyclerView to top if Home icon is reselected.
        val bottomNav: BottomNavigationView? =
            requireActivity().findViewById(R.id.bottom_nav)
        bottomNav?.setOnNavigationItemReselectedListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun setupViewModel() {
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            it?.apply {
                adapter.submitList(this)
            }
        })
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}