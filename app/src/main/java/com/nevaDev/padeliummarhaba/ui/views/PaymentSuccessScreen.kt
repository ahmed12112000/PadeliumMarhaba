package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PaymentSuccessScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F9E6))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Succès des paiements",
                tint = Color(0xFF28A745),
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Succès des paiements!",
                style = MaterialTheme.typography.h4.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Votre paiement a été traité avec succès.",
                style = MaterialTheme.typography.body1.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("main_screen") },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),

                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Accueil",color = White)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PaymentSuccessScreenPreview() {
    val mockNavController = rememberNavController()
    PaymentSuccessScreen(navController = mockNavController)
}
