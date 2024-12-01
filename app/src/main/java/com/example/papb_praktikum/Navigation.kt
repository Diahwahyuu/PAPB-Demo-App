package com.example.papb_praktikum

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.papb_praktikum.pages.Homepage
import com.example.papb_praktikum.pages.Login
import com.example.papb_praktikum.pages.SignUp

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            Login(modifier, navController, authViewModel)
        }
        composable("home") {
            Homepage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignUp(modifier, navController, authViewModel)
        }
    })
}