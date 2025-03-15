package com.nshm.attendancesystem

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

data class ScanResponse(
    val message: String,
    val user: User? = null,
    val error: String? = null
)

data class User(
    val _id: String,
    val name: String,
    val collegeEmail: String,
    val collegeId: Long,
    val year: String,
    val department: String,
    val contactNumber: Long,
    val whatsappNumber: Long,
    val token: String,
    val isPresent: Boolean,
    val isSeminarAttendee: Boolean,
    val __v: Int
)

data class RegistrationData(
    val name: String,
    val collegeEmail: String,
    val collegeId: Long,
    val year: String,
    val department: String,
    val contactNumber: Long,
    val whatsappNumber: Long
)

data class ApiResponse(
    val message: String
)

data class SendMailBody(
    val collegeId: Long
)