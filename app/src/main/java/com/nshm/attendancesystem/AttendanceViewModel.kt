package com.nshm.attendancesystem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AttendanceViewModel:ViewModel() {
    private val _attendanceService = attendanceService

    var message by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var clgId by mutableStateOf("")

    fun fetchScan(id: String){
        viewModelScope.launch {
            try {
                val attendanceData = _attendanceService.getScanBar(id)
                formatData(attendanceData)
            } catch (e: Exception){
                message = "Some error"
            }
        }
    }

    private fun formatData(attendanceData: ScanResponse){
        message = if(attendanceData.message == "true"){ "Present" } else { attendanceData.message }
        name = attendanceData.user.name
        clgId = attendanceData.user.collegeId.toString()
    }
}