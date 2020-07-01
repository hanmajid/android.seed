package com.hanmajid.android.seed.ui.onboarding

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class OnboardingViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val pagerPosition = MutableLiveData<Int>(0)

    val maxPagerPosition = 2

    fun changePagerPosition(newPosition: Int) {
        pagerPosition.value = newPosition
    }
}