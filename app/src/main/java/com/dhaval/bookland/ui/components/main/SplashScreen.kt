package com.dhaval.bookland.ui.components.main

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dhaval.bookland.R
import com.dhaval.bookland.utils.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        delay(500)
        visible = true
        delay(2000)
        visible = false
        delay(1000)
        navController.popBackStack()
        navController.navigate(Screen.Main.route)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = expandHorizontally { 20 },
            exit = shrinkHorizontally(
                animationSpec = tween(),
                shrinkTowards = Alignment.End,
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier.size(100.dp, 100.dp),
                    painter = painterResource(id = R.drawable.logo_white),
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.padding(0.dp, 10.dp))

                Text(
                    text = stringResource(id = R.string.app_name),
                    color = MaterialTheme.colors.onPrimary,
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily.Cursive,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
        }
    }
}