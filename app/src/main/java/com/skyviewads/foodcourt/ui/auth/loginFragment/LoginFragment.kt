package com.skyviewads.foodcourt.ui.auth.loginFragment

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.skyviewads.foodcourt.databinding.FragmentLoginBinding

import com.skyviewads.foodcourt.R
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.ui.home.MainActivity
import com.skyviewads.foodcourt.utils.Utils

class LoginFragment : Fragment() {

    private val loginViewModel by activityViewModels<LoginViewModel>()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      _binding = FragmentLoginBinding.inflate(inflater, container, false)
      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val phoneEditText = binding.phoneEditText
        val passwordEditText = binding.passEdittext
        setClickListners()

        binding.lifecycleOwner=viewLifecycleOwner

        bindObservers()

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                   phoneEditText.text.toString().trim(),
                    passwordEditText.text.toString().trim()
                )
            }
        }
        loginViewModel.loginFormState.observe(viewLifecycleOwner){
            binding.logInFormData=it
        }
        phoneEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)

        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateAndSubmit(phoneEditText, passwordEditText)
            }
            false
        }
    }

    private fun bindObservers() {
        loginViewModel.logInResponse.observe(viewLifecycleOwner) {
            showViews()
            it.let {
                when (it) {
                    is DataHelper.Loading -> { hideViews() }
                    is DataHelper.Error -> {
                        Utils.toasty(requireContext(), it.msg)
                    }
                    is DataHelper.Success -> {
                        it.data?.let { it1 -> Utils.toasty(requireContext(), it1) }
                        startActivity(Intent(requireActivity(),MainActivity::class.java))
                        requireActivity().finishAffinity()
                    }
                }
            }
        }
    }

    private fun showViews() {
        binding.loading.visibility = View.GONE
        binding.body.visibility = View.VISIBLE
        binding.signupButton.isEnabled = true
        binding.guestButton.isEnabled = true
        binding.loginButton.isEnabled = true
        binding.forgetPassButton.isEnabled = true
    }

    private fun validateAndSubmit(
        phoneEditText: TextInputEditText,
        passwordEditText: TextInputEditText
    ) {
        val phone = phoneEditText.text.toString().trim()
        val pass = passwordEditText.text.toString().trim()
        if (loginViewModel.isUserPhoneValid(phone) && loginViewModel.isPasswordValid(pass))
        {
            loginViewModel.userLogIn(phone, pass)
        }
    }

    private fun hideViews() {
        binding.body.visibility = View.INVISIBLE
        binding.loading.visibility = View.VISIBLE
        binding.signupButton.isEnabled = false
        binding.guestButton.isEnabled = false
        binding.loginButton.isEnabled = false
        binding.forgetPassButton.isEnabled = false
    }

    private fun setClickListners() {
        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        binding.forgetPassButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPassFragment)
        }
        binding.loginButton.setOnClickListener {
            validateAndSubmit(binding.phoneEditText, binding.passEdittext)
        }
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}