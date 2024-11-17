package com.nshm.attendancesystem

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
    var collegeId by remember { mutableStateOf("") }
    var collegeEmail by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("") }

    val nameFocus = remember { FocusRequester() }
    val idFocus = remember { FocusRequester() }
    val emailFocus = remember { FocusRequester() }
    val deptFocus = remember { FocusRequester() }
    val yearFocus = remember { FocusRequester() }
    val contactFocus = remember { FocusRequester() }
    val whatsappFocus = remember { FocusRequester() }

    var deptExpanded by remember { mutableStateOf(false) }
    val departments = listOf("B.Tech CSE", "B.Tech ECE", "BCA", "MCA")
    val selectedDepartment = remember { mutableStateOf("Select Department") }

    var yearExpanded by remember { mutableStateOf(false) }
    val years = listOf("1st", "2nd", "3rd", "4th")
    val selectedYear = remember { mutableStateOf("Select Current Year") }

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
            keyboardActions = KeyboardActions(onNext
            = { emailFocus.requestFocus() })
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
            keyboardActions = KeyboardActions(onNext
            = { contactFocus.requestFocus() }),
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
            keyboardActions = KeyboardActions(onNext
            = { idFocus.requestFocus() }),
            singleLine = true
        )

        OutlinedTextField(
            value = selectedDepartment.value,
            onValueChange = { selectedDepartment.value = it },
            label = { Text("Department") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(deptFocus)
                .weight(1f)
                .clickable { deptExpanded = true },
            shape = MaterialTheme.shapes.medium,
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { contactFocus.requestFocus() }),
            trailingIcon = {
                IconButton(onClick = { deptExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Department")
                }
                DropdownMenu(
                    expanded = deptExpanded,
                    onDismissRequest = { deptExpanded = false },
                ) {
                    departments.forEach { option ->
                        DropdownMenuItem(
                            onClick = { selectedDepartment.value = option; deptExpanded = false },
                            text = { Text(text = option) }
                        )
                    }
                }
            }
        )

        OutlinedTextField(
            value = selectedYear.value,
            onValueChange = { selectedYear.value = it },
            label = { Text("Current Year") },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(yearFocus)
                .weight(1f)
                .clickable { yearExpanded = true },
            shape = MaterialTheme.shapes.medium,
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext
            = { contactFocus.requestFocus() }),
            trailingIcon = {
                IconButton(onClick = { yearExpanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select Year")
                }
                DropdownMenu(
                    expanded = yearExpanded,
                    onDismissRequest = { yearExpanded = false },
                ) {
                    years.forEach { option ->
                        DropdownMenuItem(
                            onClick = { selectedYear.value = option; yearExpanded = false },
                            text = { Text(text = option) }
                        )
                    }
                }
            }
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
            keyboardActions = KeyboardActions(onNext
            = { whatsappFocus.requestFocus() }),
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
            keyboardActions = KeyboardActions(onDone
            = { keyboardController?.hide()
            }),
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
                .weight(1f),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Register", style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}