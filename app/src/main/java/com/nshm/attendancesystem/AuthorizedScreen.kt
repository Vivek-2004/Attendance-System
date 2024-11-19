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
import androidx.navigation.NavController
import java.util.Locale

@Composable
fun AuthorizedScreen(
    name: String,
    message: String,
    color: String,
    navController: NavController,
) {
    var bgColor: Int = R.color.white
    var txtColor: Int = R.color.white

    if (color == "green"){
        bgColor = R.color.bgGreen
        txtColor = R.color.textGreen
    } else if(color == "yellow") {
        bgColor = R.color.bgYellow
        txtColor = R.color.textYellow
    } else if (color == "red") {
        bgColor = R.color.bgRed
        txtColor = R.color.black
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(bgColor)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(txtColor)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.app_logo_xml),
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
            text = name.uppercase(Locale.ROOT),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(txtColor)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 22.sp,
            color = colorResource(txtColor)
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (color == "red") {
            Button(
                onClick = {
                    navController.navigate("Register")
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "Register Now",
                    color = colorResource(txtColor)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
        }

        Button(
            onClick = {
                navController.navigate("Scan")
                      },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .height(48.dp)
        ) {
            Text(
                text = "Scan Again",
                color = colorResource(txtColor)
            )
        }
    }
}