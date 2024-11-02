// ScannedScreen.kt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@Composable
fun Scanned(attendanceViewModel: AttendanceViewModel = viewModel()) {
    var isRefreshing by remember { mutableStateOf(false) }

    // Trigger fetchStudentsList on first launch or when refreshing
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            attendanceViewModel.fetchStudentsList()
            delay(5000) // Simulate a network call or refresh delay
            isRefreshing = false // Reset refreshing state after the delay
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = {
            isRefreshing = true
            attendanceViewModel.fetchStudentsList()
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                items(attendanceViewModel.registeredStudentsList) { user ->
                    Text(text = "Name: ${user.name}")
                    Text(text = "College ID: ${user.collegeId}")
                    Text(text = "Email: ${user.collegeEmail}")
                    Text(text = "Present: ${if (user.isPresent) "Yes" else "No"}")
                }
            }
        }
    }
}
