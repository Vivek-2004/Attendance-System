package com.nshm.attendancesystem

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun AttendanceScreen(attendanceViewModel: AttendanceViewModel = viewModel()) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val userList by attendanceViewModel::registeredStudentsList
    var isRefreshing by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedDepartment by remember { mutableStateOf("ALL") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val departmentNames = listOf("ALL", "CSE", "AIML", "DS", "ECE", "BCA", "MCA")

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            Toast.makeText(context, "Refreshing Data", Toast.LENGTH_SHORT).show()
            attendanceViewModel.fetchStudentsList()
            delay(1200)
            isRefreshing = false
        }
    }

    fun searchId(id: String) {

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD), Color(0xFFBBDEFB)
                    )
                )
            )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query -> searchQuery = query },
                    placeholder = { Text("Search by College ID") },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide()
                    }),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            searchId(searchQuery)
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.width(4.dp))

                Box {
                    IconButton(onClick = { isDropdownExpanded = true }) {
                        Icon(
                            painter = painterResource(R.drawable.filter),
                            contentDescription = "Filter"
                        )
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        departmentNames.forEach { department ->
                            DropdownMenuItem(
                                text = { Text(department) },
                                onClick = {
                                    selectedDepartment = department
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = { isRefreshing = true }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                val filteredUsers = userList.filter { user ->
                    val matchesDepartment =
                        selectedDepartment == "ALL" || user.department == selectedDepartment
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
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
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

@Composable
fun UserInfoCard(user: User) {
    if (user.isPresent) {
        Card(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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