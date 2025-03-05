package com.nshm.attendancesystem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileScreen(
    userId: String,
    navController: NavController,
    attendanceViewModel: AttendanceViewModel
) {

    val user = remember(userId) {
        attendanceViewModel.registeredStudentsList.find { it._id == userId }
    }

    var isEditing by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    // Editable fields
    var name by remember { mutableStateOf(user?.name ?: "") }
    var year by remember { mutableStateOf(user?.year ?: "") }
    var department by remember { mutableStateOf(user?.department ?: "") }
    var contactNumber by remember { mutableStateOf(user?.contactNumber?.toString() ?: "") }
    var whatsappNumber by remember { mutableStateOf(user?.whatsappNumber?.toString() ?: "") }

    // Non-editable fields
    val collegeId = user?.collegeId?.toString() ?: ""
    val collegeEmail = user?.collegeEmail ?: ""
    val isPresent = user?.isPresent ?: false
    val isSeminarAttendee = user?.isSeminarAttendee ?: false

    // For update status
    var isUpdating by remember { mutableStateOf(false) }
    var updateMessage by remember { mutableStateOf("") }
    var updateSuccess by remember { mutableStateOf(false) }

    // Years and department options
    val yearOptions = listOf("1st", "2nd", "3rd", "4th")
    val departmentOptions = listOf("CSE", "AIML", "DS", "ECE", "BCA", "MCA", "BBA", "ME", "Civil")

    LaunchedEffect(key1 = updateSuccess) {
        if (updateSuccess) {
            // Refresh the student list
            attendanceViewModel.fetchStudentsList()
            kotlinx.coroutines.delay(2000)
            updateSuccess = false
            updateMessage = ""
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    } else {
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                showConfirmation = true
                            }
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Save")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            user?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile header with avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.first().toString().uppercase(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name field
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // College ID (non-editable)
                    OutlinedTextField(
                        value = collegeId,
                        onValueChange = { },
                        label = { Text("College ID") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // College Email (non-editable)
                    OutlinedTextField(
                        value = collegeEmail,
                        onValueChange = { },
                        label = { Text("College Email") },
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Year dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (isEditing) {
                            ExposedDropdownMenuBox(
                                expanded = false,
                                onExpandedChange = { },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = year,
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Year") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = false,
                                    onDismissRequest = { }
                                ) {
                                    yearOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = { year = option }
                                        )
                                    }
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = year,
                                onValueChange = { },
                                label = { Text("Year") },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Department dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (isEditing) {
                            ExposedDropdownMenuBox(
                                expanded = false,
                                onExpandedChange = { },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = department,
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Department") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                                    )
                                )

                                ExposedDropdownMenu(
                                    expanded = false,
                                    onDismissRequest = { }
                                ) {
                                    departmentOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(option) },
                                            onClick = { department = option }
                                        )
                                    }
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = department,
                                onValueChange = { },
                                label = { Text("Department") },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Contact Number
                    OutlinedTextField(
                        value = contactNumber,
                        onValueChange = { if (it.all { char -> char.isDigit() }) contactNumber = it },
                        label = { Text("Contact Number") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Phone,
                                contentDescription = "Phone"
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // WhatsApp Number
                    OutlinedTextField(
                        value = whatsappNumber,
                        onValueChange = { if (it.all { char -> char.isDigit() }) whatsappNumber = it },
                        label = { Text("WhatsApp Number") },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                painter = androidx.compose.ui.res.painterResource(id = R.drawable.app_logo_xml),
                                contentDescription = "WhatsApp",
                                tint = Color.Green
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Attendance Status
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isPresent)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Attendance Status",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = if (isPresent) "Present" else "Absent",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isPresent)
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                else
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    // Seminar Attendance Status
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSeminarAttendee)
                                MaterialTheme.colorScheme.tertiaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Seminar Attendance",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = if (isSeminarAttendee) "Attended" else "Not Attended",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = if (isSeminarAttendee)
                                    MaterialTheme.colorScheme.onTertiaryContainer
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Update Status Message
                    AnimatedVisibility(visible = updateMessage.isNotEmpty()) {
                        Text(
                            text = updateMessage,
                            color = if (updateSuccess) Color.Green else Color.Red,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            } ?: run {
                // User not found state
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Not Found",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "User not found",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }

            // Loading indicator when updating
            if (isUpdating) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            // Confirmation dialog
            if (showConfirmation) {
                AlertDialog(
                    onDismissRequest = { showConfirmation = false },
                    title = { Text("Save Changes?") },
                    text = { Text("Are you sure you want to update this student's information?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showConfirmation = false
                                isUpdating = true

                                // This would connect to your updateUser API
                                // For now, we'll simulate an API call

                                // Sample implementation:
                                /*
                                viewModelScope.launch {
                                    try {
                                        val response = attendanceService.updateUser(
                                            userId,
                                            UpdateUserRequest(
                                                name = name,
                                                year = year,
                                                department = department,
                                                contactNumber = contactNumber.toLong(),
                                                whatsappNumber = whatsappNumber.toLong()
                                            )
                                        )
                                        updateMessage = "User updated successfully"
                                        updateSuccess = true
                                        isEditing = false
                                    } catch (e: Exception) {
                                        updateMessage = "Failed to update: ${e.localizedMessage}"
                                        updateSuccess = false
                                    } finally {
                                        isUpdating = false
                                    }
                                }
                                */

                                // Simulated response
                                kotlinx.coroutines.MainScope().launch {
                                    kotlinx.coroutines.delay(1500)
                                    updateMessage = "User updated successfully"
                                    updateSuccess = true
                                    isEditing = false
                                    isUpdating = false
                                }
                            }
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}