package com.example.tinderclone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tinderclone.composable.CardStack
import com.example.tinderclone.ui.theme.TinderCloneTheme

@OptIn(ExperimentalMaterialApi::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            TinderCloneTheme {
                Scaffold(
                    topBar = { TopBar() }
                ) {
                    CardStack(
                        modifier = Modifier,
                        enableButtons = true,
                        users = users,
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = "profile",
                tint = Color.LightGray,
            )
        }

        Image(
            modifier = Modifier.size(70.dp),
            painter = painterResource(id = R.drawable.tinder_logo),
            contentDescription = "logo")

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.Menu,
                contentDescription = "chat",
                tint = Color.LightGray,
            )
        }
    }
}