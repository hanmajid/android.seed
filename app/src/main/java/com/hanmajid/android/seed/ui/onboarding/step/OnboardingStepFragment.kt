package com.hanmajid.android.seed.ui.onboarding.step

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.hanmajid.android.seed.R
import com.hanmajid.android.seed.databinding.FragmentOnboardingStepBinding

private const val ARG_STEP = "step"

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingStepFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingStepFragment : Fragment() {
    private var step: Int? = null

    private lateinit var binding: FragmentOnboardingStepBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            step = it.getInt(ARG_STEP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        step?.apply {
            binding.title.text = when (step) {
                0 -> getString(R.string.onboarding_title_step_0)
                1 -> getString(R.string.onboarding_title_step_1)
                2 -> getString(R.string.onboarding_title_step_2)
                else -> "" // unreachable
            }
            binding.subtitle.text = when (step) {
                0 -> getString(R.string.onboarding_subtitle_step_0)
                1 -> getString(R.string.onboarding_subtitle_step_1)
                2 -> getString(R.string.onboarding_subtitle_step_2)
                else -> "" // unreachable
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param step Step position.
         * @return A new instance of fragment OnboardingStep1Fragment.
         */
        @JvmStatic
        fun newInstance(step: Int) =
            OnboardingStepFragment()
                .apply {
                    arguments = Bundle().apply {
                        putInt(ARG_STEP, step)
                    }
                }
    }
}