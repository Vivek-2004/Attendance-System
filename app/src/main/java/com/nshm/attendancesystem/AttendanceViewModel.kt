package com.nshm.attendancesystem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class AttendanceViewModel : ViewModel() {

    private val _attendanceService = attendanceService

    var response by mutableStateOf("")

    var messageScan by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var clgId by mutableStateOf("")

    var registeredStudentsList by mutableStateOf<List<User>>(emptyList())
        private set

    fun fetchScan(id: String) {
        viewModelScope.launch {
            try {
                val attendanceData = _attendanceService.getScanQr(id)
                formatData(attendanceData)
            } catch (_: Exception) {
                messageScan = ""
            }
        }
    }

    private fun formatData(attendanceData: ScanResponse) {
        messageScan = if (attendanceData.message == "true") {
            "Present"
        } else {
            attendanceData.message
        }
        name = attendanceData.user.name
        clgId = attendanceData.user.collegeId.toString()
    }

    fun fetchStudentsList() {
        viewModelScope.launch {
            try {
                val registeredStudents = _attendanceService.getRegisteredStudents()
                registeredStudentsList = registeredStudents
            } catch (_: Exception) {
                fetchStudentsList()
            }
        }
    }

    fun registerUser(
        name: String,
        collegeEmail: String,
        collegeId: Long,
        year: String,
        department: String,
        contactNumber: Long,
        whatsappNumber: Long
    ) {
        // Create the UserRegistration object from the provided data
        val userRegistration = RegistrationData(
            name = name,
            collegeEmail = collegeEmail,
            collegeId = collegeId,
            year = year,
            department = department,
            contactNumber = contactNumber,
            whatsappNumber = whatsappNumber
        )
        viewModelScope.launch {

            try {
                val response1 = attendanceService.registerUser(userRegistration)
                response = response1.message
            } catch (e: HttpException) {
                if (e.code() == 400) {
                    val errorBody = e.response()?.errorBody()?.string()

                    response = errorBody?.let {
                        try {
                            JSONObject(it).getString("message")
                        } catch (jsonException: Exception) {
                            "Registration failed: Bad Request (Error 400)"
                        }
                    } ?: "Registration failed: Bad Request (Error 400)"
                } else {
                    response = "Registration failed with error: ${e.code()}"
                }
            } catch (e: Exception) {
                response = "Registration failed: ${e.localizedMessage}"
            }
        }
    }
}