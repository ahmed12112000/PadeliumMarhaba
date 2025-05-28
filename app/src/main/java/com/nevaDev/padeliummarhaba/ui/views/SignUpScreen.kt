package com.nevaDev.padeliummarhaba.ui.views

import android.widget.Toast
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.SignupRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavController,
    onSignupSuccess: () -> Unit,
    viewModel : UserViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var showMessage by remember { mutableStateOf(false) }
    val result by viewModel.dataResult.observeAsState()

    LaunchedEffect(result) {
        when (result) {
            is DataResult.Loading -> {
                isLoading = true
            }

            is DataResult.Success -> {
                isLoading = false
                message = "Inscription réussie"
                isSuccess = true
                navController.navigate("SignUp_SuccessScreen")
                delay(5000)
                message = ""
            }

            is DataResult.Failure -> {
                isLoading = false
                message = "Compte déjà existe"
                showMessage = true
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                delay(3000)
                message = ""
                showMessage = false
            }

            else -> {}
        }
    }

    if (showMessage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus() },
            contentAlignment = Alignment.Center

        ) {
            Image(
                painter = painterResource(id = R.drawable.a90),
                contentDescription = "Overlay Image",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 158.dp, y = 40.dp)
                    .border(2.dp, Color.Unspecified)
            )
            Image(
                painter = painterResource(id = R.drawable.padeliuum),
                contentDescription = "Base Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -65.dp, x = 10.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                label = { Text(stringResource(R.string.firstName)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, top = 8.dp)
                    .offset(x = -22.dp, y = -8.dp)
                    .shadow(8.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                )

            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.lastName)) },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                leadingIcon = {
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .offset(x = -20.dp)
                    .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray)
                ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                )
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        var isEmailError by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.trim()
                isEmailError = !email.matches(emailPattern)
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.fillMaxWidth().offset(y = -75.dp, x = -10.dp)
                .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            isError = isEmailError

        )

    }

    Spacer(modifier = Modifier.height(8.dp))

    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    // Password validation function
    fun validatePassword(password: String): Boolean {
        return password.length >= 8 && password.any { it.isUpperCase() }
    }

    // Update password error state
    isPasswordError = password.isNotEmpty() && !validatePassword(password)

    Column {
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it.trim()
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                }
            ),
            label = { Text(stringResource(R.string.password)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.password1),
                    contentDescription = "Password Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
            trailingIcon = {
                val iconRes = if (passwordVisible) R.drawable.showpassword else R.drawable.hidepassword
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 1.dp, end = 24.dp)
                .offset(x = 5.dp, y = 355.dp)
                .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = if (isPasswordError) Color.Red else Color.Black,
                unfocusedBorderColor = if (isPasswordError) Color.Red else Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            isError = isPasswordError
        )

        // Password validation message
        if (isPasswordError) {
            Text(
                text = "Utilisez au moins 8 caractères et incluez une lettre majuscule.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 6.dp, top = 4.dp, end = 24.dp)
                    .offset(x = 5.dp, y = 355.dp)
            )
        }
    }

    Spacer(modifier = Modifier.height(2.dp))

    var checked by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 1.dp, end = 24.dp)
            .offset(x = 22.dp, y = if (isPasswordError) 470.dp else 450.dp) // Adjust position based on error message
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it
                showErrorMessage = false},
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            ),
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(35.dp))
                .shadow(50.dp, RoundedCornerShape(35.dp))
        )

        Spacer(modifier = Modifier.width(1.dp))

        Text(
            text = buildAnnotatedString {
                append("J'accepte ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {

                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = Underline,
                        color = Color.Black
                    )
                ) {
                    append("les Conditions d'utilisation")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {
                    append(" & la ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = Underline,
                        color = Color.Black
                    )
                ) {
                    append("Politique de confidentalité")
                }
                append(".")
            },
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }

    val isEmailValid = email.matches(emailPattern)
    val isPasswordValid = validatePassword(password) // Use the new validation function
    val isFirstNameValid = firstName.isNotBlank()
    val isLastNameValid = lastName.isNotBlank()
    val isButtonEnabled = isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid

    Button(
        onClick = {
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Vérifier les champs", Toast.LENGTH_SHORT).show()
            } else if (!checked) {
                Toast.makeText(context, "Veuillez accepter les conditions générales et la politique de confidentialité", Toast.LENGTH_SHORT).show()
            } else {
                isLoading = true
                val signupRequest = SignupRequest(email, password, firstName, lastName)
                viewModel.signupUser (signupRequest)
            }
        },
        enabled = isButtonEnabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .offset(x = 10.dp, y = if (isPasswordError) 540.dp else 520.dp) // Adjust position based on error message
            .padding(start = 27.dp, end = 60.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = "ENREGISTREMENT",
            color = Color.White,
            fontSize = 25.sp
        )
    }

    if (message.isNotEmpty() && !isSuccess) {
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    Spacer(modifier = Modifier.height(60.dp))
    Row (modifier = Modifier.fillMaxSize()
        .offset(x = 120.dp, y = if (isPasswordError) 620.dp else 600.dp))
    {
        Text(text = stringResource(R.string.logininbutoon), color = Color.Gray)
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = stringResource(R.string.loginredirection),
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.clickable {  navController.navigate("login_screen") },
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottompadelium),
            contentDescription = "Bottom Icon",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(110.dp)
                .height(200.dp)
                .padding(bottom = 36.dp)
                .offset( y = if (isPasswordError) 635.dp else 615.dp)
        )
    }
}




