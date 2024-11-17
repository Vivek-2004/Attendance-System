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
fun RegisterScreen(attendanceViewModel: AttendanceViewModel = viewModel()) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var collegeEmail by remember { mutableStateOf("") }
    var collegeId by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }

    val nameFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val idFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    val deptFocus = remember { FocusRequester() }
    val contactFocus = remember { FocusRequester() }
    val whatsappFocus = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(attendanceViewModel.response) {
        if (attendanceViewModel.response.isNotBlank()) {
            Toast.makeText(context, attendanceViewModel.response, Toast.LENGTH_LONG).show()
            attendanceViewModel.response = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD), Color(0xFFBBDEFB)
                    )
                )
            )
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Student Name") },
            placeholder = { Text("Enter Your Name") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(nameFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { emailFocus.requestFocus() })
        )

        OutlinedTextField(
            value = collegeId,
            onValueChange = { collegeId = it },
            label = { Text("College ID") },
            placeholder = { Text("Enter Your College ID") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(idFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { yearFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = collegeEmail,
            onValueChange = { collegeEmail = it },
            label = { Text("College Email") },
            placeholder = { Text("example.20@nshm.edu.in") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(emailFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { idFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = department,
            onValueChange = { department = it },
            label = { Text("Department") },
            placeholder = { Text("Enter Your Department") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(deptFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { contactFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Current Year") },
            placeholder = { Text("1st, 2nd, 3rd, 4th") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(yearFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { deptFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number") },
            placeholder = { Text("Enter Your Contact Number") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(contactFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { whatsappFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = whatsappNumber,
            onValueChange = { whatsappNumber = it },
            label = { Text("WhatsApp Number") },
            placeholder = { Text("Enter Your WhatsApp Number") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(whatsappFocus)
                .weight(1f),
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            singleLine = true
        )

        Button(
            onClick = {
                attendanceViewModel.registerUser(
                    name = name,
                    collegeEmail = collegeEmail,
                    collegeId = collegeId.toLongOrNull() ?: 0L,
                    year = year,
                    department = department,
                    contactNumber = contactNumber.toLongOrNull() ?: 0L,
                    whatsappNumber = whatsappNumber.toLongOrNull() ?: 0L
                )

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
                .padding(top = 16.dp)
                .weight(1.3f),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}