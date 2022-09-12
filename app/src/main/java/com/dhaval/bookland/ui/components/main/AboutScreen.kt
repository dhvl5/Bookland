package com.dhaval.bookland.ui.components.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dhaval.bookland.R
import com.dhaval.bookland.utils.Constants
import com.google.accompanist.insets.ui.Scaffold

@Composable
fun AboutScreen(context: Context, navController: NavHostController) {
    Scaffold(
        topBar = { AboutTopBar(navController) },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp, 15.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp, 100.dp),
                    ) {
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxSize(),
                            painter = painterResource(id = R.drawable.logo_white),
                            contentDescription = "",
                        )
                    }

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

                    Text(
                        text = "v".plus(context.packageManager.getPackageInfo(context.packageName, 0).versionName),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant,
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(15.dp, 15.dp, 15.dp, 15.dp),
                    text = "Bookland is an android app where you can track all of your book readings. \n\nIt is also completely free and open-source so anyone can contribute to its development. If you like this app then please leave a rating/comment on Google Play. \n\nThank you for using the app!",
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.onSecondary,
                )

                Spacer(modifier = Modifier.padding(0.dp, 15.dp))

                Button(
                    modifier = Modifier
                        .width(230.dp)
                        .align(Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 10.dp,
                        end = 20.dp,
                        bottom = 10.dp
                    ),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(43, 49, 55)),
                    onClick = {
                        val customTabsIntent = CustomTabsIntent.Builder()
                            .setDefaultColorSchemeParams(
                                CustomTabColorSchemeParams.Builder()
                                    .setToolbarColor(Color(0xFF252525).hashCode())
                                    .build()
                            )
                            .build()
                        customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                        customTabsIntent.launchUrl(context, Uri.parse(Constants.GITHUB_URL))
                    },
                ) {
                    Text(
                        text = "Contribute to Bookland",
                        color = Color.White,
                    )
                }

                Text(
                    modifier = Modifier
                        .padding(0.dp, 15.dp, 0.dp, 3.dp)
                        .align(Alignment.CenterHorizontally),
                    text = "Created and maintained by",
                    fontSize = 13.sp,
                    color = MaterialTheme.colors.onSecondary,
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            val customTabsIntent = CustomTabsIntent
                                .Builder()
                                .setDefaultColorSchemeParams(
                                    CustomTabColorSchemeParams
                                        .Builder()
                                        .setToolbarColor(Color(0xFF252525).hashCode())
                                        .build()
                                )
                                .build()
                            customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                            customTabsIntent.launchUrl(context, Uri.parse(Constants.TWITTER_URL))
                        },
                    text = "Dhaval Prajapati",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.onSecondary,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 15.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                val customTabsIntent = CustomTabsIntent.Builder()
                                    .setDefaultColorSchemeParams(
                                        CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(Color(0xFF252525).hashCode())
                                            .build()
                                    )
                                    .build()
                                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                customTabsIntent.launchUrl(context, Uri.parse(Constants.PRIVACY_URL))
                            },
                        text = "Privacy Policy",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        textDecoration = TextDecoration.Underline,
                    )
                    Text(
                        text = "  ·  ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primaryVariant,
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                val customTabsIntent = CustomTabsIntent.Builder()
                                    .setDefaultColorSchemeParams(
                                        CustomTabColorSchemeParams.Builder()
                                            .setToolbarColor(Color(0xFF252525).hashCode())
                                            .build()
                                    )
                                    .build()
                                customTabsIntent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                                customTabsIntent.launchUrl(context, Uri.parse(Constants.TERMS_URL))
                            },
                        text = "Terms & Conditions",
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.primaryVariant,
                        textDecoration = TextDecoration.Underline,
                    )
                }
            }
        }
    }
}

@Composable
fun AboutTopBar(navController: NavHostController) {
    val appBarHorizontalPadding = 4.dp
    val titleIconModifier = Modifier
        .fillMaxHeight()
        .width(72.dp - appBarHorizontalPadding)

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
    ) {
        Box(Modifier.height(32.dp)) {
            Row(titleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(
                    LocalContentAlpha provides ContentAlpha.high,
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        enabled = true,
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                }
            }
            Row(
                Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ProvideTextStyle(value = MaterialTheme.typography.h6) {
                    CompositionLocalProvider(
                        LocalContentAlpha provides ContentAlpha.high,
                    ) {
                        Text(
                            text = "About",
                            color = MaterialTheme.colors.onPrimary,
                            style = TextStyle(
                                fontSize = 30.sp,
                                fontFamily = FontFamily.Cursive,
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}