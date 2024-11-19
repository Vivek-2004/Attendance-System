package com.nshm.attendancesystem

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedTopBar(title: String) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            isVisible = true
            delay(8000)
            isVisible = false
            delay(1000)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.app_logo_xml),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp)),
                tint = Color.Black
            )
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
                        color = Color.Black
                    )
                } else {
                    Text(
                        text = "",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, onTitleChange: (String) -> Unit) {
    val items = listOf("Scan", "Register", "Attendance")

    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer
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
                        tint = if (currentDestination?.route == screen) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface
                    )
                },
                label = {
                    Text(
                        text = screen,
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