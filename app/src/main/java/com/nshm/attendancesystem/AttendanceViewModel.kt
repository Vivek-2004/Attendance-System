package com.nshm.attendancesystem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val _attendanceService = attendanceService

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
                val attendanceData = _attendanceService.getScanBar(id)
                formatData(attendanceData)
            } catch (e: Exception) {
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
            } catch (e: Exception) {
                fetchStudentsList()
            }
        }
    }
}