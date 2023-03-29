package com.skyviewads.foodcourt.ui.auth.signupFragment

/**
 * Data validation state of the login form.
 */
data class SignUpFormState (val userNameError: String = "",
                            val emailError: String = "",
                            val phoneError: String = "",
                            val passwordError: String = "",
                            val confirmPasswordError: String = "",
                            val isDataValid: Boolean = false)