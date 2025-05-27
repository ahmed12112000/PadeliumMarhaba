import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.ui.views.CopyrightText
import com.nevadev.padeliummarhaba.R


@Composable
fun DrawerContent(navController: NavController, onItemSelected: (String) -> Unit, onCloseDrawer: () -> Unit)
{

    Column(
        modifier = Modifier
            .offset(x = -40.dp)
            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .background(Color(0xFF0054D8))
    ) {
        IconButton(
            onClick = { onCloseDrawer() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.oip),
                contentDescription = "Close Drawer",
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopadelium),
                contentDescription = "Padelium Logo",
                modifier = Modifier.size(260.dp)
                    .offset(y = -90.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -90.dp, x= 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Spacer(modifier = Modifier.width(8.dp))

                Column {


                    Row {
                        Text(
                            text = "Se connecter",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("login_screen")
                                }
                        )
                        Text(
                            text = " ou ",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "S'inscrire",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable {
                                    try {
                                        navController.navigate("signup_screen")
                                    } catch (e: Exception) {
                                        Log.e("NavigationError", "Failed to navigate: ${e.message}")
                                    }
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(thickness = 2.dp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .offset(y = -80.dp, x= 20.dp)
                .weight(1f)
        ) {
            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Accueil",
                onClick = {
                    navController.navigate("main_screen")
                }
            )


            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Plans Tarifaires",
                onClick = {
                    navController.navigate("pricing_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Qui sommes nous?",
                onClick = {
                    navController.navigate("how_to_use_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "CGV",
                onClick = {
                    navController.navigate("about_us_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Contactez-nous",
                onClick = {
                    navController.navigate("contact_screen")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.a91),
            contentDescription = "Additional Image",
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
                .offset(y = -55.dp, x = -141.dp)
        )

        HorizontalDivider(modifier = Modifier .fillMaxWidth().offset(y = -10.dp),
            color = Color.White, thickness = 1.5.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "v1.0.0",
                    fontSize = 10.sp,
                    color = Color.White
                )


                CopyrightText()
            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: Int,
    label: String,
    onClick: () -> Unit,
    abelColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }

    Divider(color = Color.White, thickness = 1.dp)
}
