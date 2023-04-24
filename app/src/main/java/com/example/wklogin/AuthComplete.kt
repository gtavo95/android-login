package com.example.wklogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse


class AuthComplete : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        println("------------------- AuthComplete -------------------")
        println("intent: $intent")
        val resp: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val ex = AuthorizationException.fromIntent(intent)
        if (resp != null) {
            // authorization completed
            println("resp: $resp")
            println("resp.authorizationCode: ${resp.authorizationCode}")

        } else {
            // authorization failed, check ex for more details
            println("ex: $ex")
        }
        // ...
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = "Auth view")
            }
        }
    }

    fun handleAuthorizationResponse(intent: Intent) {
        val authorizationResponse: AuthorizationResponse? = AuthorizationResponse.fromIntent(intent)
        val error = AuthorizationException.fromIntent(intent)


    }
}

