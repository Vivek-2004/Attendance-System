// AttendanceViewModel.kt
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nshm.attendancesystem.ScanResponse
import com.nshm.attendancesystem.User
import com.nshm.attendancesystem.attendanceService
import kotlinx.coroutines.launch

class AttendanceViewModel : ViewModel() {

    private val _attendanceService = attendanceService

    var message by mutableStateOf("")
        private set

    var name by mutableStateOf("")
        private set

    var clgId by mutableStateOf("")

    // State to hold registered students list
    var registeredStudentsList by mutableStateOf<List<User>>(emptyList())
        private set

    fun fetchScan(id: String) {
        viewModelScope.launch {
            try {
                val attendanceData = _attendanceService.getScanBar(id)
                formatData(attendanceData)
                fetchStudentsList()
            } catch (e: Exception) {
                message = "Some error"
            }
        }
    }

    fun fetchStudentsList() {
        viewModelScope.launch {
            try {
                val userListResponse = _attendanceService.getRegisteredStudents()
                registeredStudentsList = userListResponse.StudentsList // Update the list
                println("Fetched students: ${registeredStudentsList.size}")
            } catch (e: Exception) {
                message = "Some error"
                println("Error fetching students: $e")
            }
        }
    }

    private fun formatData(attendanceData: ScanResponse) {
        message = if (attendanceData.message == "true") { "Present" } else { attendanceData.message }
        name = attendanceData.user.name
        clgId = attendanceData.user.collegeId.toString()
    }
}
