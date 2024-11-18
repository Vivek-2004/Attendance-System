package com.nshm.attendancesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableStateFlow

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
                        else -> NavigationDestination.Scan
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