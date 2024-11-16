package com.nshm.attendancesystem

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(navController: NavController, onTitleChange: (String) -> Unit) {
    val items = listOf("Scan", "Register", "Attendance")

    NavigationBar(
        modifier = Modifier.height(65.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        items.forEach { screen ->
            val icon = when (screen) {
                "Scan" -> Icons.Default.Search
                "Register" -> Icons.Default.Create
                "Attendance" -> Icons.Default.Person
                else -> Icons.Default.Home
            }

            NavigationBarItem(
                modifier = Modifier.padding(2.dp),
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = screen,
                        modifier = Modifier.size(24.dp),
                        tint = if (currentDestination?.route == screen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        screen,
                        color = if (currentDestination?.route == screen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        style = if (currentDestination?.route == screen) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium
                    )
                },
                selected = currentDestination?.route == screen,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                    onTitleChange(screen)
                }
            )
        }
    }
}

@Composable
fun TopAppBar(title: String) {
    Surface(
        tonalElevation = 16.dp,
        shadowElevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp), // Increased height for better appearance
        color = MaterialTheme.colorScheme.background, // Match the background color
        contentColor = MaterialTheme.colorScheme.onBackground // Adjust to suit your design
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(8.dp), // Adjusted padding for better appearance
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 20.sp, // Slightly reduced font size for a better fit
            )
        }
    }
}
