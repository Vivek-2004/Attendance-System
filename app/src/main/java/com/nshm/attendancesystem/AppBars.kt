package com.nshm.attendancesystem

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(navController: NavController, onTitleChange: (String) -> Unit) {
    val items = listOf("Scan", "Register", "Scanned")
    NavigationBar(modifier = Modifier.height(65.dp)) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector =  Icons.Default.Home, contentDescription = screen) },
                label = { Text(screen) },
                selected = false,
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
    Column (
        modifier = Modifier.fillMaxWidth().height(40.dp).padding(top = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontSize = 25.sp
        )
        Spacer(Modifier.height(15.dp))
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
    }
}