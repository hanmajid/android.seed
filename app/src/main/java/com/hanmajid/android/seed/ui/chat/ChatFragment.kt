package com.hanmajid.android.seed.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentChatBinding
import com.hanmajid.android.seed.ui.AuthViewModel
import com.hanmajid.android.seed.ui.NavigationUIFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var binding: FragmentChatBinding
    private lateinit var navController: NavController
    private lateinit var adapter: ChatAdapter

    private var loadJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
        setupViewModel()

//        loadData()
        viewModel.chatLiveData.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                it?.apply {
                    Log.wtf(TAG, "Update data...")
                    adapter.submitData(this)
                }
            }
        })
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ChatAdapter()
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadState ->
                // Only show the list if refresh succeeds.
                binding.recyclerView.isVisible = loadState.refresh is LoadState.NotLoading
                // Show loading spinner during initial load or refresh.
                binding.progressBar.isVisible = loadState.refresh is LoadState.Loading
                // Show the retry state if initial load or refresh fails.
                binding.btnRetry.isVisible = loadState.refresh is LoadState.Error

                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                errorState?.let {
                    Toast.makeText(
                        context,
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // RecyclerView
        binding.recyclerView.adapter = adapter

        // Retry button
        binding.btnRetry.setOnClickListener { adapter.retry() }

        // Scroll RecyclerView to top if Home icon is reselected.
        val bottomNav: BottomNavigationView? =
            requireActivity().findViewById(R.id.bottom_nav)
        bottomNav?.setOnNavigationItemReselectedListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }

    private fun setupViewModel() {
        authViewModel.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                AuthViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    navController.navigate(
                        NavigationUIFragmentDirections.actionNavigationUIFragmentToAuthNavGraph()
                    )
                }
                else -> {
                }
            }
        })
    }

    private fun loadData() {
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            viewModel.refresh().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        const val TAG = "ChatFragment"
    }
}