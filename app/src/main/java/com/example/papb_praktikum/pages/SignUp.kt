package com.example.papb_praktikum.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.papb_praktikum.AuthState
import com.example.papb_praktikum.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp (
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,(authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFF080B1C)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(text = "SIGN UP PAGE", fontSize = 32.sp, color = Color.White)
        Spacer(modifier = Modifier.height((16.dp)))

        OutlinedTextField(
            value = email,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email", color = Color.White)
            },
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.White,
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White
            ),
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password", color = Color.White)
            },
            shape = RoundedCornerShape(50)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                authViewModel.signup(email, password)
            },
            enabled = authState.value != AuthState.Loading,
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(50.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(
                text = "Sign Up",
                color = Color.White
            )
        }

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Already have an account ? Login", color = Color.White)
        }

    }
}