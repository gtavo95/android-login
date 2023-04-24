package com.example.wklogin

import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.auth0.android.jwt.JWT
import net.openid.appauth.*
import net.openid.appauth.AuthorizationServiceConfiguration.RetrieveConfigurationCallback
import net.openid.appauth.browser.BrowserAllowList
import net.openid.appauth.browser.VersionedBrowserMatcher
import org.json.JSONException
import java.security.MessageDigest
import java.security.SecureRandom


class MainViewModel(application: Application) : AndroidViewModel(application) {
//class MyViewModel(application: Application) : AndroidViewModel(application) {

    var appJustLaunched by mutableStateOf(true)
    var userIsAuthenticated by mutableStateOf(false)

    private var authState: AuthState = AuthState()
    private var jwt : JWT? = null
    private lateinit var authorizationService : AuthorizationService
    private lateinit var authServiceConfig : AuthorizationServiceConfiguration
    private var app: Application = application



    init {
    // Initialize the ViewModel with any necessary data
        println("1. init auth")
        initAuthServiceConfig()
        println("2. restore state")
        restoreState()
        initAuthService()
    }

    private fun initAuthServiceConfig() {
        authServiceConfig = AuthorizationServiceConfiguration(
            Uri.parse(Constants.URL_AUTHORIZATION),
            Uri.parse(Constants.URL_TOKEN_EXCHANGE),
            null,
            Uri.parse(Constants.URL_LOGOUT))

    }

    private fun initAuthService() {
        val appAuthConfiguration = AppAuthConfiguration.Builder()
            .setBrowserMatcher(
                BrowserAllowList(
                    VersionedBrowserMatcher.CHROME_CUSTOM_TAB,
                    VersionedBrowserMatcher.SAMSUNG_CUSTOM_TAB
                )
            ).build()

        authorizationService = AuthorizationService(
            getApplication(),
            appAuthConfiguration)
    }

    private fun restoreState() {
        val jsonString = app.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(Constants.AUTH_STATE, null)
        if( jsonString != null && !TextUtils.isEmpty(jsonString) ) {
            try {
                authState = AuthState.jsonDeserialize(jsonString)

                if( !TextUtils.isEmpty(authState.idToken) ) {
                    jwt = JWT(authState.idToken!!)
                }

            } catch(_: JSONException) { }
        }
    }


    data class Codes(
        val codeVerifier: String,
        val codeChallenge: String
    )
    private fun createCodeChallenge(): Codes {
        val secureRandom = SecureRandom()
        val bytes = ByteArray(64)
        secureRandom.nextBytes(bytes)

        val encoding = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP
        val codeVerifier = Base64.encodeToString(bytes, encoding)

        val digest = MessageDigest.getInstance(Constants.MESSAGE_DIGEST_ALGORITHM)
        val hash = digest.digest(codeVerifier.toByteArray())
        val codeChallenge = Base64.encodeToString(hash, encoding)
        return Codes(codeVerifier, codeChallenge)
    }






    fun login(): AuthorizationRequest{
        userIsAuthenticated = true
        appJustLaunched = false

        val codes = createCodeChallenge()

        val builder = AuthorizationRequest.Builder(authServiceConfig, Constants.CLIENT_ID, ResponseTypeValues.CODE,
            Uri.parse(Constants.URL_AUTH_REDIRECT)).setCodeVerifier(codes.codeVerifier, codes.codeChallenge, Constants.CODE_VERIFIER_CHALLENGE_METHOD)

        builder.setScopes(Constants.SCOPE_PROFILE,
            Constants.SCOPE_EMAIL,
            Constants.SCOPE_OPENID,
            Constants.SCOPE_DRIVE)

        val authRequest = builder.build()
        println("LOG:: request: $authRequest")
        return authRequest

    }



    fun logout(){
        userIsAuthenticated = false
    }


}