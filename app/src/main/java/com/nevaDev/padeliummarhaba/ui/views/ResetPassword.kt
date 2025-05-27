package com.nevaDev.padeliummarhaba.ui.views


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.viewmodels.GetPasswordViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ResetPasswordViewModel
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.GetProfileResponse
import kotlinx.coroutines.launch

@Composable
fun ResetPassword(
    navController: NavController,
    loginRequest: LoginRequest,
    viewModel1: GetPasswordViewModel = hiltViewModel(),
    viewModel2: ResetPasswordViewModel = hiltViewModel(),
    ) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    var email by remember { mutableStateOf(loginRequest.username) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val backStackEntry = navController.currentBackStackEntry
    val keyboardController = LocalSoftwareKeyboardController.current
    var loginState by remember { mutableStateOf<LoginState>(LoginState.Idle) }

    LaunchedEffect(Unit) {
        viewModel2.dataResult.observe(lifecycleOwner) { result ->
            when (result) {
                is DataResult.Success -> {
                    Toast.makeText(
                        context, "Veuillez vérifier votre nouveau email et suivre les instructions pour réinitialiser votre mot de passe.", Toast.LENGTH_LONG).show()

                    navController.navigate("login_screen") {
                        popUpTo("reset_password") { inclusive = true }
                    }
                }

                is DataResult.Failure -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
                }

                is DataResult.Loading -> {
                }
            }
        }
    }





    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.padeliuum),
                    contentDescription = "Base Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.a90),
                    contentDescription = "Overlay Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 145.dp, y = 60.dp),
                    contentScale = ContentScale.Fit

                )
            }

            Spacer(modifier = Modifier.height(66.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF3CD), shape = RoundedCornerShape(4.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "Veuillez renseigner l'adresse email utilisée pour vous enregistrer",
                    color = Color(0xFF856404),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            var isEmailError by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it.trim()
                    isEmailError = !email.matches(emailPattern)
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                label = { Text(stringResource(R.string.email)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.mail),
                        contentDescription = "E-mail Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                ),
                isError = isEmailError
            )


            Spacer(modifier = Modifier.height(8.dp))






            Spacer(modifier = Modifier.height(8.dp))



            Spacer(modifier = Modifier.height(26.dp))
            val isEmailValid = email.matches(emailPattern)
            val isButtonEnabled = isEmailValid  && email.isNotBlank()

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        errorMessage = null

                        try {
                            viewModel1.GetPassword(email)
                            viewModel2.ResetPassword(email)

                        } catch (e: Exception) {
                            errorMessage = "Une erreur est survenue. Veuillez réessayer."
                            loginState = LoginState.Idle
                        } finally {
                            isLoading = false
                        }
                    }
                },

                enabled = !isLoading && isButtonEnabled,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                shape = RoundedCornerShape(13.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                } else {
                    Text(
                        text = "RÉINITIALISER LE MOT DE PASSE",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        color = Color(android.graphics.Color.parseColor("#999999")),
                        thickness = 1.dp
                    )

                }
                Spacer(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Text(
                        text = stringResource(R.string.signinbutoon),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.signinredirection),
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("signup_screen") },
                        textDecoration = Underline
                    )
                }
            }
        }
    }
}



