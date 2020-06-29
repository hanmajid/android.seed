package com.hanmajid.android.seed.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentHomeBinding
import com.hanmajid.android.seed.ui.AuthViewModel
import com.hanmajid.android.seed.ui.NavigationUIFragmentDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    private lateinit var adapter: ChatAdapter

    private var loadJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

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
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(HomeViewModel::class.java)

        loadData()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        adapter = ChatAdapter()
        binding.recyclerView.adapter = adapter
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(decoration)
    }

    private fun loadData() {
        loadJob?.cancel()
        loadJob = lifecycleScope.launch {
            viewModel.refresh().collectLatest {
                adapter.submitData(it)
            }
        }
    }
}