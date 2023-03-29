package com.skyviewads.foodcourt.utils

import com.google.android.material.textfield.TextInputLayout
import com.skyviewads.foodcourt.R


@androidx.databinding.BindingAdapter("setError")
fun TextInputLayout.setError(phoneError:String?){
    if (phoneError != null&&phoneError.isNotBlank()) {
        error = this.context.getString(phoneError.toInt())
    }else error=""
}
