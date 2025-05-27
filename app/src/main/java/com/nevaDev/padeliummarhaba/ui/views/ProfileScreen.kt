package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.viewmodels.DeleteAccountViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ProfileViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetProfileResponse
import kotlinx.coroutines.delay
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: GetProfileViewModel = hiltViewModel(),
    viewModel2: ProfileViewModel = hiltViewModel(),
    sessionManager: SessionManager = SessionManager.getInstance(LocalContext.current),
    viewModel3: DeleteAccountViewModel = hiltViewModel(),
) {
    var activated by remember { mutableStateOf(false) }
    var authorities by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var langKey by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    fun isFormValid(): Boolean {
        return firstName.isNotBlank() && lastName.isNotBlank() && phoneNumber.isNotBlank() && phoneNumber.length >= 8
    }

    LaunchedEffect(showToast) {
        if (showToast) {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            delay(3000)
            showToast = false
        }
    }

    val bitmap = remember(image) {
        if (image.isNotEmpty()) {
            try {
                val decodedString = Base64.decode(image, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            } catch (e: Exception) {
                Log.e("ProfileScreen", "Error decoding image: ${e.message}")
                null
            }
        } else {
            null
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri
        }
    }

    val profileData by viewModel.profileData.observeAsState()

    // Fetch profile data when the composable is first created
    LaunchedEffect(Unit) {
        Log.d("ProfileScreen", "Launching profile fetch...")
        viewModel.fetchProfileData()
    }

    // Handle profile data changes
    LaunchedEffect(profileData) {
        Log.d("ProfileScreen", "Profile data changed: $profileData")

        when (val result = profileData) {
            is DataResultBooking.Success -> {
                Log.d("ProfileScreen", "Success state received")
                try {
                    val profile = result.data
                    Log.d("ProfileScreen", "Profile data: $profile")

                    if (profile != null) {
                        firstName = profile.firstName ?: ""
                        lastName = profile.lastName ?: ""
                        phoneNumber = profile.phone ?: ""
                        activated = profile.activated ?: false
                        authorities = (profile.authorities ?: emptyList()).joinToString(", ")
                        email = profile.email ?: ""
                        langKey = profile.langKey ?: ""
                        login = profile.login ?: ""
                        image = profile.image ?: ""
                        imageUrl = profile.imageUrl ?: ""

                        Log.d("ProfileScreen", "Data populated - FirstName: $firstName, LastName: $lastName, Email: $email")
                    } else {
                        Log.e("ProfileScreen", "Profile is null")
                        toastMessage = "Données de profil invalides"
                        showToast = true
                    }
                } catch (e: Exception) {
                    Log.e("ProfileScreen", "Error processing profile data: ${e.message}", e)
                    toastMessage = "Erreur lors du traitement des données"
                    showToast = true
                }
                isLoading = false
            }
            is DataResultBooking.Loading -> {
                Log.d("ProfileScreen", "Loading state")
                isLoading = true
            }
            is DataResultBooking.Failure -> {
                Log.e("ProfileScreen", "Exception: ${result.exception?.message}")
                isLoading = false
                showToast = true
            }
            null -> {
                Log.d("ProfileScreen", "Profile data is null")
                isLoading = true
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Text(
            text = "Mon Profil",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.offset(x = 5.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
            color = Color.Gray, thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(2.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = if (email.isNotEmpty()) email else "Email non disponible",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = {
                    if ((it.all { char -> char.isDigit() || char == '+' }) && it.length <= 8) {
                        if (it.count { char -> char == '+' } <= 1 && (it.indexOf('+') == 0 || !it.contains('+'))) {
                            phoneNumber = it
                        }
                    }
                },
                label = { Text("Téléphone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )

            Spacer(modifier = Modifier.height(26.dp))

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Photo de profile",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    if (profileImageUri != null) {
                        Image(
                            painter = rememberImagePainter(profileImageUri),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { launcher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )
                    } else if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .clickable { launcher.launch("image/*") },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.a9),
                            contentDescription = "Fallback Avatar",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .clickable { launcher.launch("image/*") }
                                .align(Alignment.Center),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            if (!isFormValid()) {
                                toastMessage = "Veuillez vérifier vos champs"
                                showToast = true
                            } else {
                                val base64Image = profileImageUri?.let { getBase64FromUri(context, it) } ?: image

                                val accountData = mapOf(
                                    "activated" to activated,
                                    "authorities" to listOf(authorities),
                                    "email" to email,
                                    "firstName" to firstName,
                                    "image" to base64Image,
                                    "imageUrl" to imageUrl,
                                    "langKey" to langKey,
                                    "lastName" to lastName,
                                    "login" to login,
                                    "phone" to phoneNumber,
                                )
                                val accountJson = JSONObject(accountData).toString()
                                viewModel2.Profile(accountJson, profileImageUri)
                                toastMessage = "Votre profil a été sauvegardé !"
                                showToast = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF0066CC),
                            contentColor = Color.White
                        ),
                        enabled = isFormValid() && !isLoading,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Sauvegarder",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            sessionManager.logout()
                            navController.navigate("main_screen") {
                                popUpTo("profile") { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(13.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text(
                            text = "Se déconnecter",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                        elevation = ButtonDefaults.elevation(0.dp)
                    ) {
                        Text(
                            text = "Supprimer Compte",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }

                    if (showDeleteDialog) {
                        AlertDialog(
                            onDismissRequest = { showDeleteDialog = false },
                            title = {
                                Text(text = "Suppression du Compte", fontWeight = FontWeight.Bold)
                            },
                            text = {
                                Text("Voulez-vous vraiment supprimer votre compte !!")
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        viewModel3.DeleteAccount(email)
                                        sessionManager.logout()
                                        navController.navigate("main_screen") {
                                            popUpTo("profile") { inclusive = true }
                                        }
                                        toastMessage = "Compte supprimé avec succès."
                                        showToast = true
                                        showDeleteDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                                ) {
                                    Text("Supprimer", color = Color.White)
                                }
                            },
                            dismissButton = {
                                OutlinedButton(
                                    onClick = { showDeleteDialog = false }
                                ) {
                                    Text("Annuler", color = Color.Black)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
fun getBase64FromUri(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
