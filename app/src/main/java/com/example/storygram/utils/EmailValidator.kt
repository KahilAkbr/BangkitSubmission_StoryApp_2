package com.example.storygram.utils

import java.util.regex.Pattern

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    val pattern = Pattern.compile(emailRegex)
    return pattern.matcher(email).matches()
}