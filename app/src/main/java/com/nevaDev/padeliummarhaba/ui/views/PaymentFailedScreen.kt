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
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController



@Composable
fun PaymentFailedScreen(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE5E5))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Échec du paiement",
                tint = Color(0xFFDC3545),
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Échec du paiement!",
                style = MaterialTheme.typography.h4.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Votre paiement n'a pas pu être traité. Veuillez réessayer.",
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
                Text(text = "Retour à l'accueil", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentFailedScreenPreview() {
    val mockNavController = rememberNavController()
    PaymentFailedScreen(navController = mockNavController)
}
