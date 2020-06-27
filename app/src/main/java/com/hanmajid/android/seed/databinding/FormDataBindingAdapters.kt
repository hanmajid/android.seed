package com.hanmajid.android.seed.databinding

import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("touched")
fun touched(textInputEditText: TextInputEditText?, errorField: MutableLiveData<Boolean>) {
    textInputEditText?.apply {
        addTextChangedListener {
            errorField.value = true
        }
    }
}