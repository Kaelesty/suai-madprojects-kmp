package com.kaelesty.madprojects.domain.repos.auth

enum class RegisterResult {
    OK, BAD_PASSWORD, BAD_EMAIL, BAD_USERNAME, INTERNET_ERROR, BAD_REQUEST
}