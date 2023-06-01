package com.example.mosisprojekat.util

import android.content.Context
import com.example.mosisprojekat.R

fun validatePasswordTextField(passwordTextState: String, context: Context): Pair<Boolean, String> {
    return if (passwordTextState.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else Pair(false, "")
}

fun validateRepeatPasswordTextField(
    repeatPasswordTextState: String,
    passwordToMatchTextState: String,
    context: Context
): Pair<Boolean, String> {
    return if (repeatPasswordTextState != passwordToMatchTextState) {
        Pair(true, context.getString(R.string.error_passwords_dont_match))
    } else {
        Pair(false, "")
    }
}

fun validateUsernameTextField(usernameTextState: String, context: Context): Pair<Boolean, String> {
    return if (usernameTextState.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else Pair(false, "")
}