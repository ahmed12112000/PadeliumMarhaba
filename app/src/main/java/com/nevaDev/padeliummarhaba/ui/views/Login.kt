package com.nevaDev.padeliummarhaba.ui.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
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
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.GetProfileResponse
import kotlinx.coroutines.launch

sealed interface LoginState {
    object Idle : LoginState
    object Success : LoginState
    object ProfileFetching : LoginState
    object Completed : LoginState
}
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
    getProfileViewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    loginRequest: LoginRequest,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    var email by remember { mutableStateOf(loginRequest.username) }
    var password by remember { mutableStateOf(loginRequest.password) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val backStackEntry = navController.currentBackStackEntry
    val destinationRoute = backStackEntry?.arguments?.getString("destination") ?: "main_screen"
    val redirectUrl = backStackEntry?.arguments?.getString("redirectUrl")
    val profileResult = getProfileViewModel.profileData.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var loginState by remember { mutableStateOf<LoginState>(LoginState.Idle) }
    var pendingToken: String? by remember { mutableStateOf(null) }

    // Replace your current observer with this improved version:

    viewModel.dataResult1.observe(lifecycleOwner) { result ->
        isLoading = false
        Log.d("LoginDebug", "Observer triggered with result: $result")

        when (result) {
            is Resulta.Loading -> {
                Log.d("LoginDebug", "Loading state")
                isLoading = true
            }
            is Resulta.Success -> {
                Log.d("LoginDebug", "Success state - data: ${result.data}")

                // For session-based authentication, we don't need to extract a token
                // The success response means authentication worked and session is established
                if (result.data != null) {
                    Log.d("LoginDebug", "Login successful - using session-based auth")

                    // Use a placeholder token or session identifier
                    // The actual authentication is handled by cookies/session
                    pendingToken = "session_authenticated"

                    // Clear any previous error messages
                    errorMessage = null

                    loginState = LoginState.Success
                } else {
                    Log.e("LoginDebug", "Success but data is null!")
                    errorMessage = "Erreur de connexion - données manquantes"
                }
            }
            is Resulta.Failure -> {
                Log.e("LoginDebug", "Failure state: ${result.exception?.message}")
                errorMessage = "E-mail ou Mot de passe invalide"
            }
            else -> {
                Log.d("LoginDebug", "Unknown state: $result")
            }
        }
    }

    LaunchedEffect(loginState) {
        if (loginState == LoginState.Success) {
            getProfileViewModel.fetchProfileData()
            loginState = LoginState.ProfileFetching
        }
    }

    LaunchedEffect(profileResult.value) {
        when (val result = profileResult.value) {
            is DataResultBooking.Success -> {
                Log.d("ProfileDebug", "Profile Success - proceeding with login")

                // Assume non-admin user for now
                pendingToken?.let {
                    val expiresIn = 1800000L
                    sessionManager.saveAuthToken(it, expiresIn)
                }

                navController.navigate(redirectUrl ?: destinationRoute) {
                    popUpTo("login_screen") { inclusive = true }
                }

                loginState = LoginState.Completed
                onLoginSuccess()
            }

            is DataResultBooking.Failure -> {
                Log.e("ProfileDebug", "Profile failed, but proceeding anyway for testing")

                // For testing - proceed even if profile fails
                pendingToken?.let {
                    val expiresIn = 1800000L
                    sessionManager.saveAuthToken(it, expiresIn)
                }

                navController.navigate(redirectUrl ?: destinationRoute) {
                    popUpTo("login_screen") { inclusive = true }
                }

                loginState = LoginState.Completed
                onLoginSuccess()
            }

            else -> Unit
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

                var isEmailError by remember { mutableStateOf(false) }
                var isPasswordError by remember { mutableStateOf(false) }

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


                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it.trim()
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    ),
                    label = { Text(stringResource(R.string.password)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.password),
                            contentDescription = "Password Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    trailingIcon = {
                        val iconRes =
                            if (passwordVisible) R.drawable.showpassword else R.drawable.hidepassword
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black,
                    ),
                    isError = isPasswordError
                )



                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = Color(0xFF0054D8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            navController.navigate("reset_password")

                        }
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))
                val isEmailValid = email.matches(emailPattern)
                val isButtonEnabled =
                    isEmailValid  && email.isNotBlank() && password.isNotBlank()

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val updatedRequest = loginRequest.copy(username = email, password = password)

                            viewModel.loginUser(updatedRequest)
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
                            text = stringResource(R.string.login_button),
                            color = Color.White,
                            fontSize = 25.sp,
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



