package com.nshm.attendancesystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthorizedScreen(
    name: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.bgGreen)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.textGreen)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Image(
            painter = painterResource(id = R.drawable.codenest),
            contentDescription = "Logo",
            modifier = Modifier.width(250.dp).height(30.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Image(
            painter = painterResource(id = R.drawable.slogan),
            contentDescription = "Logo",
            modifier = Modifier.width(340.dp).height(28.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.textGreen)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You're now authorized to explore our features.",
            fontSize = 14.sp,
            color = colorResource(R.color.textGreen)
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {  },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .height(48.dp)
        ) {
            Text(
                text = "Scan Again",
                color = colorResource(R.color.textGreen)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthorizedScreenPreview() {
    AuthorizedScreen("Vivek Ghosh")
}