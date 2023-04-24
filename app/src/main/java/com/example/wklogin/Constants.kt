package com.example.wklogin


class Constants {
    companion object {
        const val SHARED_PREFERENCES_NAME = "AUTH_STATE_PREFERENCE"
        const val AUTH_STATE = "AUTH_STATE"

        val SCOPE_PROFILE = "profile"
        val SCOPE_EMAIL = "email"
        val SCOPE_OPENID = "openid"
        val SCOPE_DRIVE = "https://www.googleapis.com/auth/drive"

        val DATA_PICTURE = "picture"
        val DATA_FIRST_NAME = "given_name"
        val DATA_LAST_NAME = "family_name"
        val DATA_EMAIL = "email"
        val CLIENT_ID = "nulifespanAndroid"
        val CODE_VERIFIER_CHALLENGE_METHOD = "S256"
        val MESSAGE_DIGEST_ALGORITHM = "SHA-256"

        const val URL_AUTHORIZATION = "https://backtest.authworkinglive.com/auth"
        const val URL_TOKEN_EXCHANGE = "https://backtest.authworkinglive.com/token"
        val URL_AUTH_REDIRECT = "https://backtest.authworkinglive.com/providers/app/nulifespanAndroid"
        val URL_API_CALL = "https://backtest.authworkinglive.com/me"
        const val URL_LOGOUT = "https://backtest.authworkinglive.com/session/end"

        val URL_LOGOUT_REDIRECT = "com.ptruiz.authtest:/logout"
    }
}
