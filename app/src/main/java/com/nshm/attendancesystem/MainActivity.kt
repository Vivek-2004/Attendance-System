package com.nshm.attendancesystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceSystemTheme {
                val attendanceViewModel: AttendanceViewModel = viewModel()
                MyApp(attendanceViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(attendanceViewModel: AttendanceViewModel) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState()
    val currentScreenTitle = remember { mutableStateOf("Scan") }
    var isLoading by remember { mutableStateOf(false) }

    // Update the title whenever navigation changes
    LaunchedEffect(currentDestination.value?.destination?.route) {
        currentDestination.value?.destination?.route?.let { route ->
            currentScreenTitle.value = route.substringBefore("/")
            isLoading = true
            kotlinx.coroutines.delay(800)
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = currentScreenTitle.value,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
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
                CameraScreen(
                    navController = navController,
                    attendanceViewModel = attendanceViewModel
                )
            }
            composable(NavigationDestination.Register.name) {
                RegisterScreen(attendanceViewModel = attendanceViewModel)
            }
            composable(NavigationDestination.Attendance.name) {
                AttendanceScreen(attendanceViewModel = attendanceViewModel)
            }
            composable(NavigationDestination.Registered.name) {
                RegisteredStudentsScreen(
                    navController = navController,
                    attendanceViewModel = attendanceViewModel
                )
            }
            composable(
                route = "${NavigationDestination.Profile.name}/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                ProfileScreen(
                    userId = userId,
                    navController = navController,
                    attendanceViewModel = attendanceViewModel
                )
            }
            // Add this new composable for the AuthorizedScreen
            composable("${NavigationDestination.Authorized.name}/{name}/{message}/{color}") { backStackEntry ->
                // Extract and decode arguments if needed
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val message = backStackEntry.arguments?.getString("message") ?: ""
                val color = backStackEntry.arguments?.getString("color") ?: ""

                AuthorizedScreen(
                    name = name,
                    message = message,
                    color = color,
                    navController = navController
                )
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
        NavigationItem(NavigationDestination.Scan.name, Icons.Default.Search, "Scan QR"),
        NavigationItem(NavigationDestination.Register.name, Icons.Default.Create, "Register"),
        NavigationItem(NavigationDestination.Attendance.name, Icons.Default.Person, "Attendance"),
        NavigationItem(NavigationDestination.Registered.name, Icons.Default.Face, "Students")
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        NavigationBar(
            modifier = Modifier.height(90.dp),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    modifier = Modifier.padding(2.dp),
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp),
                            tint = if (currentRoute == item.route) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            color = if (currentRoute == item.route) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (currentRoute == item.route) FontWeight.Bold else FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    selected = currentRoute == item.route,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AttendanceSystemTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6750A4),
            onPrimary = Color.White,
            primaryContainer = Color(0xFFEADDFF),
            onPrimaryContainer = Color(0xFF21005E),
            secondary = Color(0xFF625B71),
            onSecondary = Color.White,
            secondaryContainer = Color(0xFFE8DEF8),
            onSecondaryContainer = Color(0xFF1E192B),
            tertiary = Color(0xFF7D5260),
            onTertiary = Color.White,
            tertiaryContainer = Color(0xFFFFD8E4),
            onTertiaryContainer = Color(0xFF370B1E),
            background = Color(0xFFF6EDFF),
            onBackground = Color(0xFF1C1B1F),
            surface = Color.White,
            onSurface = Color(0xFF1C1B1F),
            surfaceVariant = Color(0xFFE7E0EB),
            onSurfaceVariant = Color(0xFF49454E)
        ),
        content = content
    )
}