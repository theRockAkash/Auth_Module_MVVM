package com.skyviewads.foodcourt.ui.auth.loginFragment

/**
 * Data validation state of the login form.
 */
data class LoginFormState (val phoneError: String = "",
                      val passwordError: String = "",
                      val isDataValid: Boolean = false)