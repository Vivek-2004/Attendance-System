package com.nshm.attendancesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

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
    val title = remember { mutableStateOf("Scan") }
    Scaffold(
        topBar = { TopAppBar(title = title.value) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onTitleChange = { title.value = it }
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = "Scan"
        ) {
            composable("Scan") {
                title.value = "Scan"
                CameraScreen(navController = navController)
            }
            composable("Register") {
                title.value = "Register"
                RegisterScreen()
            }
            composable("Attendance") {
                title.value = "Scanned Ids"
                AttendanceScreen()
            }
        }
    }
}