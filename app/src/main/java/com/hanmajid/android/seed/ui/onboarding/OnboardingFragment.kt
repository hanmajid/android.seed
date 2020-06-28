package com.hanmajid.android.seed.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.hanmajid.android.seed.databinding.FragmentOnboardingBinding
import com.hanmajid.android.seed.ui.AuthViewModel
import com.hanmajid.android.seed.ui.onboarding.step.OnboardingStepFragment

class OnboardingFragment : Fragment() {

    private val viewModel: OnboardingViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()

        authViewModel.isFinishedOnboarding.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(
                    OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment()
                )
            }
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        // Setup ViewPager2.
        val pagerAdapter =
            OnboardingStepAdapter(
                this
            )
        binding.pager.apply {
            adapter = pagerAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.changePagerPosition(position)
                }
            })
        }
        binding.btnNext.setOnClickListener {
            val currentPosition = viewModel.pagerPosition.value!!
            if (currentPosition < viewModel.maxPagerPosition) {
                binding.pager.setCurrentItem(currentPosition + 1, true)
            } else {
                authViewModel.finishOnboarding()
            }
        }
        binding.btnPrev.setOnClickListener {
            val currentPosition = viewModel.pagerPosition.value!!
            binding.pager.setCurrentItem(currentPosition - 1, true)
        }
    }

    class OnboardingStepAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return OnboardingStepFragment.newInstance(position)
        }

    }

    companion object {
        val TAG = "OnboardingFragment"
    }
}