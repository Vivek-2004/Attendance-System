package com.nshm.attendancesystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun AttendanceScreen(attendanceViewModel: AttendanceViewModel = viewModel()) {
    val userList by attendanceViewModel::registeredStudentsList
    var isRefreshing by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            // Logging for debugging
            println("Refreshing data...")
            attendanceViewModel.fetchStudentsList()
            delay(1500) // Simulating network delay
            isRefreshing = false
        }
    }

    // Define department names
    val departmentNames = listOf("CSE", "AIML", "DS")

    // Apply gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
    ) {
        Column {
            // Department Tabs
            TabRow(selectedTabIndex = selectedTab) {
                departmentNames.forEachIndexed { index, department ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(department) }
                    )
                }
            }

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { query -> searchQuery = query },
                placeholder = { Text("Search by College ID") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                trailingIcon = {
                    IconButton(onClick = { /* Handle search action */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )

            // Swipe Refresh Layout
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    // Log refresh action
                    println("Swipe to refresh triggered")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Add a Box for further debugging
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val filteredUsers = userList.filter { user ->
                            val matchesDepartment = when (selectedTab) {
                                0 -> user.department == "CSE"
                                1 -> user.department == "AIML"
                                2 -> user.department == "DS"
                                else -> false
                            }
                            val matchesSearchQuery = if (searchQuery.isBlank()) true
                            else {
                                try {
                                    val idToSearch = searchQuery.toLong()
                                    user.collegeId == idToSearch
                                } catch (_: NumberFormatException) {
                                    false
                                }
                            }
                            matchesDepartment && matchesSearchQuery
                        }

                        if (filteredUsers.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(
                                        "No users found.",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        } else {
                            items(filteredUsers) { user ->
                                UserInfoCard(user)
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun UserInfoCard(user: User) {
    if (user.isPresent) {
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp), // Slight elevation for subtle shadow
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Change to match background theme
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = user.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "College ID: ${user.collegeId}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user.collegeEmail.lowercase(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}
