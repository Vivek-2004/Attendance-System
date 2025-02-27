package com.nshm.attendancesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
    val currentDestination = navController.currentBackStackEntryAsState()
    val currentScreenTitle = remember { mutableStateOf("Scan") }

    // Update the title whenever navigation changes
    LaunchedEffect(currentDestination.value?.destination?.route) {
        currentDestination.value?.destination?.route?.let { route ->
            currentScreenTitle.value = route
        }
    }

    Scaffold(
        topBar = {
            AnimatedTopBar(title = currentScreenTitle.value)
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentDestination.value?.destination?.route
            )
        }
    ) { innerPadding ->
        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = NavigationDestination.Scan.name
        ) {
            composable(NavigationDestination.Scan.name) {
                CameraScreen(navController = navController)
            }
            composable(NavigationDestination.Register.name) {
                RegisterScreen()
            }
            composable(NavigationDestination.Attendance.name) {
                AttendanceScreen()
            }
            composable(NavigationDestination.Registered.name) {
                RegisteredStudentsScreen()
            }

        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?
) {
    val items = listOf(
        NavigationDestination.Scan.name,
        NavigationDestination.Register.name,
        NavigationDestination.Attendance.name,
        NavigationDestination.Registered.name
    )

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        items.forEach { screen ->
            val icon = when (screen) {
                NavigationDestination.Scan.name -> Icons.Default.Search
                NavigationDestination.Register.name -> Icons.Default.Create
                NavigationDestination.Attendance.name -> Icons.Default.Person
                NavigationDestination.Registered.name-> Icons.Default.Face
                else -> Icons.Default.Warning
            }

            NavigationBarItem(
                modifier = Modifier.padding(2.dp),
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = screen,
                        modifier = Modifier.size(24.dp),
                        tint = if (currentRoute == screen) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = screen,
                        color = if (currentRoute == screen) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface,
                        style = if (currentRoute == screen) MaterialTheme.typography.bodyLarge
                        else MaterialTheme.typography.bodyMedium
                    )
                },
                selected = currentRoute == screen,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}