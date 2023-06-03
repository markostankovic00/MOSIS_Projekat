package com.example.mosisprojekat.util

import android.content.Context
import com.example.mosisprojekat.R


fun validateRepeatPasswordTextField(
    repeatPasswordTextState: String,
    passwordToMatchTextState: String,
    context: Context
): Pair<Boolean, String> {
    return if (repeatPasswordTextState.isEmpty())
        Pair(true, context.getString(R.string.error_cant_be_empty))
    else if (repeatPasswordTextState != passwordToMatchTextState) {
        Pair(true, context.getString(R.string.error_passwords_dont_match))
    } else {
        Pair(false, "")
    }
}

fun validateNotEmpty(textState: String, context: Context): Pair<Boolean, String> {
    return if (textState.isEmpty()) {
        Pair(true, context.getString(R.string.error_cant_be_empty))
    } else Pair(false, "")
}