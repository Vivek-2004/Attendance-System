package com.nshm.attendancesystem

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

class AttendanceViewModel : ViewModel() {

    private val _attendanceService = attendanceService


    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    var messageScan by mutableStateOf("")
        private set

    var color by mutableStateOf("")
        private set

    var registeredStudentsList by mutableStateOf<List<User>>(emptyList())
        private set

    var message by mutableStateOf("")
        private set

    var response by mutableStateOf("")

    var mailMessage by mutableStateOf("")

    init {
        fetchStudentsList()
    }


    fun resetName() {
        _name.value = ""
    }

    // In AttendanceViewModel.kt, update the formatData method

    // In AttendanceViewModel.kt, update the formatData method

    private fun formatData(attendanceData: Response<ScanResponse>) {
        Log.d("AttendanceViewModel", "Response status: ${attendanceData.code()}")
        Log.d("AttendanceViewModel", "Response body: ${attendanceData.body()}")

        // Reset values first
        message = ""
        messageScan = ""
        color = ""

        // Get user data or default to empty string
        val responseBody = attendanceData.body()
        // Ensure _name is set even for errors
        _name.value = responseBody?.user?.name ?: "Unknown"
        message = responseBody?.message ?: ""

        // Log names and messages for debugging
        Log.d("AttendanceViewModel", "Name set to: ${_name.value}")
        Log.d("AttendanceViewModel", "Message set to: $message")

        if (message == "User checked in successfully") {
            messageScan = "Authorized"
            color = "green"
        } else if (message == "Duplicate entry") {
            messageScan = "Duplicate Scan"
            color = "yellow"
        } else {
            messageScan = "Unauthorized"  // Set a default message instead of empty
            color = "red"
        }

        // Log the final values being set
        Log.d("AttendanceViewModel", "Final messageScan: $messageScan, color: $color")
    }

    // Also, let's improve the fetchScan method for better error handling
    // Updated fetchScan()
    fun fetchScan(id: String) {
        viewModelScope.launch {
            try {
                val attendanceData = _attendanceService.getScanQr(id)
                if (attendanceData.isSuccessful) {
                    formatData(attendanceData)
                } else {
                    // Force non-empty name to trigger navigation
                    _name.value = "Error"
                    messageScan = "Error"
                    color = "red"
                    Log.e("ViewModel", "API Error: ${attendanceData.code()}")
                }
            } catch (e: Exception) {
                _name.value = "Error"  // Force navigation trigger
                messageScan = "Error"
                color = "red"
                Log.e("ViewModel", "Network Error: ${e.message}")
            }
        }
    }

    fun updateUserInfo(id: String,user: User) {
        viewModelScope.launch {
            try {
                val response = _attendanceService.updateUserInfo(id = id, userInfo = user)
            } catch (_: Exception) {
            }
        }
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

    fun sendMail(
        collegeId: String
    ) {
        viewModelScope.launch {
            mailMessage = try {
                val response = _attendanceService.sendMail(
                    collegeId = SendMailBody(
                        collegeId = collegeId.toLong()
                    )
                )
                if (response.code() == 200) {
                    "Email Sent Successfully"
                } else {
                    "Failed to Send Mail"
                }
            } catch (e: Exception) {
                "Failed to Send Mail"
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
                            "Registration Failed"
                        }
                    } ?: "Registration Failed"
                } else {
                    response = "Registration Failed with Error: ${e.code()}"
                }
            } catch (e: Exception) {
                response = "Registration Failed: ${e.localizedMessage}"
            }
        }
    }
}