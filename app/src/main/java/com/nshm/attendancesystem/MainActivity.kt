package com.nshm.attendancesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

enum class NavigationDestination {
    Scan,
    Register,
    Attendance
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MyApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTopBar(title: String) {
    var isVisible by remember { mutableStateOf(false) }

    // Infinite loop for fading the text in and out
    LaunchedEffect(Unit) {
        while (true) {
            isVisible = true
            delay(5000) // Text stays visible for 5 seconds
            isVisible = false
            delay(3000) // Text stays hidden for 3 seconds
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Static Logo
            Icon(
                painter = painterResource(id = R.drawable.app_logo), // Updated logo with transparency
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp), // Adjust logo size as needed
                tint = MaterialTheme.colorScheme.onPrimaryContainer // Remove tint if not needed
            )

            // Animated Title Text
            AnimatedContent(
                targetState = isVisible,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 3000)) with
                            fadeOut(animationSpec = tween(durationMillis = 3000))
                }
            ) { visible ->
                if (visible) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                } else {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val currentScreen = remember { MutableStateFlow(NavigationDestination.Scan) }

    Scaffold(
        topBar = {
            AnimatedTopBar(title = currentScreen.collectAsState().value.name)
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onTitleChange = {

                    currentScreen.value = when (it) {
                        "Scan" -> NavigationDestination.Scan
                        "Register" -> NavigationDestination.Register
                        "Attendance" -> NavigationDestination.Attendance
                        else -> NavigationDestination.Scan // Default
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = NavigationDestination.Scan.name
        ) {
            composable(NavigationDestination.Scan.name) {
                LaunchedEffect(key1 = Unit) {
                    currentScreen.value = NavigationDestination.Scan
                }
                CameraScreen(navController = navController)
            }
            composable(NavigationDestination.Register.name) {
                LaunchedEffect(key1 = Unit) {
                    currentScreen.value = NavigationDestination.Register
                }
                RegisterScreen()
            }
            composable(NavigationDestination.Attendance.name) {
                LaunchedEffect(key1 = Unit) {
                    currentScreen.value = NavigationDestination.Attendance
                }
                AttendanceScreen()
            }
        }
    }
}