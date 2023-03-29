package com.skyviewads.foodcourt.ui.auth.signupFragment

import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.skyviewads.foodcourt.databinding.FragmentSignupBinding
import com.skyviewads.foodcourt.network.DataHelper
import com.skyviewads.foodcourt.ui.home.MainActivity
import com.skyviewads.foodcourt.utils.Utils

class SignupFragment : Fragment() {

    private val viewModel by activityViewModels<SignupViewModel>()
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setClickListners()
        setFormTextChangeListners()
        bindObservers()
    }

    private fun bindObservers() {
        viewModel.signUpFormState.observe(viewLifecycleOwner) {
            it.let { binding.signUpFormData = it }
        }
        viewModel.singUpResponse.observe(viewLifecycleOwner){
            showViews()
            when (it) {
                is DataHelper.Loading -> {
                    hideViews()
                }
                is DataHelper.Error -> {
                    Utils.toasty(requireContext(), it.msg)
                }
                is DataHelper.Success -> {
                    Utils.toasty(requireContext(), it.data)
                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                    requireActivity().finishAffinity()
                }
            }
        }
    }

    private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            viewModel.signUpDataChanged(
                binding.nameEditText.text.toString().trim(),
                binding.emailEditText.text.toString().trim(),
                binding.phoneEditText.text.toString().trim(),
                binding.passEdittext.text.toString().trim(),
                binding.confirmPassEdittext.text.toString().trim()
            )
        }
    }

    private fun setFormTextChangeListners() {

        binding.nameEditText.addTextChangedListener(afterTextChangedListener)
        binding.emailEditText.addTextChangedListener(afterTextChangedListener)
        binding.phoneEditText.addTextChangedListener(afterTextChangedListener)
        binding.passEdittext.addTextChangedListener(afterTextChangedListener)
        binding.confirmPassEdittext.addTextChangedListener(afterTextChangedListener)

        binding.confirmPassEdittext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                validateAndSubmit()
            false
        }
    }

    private fun setClickListners() {
        binding.loginButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.signUpButton.setOnClickListener {
                validateAndSubmit()
        }
    }

    private fun validateAndSubmit() {
        if (!binding.acceptConditions.isChecked)
        {
           Utils.toasty(requireContext(),"Accept T&C and Privacy Policy")
            return
        }
        val phone = binding.phoneEditText.text.toString().trim()
        val pass = binding.passEdittext.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val confirmPassword = binding.confirmPassEdittext.text.toString().trim()
        if (viewModel.isPhoneValid(phone) && viewModel.isPasswordValid(pass) && viewModel.isEmailValid(email)
            && viewModel.isUserNameValid(name)&& viewModel.isConfirmPasswordValid(pass,confirmPassword)
        ) {
            viewModel.userRegister(name, email, phone, pass)
        }
    }

    private fun hideViews() {
        binding.body.visibility = View.INVISIBLE
        binding.loading.visibility = View.VISIBLE
        binding.signUpButton.isEnabled = false
        binding.loginButton.isEnabled = false
    }
    private fun showViews() {
        binding.body.visibility = View.VISIBLE
        binding.loading.visibility = View.GONE
        binding.signUpButton.isEnabled = true
        binding.loginButton.isEnabled = true
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}