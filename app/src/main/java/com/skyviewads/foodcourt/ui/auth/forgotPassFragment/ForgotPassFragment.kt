package com.skyviewads.foodcourt.ui.auth.forgotPassFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.skyviewads.foodcourt.R
import com.skyviewads.foodcourt.databinding.FragmentForgotPassBinding
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.utils.Utils

class ForgotPassFragment : Fragment() {
    private val viewModel: ForgotPassViewModel by activityViewModels()
    private var _binding: FragmentForgotPassBinding? = null
    private val binding: FragmentForgotPassBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPassBinding.inflate(layoutInflater);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.resetButton.setOnClickListener {
            validateAndSubmit()
        }
        binding.doneButton.setOnClickListener {
             submitOtpAndPassword()
        }
        binding.phoneEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                validateAndSubmit()
            false
        }
        binding.confirmPassEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                submitOtpAndPassword()
            false
        }
        binding.phoneEditText.addTextChangedListener(afterTextChangedListener)
        bindObservers()

    }

    private fun submitOtpAndPassword() {
        val pass=binding.passEdittext.text.toString().trim()
        val cPass=binding.confirmPassEdittext.text.toString().trim()
        val otp=binding.otpEdittext.text.toString().trim()
        if (viewModel.isConfirmPasswordValid(pass,cPass)&&otp.isNotEmpty()){

        }else Utils.toasty(requireContext(),"Enter both Otp and Password")

    }

    private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            binding.phoneInputLayout.error = ""
        }
    }

    private fun bindObservers() {
        viewModel.forgetPassResponse.observe(viewLifecycleOwner) {
            when (it) {
                is DataHelper.Loading -> {
                    hideViews()
                }
                is DataHelper.Error -> {
                    showViews()
                    Utils.toasty(requireContext(), it.msg)
                }
                is DataHelper.Success -> {
                    showOtpViews()
                    Utils.toasty(requireContext(), it.data)
                }
            }
        }
    }

    private fun validateAndSubmit() {
        val phone = binding.phoneEditText.text.toString().trim();
        if (phone.isNotEmpty() && phone.length > 8) {
            viewModel.userForgotPassword(phone)
        } else binding.phoneInputLayout.error = getString(R.string.invalid_phone)

    }

    private fun hideViews() {
        binding.loading.visibility = View.VISIBLE
        binding.body.visibility = View.INVISIBLE
        binding.resetButton.isEnabled = false
    }

    private fun showViews() {
        binding.loading.visibility = View.GONE
        binding.body.visibility = View.VISIBLE
        binding.resetButton.isEnabled = true
    }

    private fun showOtpViews() {
        binding.loading.visibility = View.GONE
        binding.body.visibility = View.GONE
        binding.otpLayout.visibility = View.VISIBLE
    }
}