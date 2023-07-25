package ru.kovsh.tasku.ui.auth.entities.signInUpStates
import ru.kovsh.tasku.R
import ru.kovsh.tasku.ui.auth.entities.sharedStates.AuthRequestsStates
import ru.kovsh.tasku.ui.auth.entities.sharedStates.ValidityStates


data class AuthStates(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val refreshToken: String = "",
    val isValidEmail: ValidityStates = ValidityStates.None,
    val isValidPassword: ValidityStates = ValidityStates.None,
    val isValidRepeatedPassword: ValidityStates = ValidityStates.None,
    val authScreenState: AuthScreenStates = AuthScreenStates.SignIn,
    val authResult: AuthRequestsStates = AuthRequestsStates.None,
    val buttonTextID: Int = R.string.sign_in_button,
    val bottomButtonDescriptionID: Int = R.string.bottom_button_dscription_sign_in,
    val bottomButtonTextID: Int = R.string.sign_up_button
    )
