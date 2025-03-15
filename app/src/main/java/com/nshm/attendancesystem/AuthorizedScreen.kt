package com.nshm.attendancesystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AuthorizedScreen(
    name: String,
    message: String,
    color: String,
    navController: NavController,
) {
    var bgColor: Int = R.color.white
    var txtColor: Int = R.color.white
    var animationStarted by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationStarted = true
    }

    if (color == "green") {
        bgColor = R.color.bgGreen
        txtColor = R.color.textGreen
    } else if (color == "yellow") {
        bgColor = R.color.bgYellow
        txtColor = R.color.textYellow
    } else if (color == "red") {
        bgColor = R.color.bgRed
        txtColor = R.color.black
    }

    Scaffold(
        containerColor = colorResource(bgColor)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn(tween(500)) + slideInVertically(
                        initialOffsetY = { -50 },
                        animationSpec = tween(500)
                    )
                ) {
                    Text(
                        text = "Welcome to",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(txtColor)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn(tween(800))
                ) {
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.9f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.app_logo_xml),
                                contentDescription = "Logo",
                                modifier = Modifier.size(48.dp),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Image(
                                painter = painterResource(id = R.drawable.codenest),
                                contentDescription = "CodeNest Logo",
                                modifier = Modifier
                                    .width(250.dp)
                                    .height(30.dp),
                                contentScale = ContentScale.Fit
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Image(
                                painter = painterResource(id = R.drawable.slogan),
                                contentDescription = "Slogan",
                                modifier = Modifier
                                    .width(340.dp)
                                    .height(28.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                AnimatedVisibility(
                    visible = animationStarted,
                    enter = fadeIn(tween(1000)) + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = tween(1000)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .padding(horizontal = 16.dp),
                            color = Color.White.copy(alpha = 0.9f),
                            tonalElevation = 4.dp
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(vertical = 20.dp, horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = name,
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(txtColor)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = message,
                                    fontSize = 22.sp,
                                    color = colorResource(txtColor),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        FilledTonalButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White.copy(alpha = 0.7f),
                                contentColor = colorResource(txtColor)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 32.dp)
                        ) {
                            Icon(
                                Icons.Rounded.QrCodeScanner,
                                contentDescription = "Scan QR Code",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Scan Again",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}