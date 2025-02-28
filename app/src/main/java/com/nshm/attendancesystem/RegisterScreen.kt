package com.nshm.attendancesystem

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(attendanceViewModel: AttendanceViewModel = viewModel()) {
    // Context and keyboard controller for UI interactions
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()

    // Constants
    val clgDomain = "@nshm.edu.in"

    // Helper function to show toast messages
    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Form state variables
    var name by remember { mutableStateOf("") }
    var collegeId by remember { mutableStateOf("") }
    var collegeEmail by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    var clgId by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var sendMail by remember { mutableStateOf(false) }

    // Focus requesters for form fields
    val nameFocus = remember { FocusRequester() }
    val idFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val deptFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    val contactFocus = remember { FocusRequester() }
    val whatsappFocus = remember { FocusRequester() }

    // Department dropdown state
    var deptExpanded by remember { mutableStateOf(false) }
    val departments = listOf("CSE", "AIML", "BCA", "DS", "ECE", "ME", "MCA", "BBA", "Civil")
    val selectedDepartment = remember { mutableStateOf("Select Department") }

    // Year dropdown state
    var yearExpanded by remember { mutableStateOf(false) }
    val years = listOf("1st", "2nd", "3rd", "4th")
    val selectedYear = remember { mutableStateOf("Select Current Year") }

    var isMailClicked by remember { mutableStateOf(false) }
    var registrationSuccess by remember { mutableStateOf(false) }

    if (attendanceViewModel.mailMessage != "") {
        Toast.makeText(context, attendanceViewModel.mailMessage, Toast.LENGTH_SHORT).show()
        attendanceViewModel.mailMessage = ""
    }

    LaunchedEffect(sendMail) {
        if (!sendMail) return@LaunchedEffect
        try {
            attendanceViewModel.sendMail(collegeId = clgId)
        } finally {
            sendMail = false
            clgId = ""
        }
    }

    // Handle registration response from ViewModel
    LaunchedEffect(attendanceViewModel.response) {
        if (attendanceViewModel.response.isNotBlank()) {
            if (attendanceViewModel.response == "User with this ID already exists" ||
                attendanceViewModel.response == "User with this email already exists"
            ) {
                showToast("User Already Exists")
            } else {
                showToast("User Registered Successfully")
                registrationSuccess = true
                // Reset registration success after showing confirmation
                delay(3000)
                registrationSuccess = false
            }
            attendanceViewModel.response = ""
        }
    }

    if (isMailClicked) {
        AlertDialog(
            onDismissRequest = {
                isMailClicked = false
                errorMessage = ""
                clgId = ""
            },
            title = { Text(text = "Enter College ID") },
            text = {
                Column {
                    OutlinedTextField(
                        value = clgId,
                        onValueChange = { clgId = it },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = Color.Red
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (clgId.length != 11) {
                        errorMessage = "College Id Must be 11 Digits"
                    } else {
                        sendMail = true
                        errorMessage = ""
                        isMailClicked = false
                    }
                }
                ) {
                    Text(text = "Send", fontSize = 14.5.sp)
                }
            },
            containerColor = Color.White
        )
    }

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB)
                    )
                )
            )
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Custom TextField function to reduce boilerplate
                @Composable
                fun CustomTextField(
                    value: String,
                    onValueChange: (String) -> Unit,
                    label: String,
                    placeholder: String,
                    focusRequester: FocusRequester,
                    keyboardType: KeyboardType = KeyboardType.Text,
                    imeAction: ImeAction = ImeAction.Next,
                    onAction: () -> Unit = {},
                    readOnly: Boolean = false,
                    trailingIcon: @Composable (() -> Unit)? = null,
                    leadingIcon: ImageVector? = null
                ) {
                    OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        label = { Text(label) },
                        placeholder = { Text(placeholder) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        shape = MaterialTheme.shapes.medium,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = keyboardType,
                            imeAction = imeAction
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { onAction() },
                            onDone = { keyboardController?.hide() }
                        ),
                        readOnly = readOnly,
                        trailingIcon = trailingIcon,
                        leadingIcon = leadingIcon?.let {
                            {
                                Icon(
                                    imageVector = it,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedLabelColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Personal Information Section
                SectionHeader("Personal Information")

                // Name field
                CustomTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Student Name",
                    placeholder = "Enter Your Name",
                    focusRequester = nameFocus,
                    onAction = { emailFocus.requestFocus() },
                    leadingIcon = Icons.Filled.Person
                )

                // College ID field
                CustomTextField(
                    value = collegeId,
                    onValueChange = { collegeId = it },
                    label = "College ID",
                    placeholder = "Enter Your College ID",
                    focusRequester = idFocus,
                    keyboardType = KeyboardType.Number,
                    onAction = { contactFocus.requestFocus() },
                    leadingIcon = Icons.Filled.Info
                )

                // Email field
                CustomTextField(
                    value = collegeEmail,
                    onValueChange = { collegeEmail = it },
                    label = "College Email",
                    placeholder = "example.24@nshm.edu.in",
                    focusRequester = emailFocus,
                    keyboardType = KeyboardType.Email,
                    onAction = { idFocus.requestFocus() },
                    leadingIcon = Icons.Filled.Email
                )

                // Academic Information Section
                SectionHeader("Academic Information")

                // Department field
                CustomTextField(
                    value = selectedDepartment.value,
                    onValueChange = { selectedDepartment.value = it },
                    label = "Department",
                    placeholder = "Select Your Department",
                    focusRequester = deptFocus,
                    readOnly = true,
                    leadingIcon = Icons.Filled.School,
                    trailingIcon = {
                        IconButton(onClick = { deptExpanded = true }) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Department"
                            )
                        }
                        DropdownMenu(
                            expanded = deptExpanded,
                            onDismissRequest = { deptExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            departments.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedDepartment.value = option
                                        deptExpanded = false
                                    },
                                    text = { Text(text = option) }
                                )
                            }
                        }
                    }
                )

                // Year field
                CustomTextField(
                    value = selectedYear.value,
                    onValueChange = { selectedYear.value = it },
                    label = "Current Year",
                    placeholder = "Select Your Current Year",
                    focusRequester = yearFocus,
                    readOnly = true,
                    leadingIcon = Icons.Filled.School,
                    trailingIcon = {
                        IconButton(onClick = { yearExpanded = true }) {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = "Select Year"
                            )
                        }
                        DropdownMenu(
                            expanded = yearExpanded,
                            onDismissRequest = { yearExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            years.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedYear.value = option
                                        yearExpanded = false
                                    },
                                    text = { Text(text = option) }
                                )
                            }
                        }
                    }
                )

                // Contact Information Section
                SectionHeader("Contact Information")

                // Contact number field
                CustomTextField(
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    label = "Contact Number",
                    placeholder = "Enter Your Contact Number",
                    focusRequester = contactFocus,
                    keyboardType = KeyboardType.Phone,
                    onAction = { whatsappFocus.requestFocus() },
                    leadingIcon = Icons.Filled.Phone
                )

                // WhatsApp number field
                CustomTextField(
                    value = whatsappNumber,
                    onValueChange = { whatsappNumber = it },
                    label = "WhatsApp Number",
                    placeholder = "Enter Your WhatsApp Number",
                    focusRequester = whatsappFocus,
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done,
                    leadingIcon = Icons.Filled.Phone
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Registration button handler
            fun onButtonClick() {
                attendanceViewModel.registerUser(
                    name = name,
                    collegeEmail = collegeEmail,
                    collegeId = collegeId.toLongOrNull() ?: 0L,
                    year = selectedYear.value,
                    department = selectedDepartment.value,
                    contactNumber = contactNumber.toLongOrNull() ?: 0L,
                    whatsappNumber = whatsappNumber.toLongOrNull() ?: 0L
                )

                // Reset form fields
                name = ""
                collegeEmail = ""
                collegeId = ""
                selectedYear.value = "Select Current Year"
                selectedDepartment.value = "Select Department"
                contactNumber = ""
                whatsappNumber = ""

                // Show loading indicator
                isLoading = true

                CoroutineScope(Dispatchers.Main).launch {
                    delay(3200)
                    isLoading = false
                }
            }

            // Register Button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(56.dp),
                onClick = {
                    // Validate all fields before submitting
                    if (
                        name.isNotBlank() &&
                        collegeId.isNotBlank() &&
                        collegeEmail.isNotBlank() &&
                        selectedDepartment.value != "Select Department" &&
                        selectedYear.value != "Select Current Year" &&
                        contactNumber.isNotBlank() &&
                        whatsappNumber.isNotBlank()
                    ) {
                        // Validate college email domain
                        if (collegeEmail.endsWith(clgDomain)) {
                            // Validate college ID length
                            if (collegeId.length == 11) {
                                // Validate contact number length
                                if (contactNumber.length == 10) {
                                    // Validate WhatsApp number length
                                    if (whatsappNumber.length == 10) {
                                        onButtonClick()
                                    } else {
                                        showToast("WhatsApp Number must be 10 Digits")
                                    }
                                } else {
                                    showToast("Contact Number must be 10 Digits")
                                }
                            } else {
                                showToast("College ID must be 11 Digits")
                            }
                        } else {
                            showToast("Enter a Valid College Email")
                        }
                    } else {
                        showToast("All Fields are Required")
                    }
                },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 2.dp
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }
        }

        // Success animation overlay
        if (registrationSuccess) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = "Success",
                        tint = Color.Green,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Registration Successful!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { isMailClicked = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(25.dp),
            containerColor = Color.Black,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            elevation = FloatingActionButtonDefaults.elevation(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email"
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    }
}