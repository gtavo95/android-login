package com.example.wklogin

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.wklogin.ui.theme.WkLoginTheme
import net.openid.appauth.AuthorizationService


class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            WkLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainView(mainViewModel)
                }
            }
        }
    }
}

@SuppressLint("UnspecifiedImmutableFlag")
@Composable
fun MainView(
    viewModel: MainViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        val title = if (viewModel.userIsAuthenticated) {
            stringResource(R.string.logged_in_title)
        } else {
            if (viewModel.appJustLaunched) {
                stringResource(R.string.initial_title)
            } else {
                stringResource(R.string.logged_out_title)
            }
        }

        Title(text= title)

        if (viewModel.userIsAuthenticated){
            UserInfoRow(label = stringResource(id = R.string.name_label), value = "Name goes here")
            UserInfoRow(label = stringResource(id = R.string.email_label), value = "Email goes here")

            UserPicture(
                url = "https://images.ctfassets.net/23aumh6u8s0i/5hHkO5DxWMPxDjc2QZLXYf/403128092dedc8eb3395314b1d3545ad/icon-user.png",
                description = "Description goes here",
            )
        }

        val buttonText: String
        val onClickAction: ()-> Unit



        if (viewModel.userIsAuthenticated){
            buttonText = stringResource(R.string.log_out_button)
            onClickAction = {viewModel.logout()}
        } else {
            buttonText = stringResource(R.string.log_in_button)
            onClickAction = {
                val authRequest = viewModel.login()

                val authService = AuthorizationService(context)

                authService.performAuthorizationRequest(
                    authRequest,
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, AuthComplete::class.java),
                        PendingIntent.FLAG_MUTABLE
                    ),
                    PendingIntent.getActivity(
                        context,
                        0,
                        Intent(context, CancelAuth::class.java),
                        PendingIntent.FLAG_IMMUTABLE
                    )
                )

            }

        }


        LogButton(text= buttonText, onClick = onClickAction)
    }

}



@Composable
fun Title(text: String) {
    Text(text = text, style = TextStyle(
        fontSize = 30.sp,
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.W800,
    ))
}

@Composable
fun UserInfoRow(label: String, value: String) {
    Row{
        Text(
            text = label,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        )
        Spacer(
            modifier = Modifier.width(10.dp),
        )
        Text(
            text = value,
            style = TextStyle(
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
            )
        )
    }
}

@Composable
fun UserPicture(
    // 1
    url: String,
    description: String,
) {
    Column(
        // 2
        modifier = Modifier
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            // 3
            painter = rememberAsyncImagePainter(url),
            contentDescription = description,
            modifier = Modifier
                .fillMaxSize(0.5f),
        )
    }
}

@Composable
fun LogButton(text: String, onClick: ()-> Unit){
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .width(200.dp)
                .height(50.dp)
        ) {
            Text(text = text, fontSize= 20.sp)
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    WkLoginTheme {
//        MainView()
//    }
//}

