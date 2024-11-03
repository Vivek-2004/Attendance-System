package com.nshm.attendancesystem

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TabRegister(attendanceViewModel: AttendanceViewModel = viewModel()) {

    // State variables for user input
    var name by remember { mutableStateOf("") }
    var collegeEmail by remember { mutableStateOf("") }
    var collegeId by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }

    // Column layout for the registration form
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Text fields for user input
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = collegeEmail,
            onValueChange = { collegeEmail = it },
            label = { Text("College Email ID") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = collegeId,
            onValueChange = { collegeId = it },
            label = { Text("College ID") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Current Year") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = whatsappNumber,
            onValueChange = { whatsappNumber = it },
            label = { Text("WhatsApp Number") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Register button
        Button(
            onClick = {
                attendanceViewModel.registerUser(
                    name = name,
                    collegeEmail = collegeEmail,
                    collegeId = collegeId.toLongOrNull() ?: 0L,
                    year = year,
                    department = "CSE", // assuming department is fixed as "CSE" based on the sample data
                    contactNumber = contactNumber.toLongOrNull() ?: 0L,
                    whatsappNumber = whatsappNumber.toLongOrNull() ?: 0L
                )
                name = ""
                collegeEmail = ""
                collegeId = ""
                year = ""
                contactNumber = ""
                whatsappNumber = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }
    }
}
