package com.nshm.attendancesystem

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TabRegister(attendanceViewModel: AttendanceViewModel = viewModel()) {
    // State variables for user input
    var name by remember { mutableStateOf("") }
    var collegeEmail by remember { mutableStateOf("") }
    var collegeId by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }

    // State to observe response and show toast
    val response = attendanceViewModel.response



    // FocusRequesters for managing focus
    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val idFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    val deptFocus = remember { FocusRequester() }
    val contactFocus = remember { FocusRequester() }
    val whatsappFocus = remember { FocusRequester() }

    // Keyboard controller
    val keyboardController = LocalSoftwareKeyboardController.current

    // Show toast when showToast is true
    val context = LocalContext.current

    // Show toast when response updates (optional for success feedback)
    LaunchedEffect(attendanceViewModel.response) {
        if (attendanceViewModel.response.isNotBlank()) {
            Toast.makeText(context, attendanceViewModel.response, Toast.LENGTH_SHORT).show()
            attendanceViewModel.response = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            placeholder = { Text("Enter your name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() })
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = collegeEmail,
            onValueChange = { collegeEmail = it },
            label = { Text("College Email ID") },
            placeholder = { Text("example@college.edu") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { idFocus.requestFocus() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = collegeId,
            onValueChange = { collegeId = it },
            label = { Text("College ID") },
            placeholder = { Text("Enter your college ID") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(idFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { yearFocus.requestFocus() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Current Year") },
            placeholder = { Text("e.g., 1st, 2nd, etc.") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(yearFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { deptFocus.requestFocus() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") },
            placeholder = { Text("Enter your department") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(deptFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { contactFocus.requestFocus() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            placeholder = { Text("Enter your contact number") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(contactFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { whatsappFocus.requestFocus() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = whatsappNumber,
            onValueChange = { whatsappNumber = it },
            label = { Text("WhatsApp Number") },
            placeholder = { Text("Enter your WhatsApp number") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(whatsappFocus),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle form submission
                attendanceViewModel.registerUser(
                    name = name,
                    collegeEmail = collegeEmail,
                    collegeId = collegeId.toLongOrNull() ?: 0L,
                    year = year,
                    department = department,
                    contactNumber = contactNumber.toLongOrNull() ?: 0L,
                    whatsappNumber = whatsappNumber.toLongOrNull() ?: 0L
                )


                // Reset text fields after registration
                name = ""
                collegeEmail = ""
                collegeId = ""
                year = ""
                department = ""
                contactNumber = ""
                whatsappNumber = ""
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}
