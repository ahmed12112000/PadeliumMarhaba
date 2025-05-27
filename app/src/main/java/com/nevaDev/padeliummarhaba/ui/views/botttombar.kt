package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.compiler.plugins.kotlin.ComposeCallableIds.remember
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MobileNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val navItems = listOf(
        NavItemm("Home", "home", Icons.Default.Home),
        NavItemm("Search", "search", Icons.Default.Search),
        NavItemm("Store", "store", Icons.Default.Store, highlight = true),
        NavItemm("Cart", "cart", Icons.Default.ShoppingCart),
        NavItemm("Profile", "profile", Icons.Default.Person)
    )

    Box {
        BottomNavigation(
            backgroundColor = Color.White,
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            navItems.forEachIndexed { index, item ->
                if (item.highlight) {
                    Spacer(modifier = Modifier.weight(1f))
                } else {
                    BottomNavigationItem(
                        selected = currentRoute == item.route,
                        onClick = { onNavigate(item.route) },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name,
                                    tint = if (currentRoute == item.route) Color(0xFF386BF6) else Color(0xFF9DB2CE)
                                )
                                Text(
                                    text = item.name,
                                    fontSize = 10.sp,
                                    color = if (currentRoute == item.route) Color(0xFF386BF6) else Color(0xFF9DB2CE)
                                )
                            }
                        },
                        selectedContentColor = Color(0xFF386BF6),
                        unselectedContentColor = Color(0xFF9DB2CE)
                    )
                }
            }
        }

        // Floating Highlighted Button (like Store icon)
        val highlightedItem = navItems.first { it.highlight }
        FloatingActionButton(
            onClick = { onNavigate(highlightedItem.route) },
            backgroundColor = Color(0xFF613EEA),
            contentColor = Color.White,
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.BottomCenter)
                .offset(y = (-32).dp),
            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
        ) {
            Icon(imageVector = highlightedItem.icon, contentDescription = highlightedItem.name)
        }
    }
}
@Preview(showBackground = true)
@Composable
fun MobileNavigationBarPreview() {
    var currentRoute by remember { mutableStateOf("home") }

    MobileNavigationBar(currentRoute = currentRoute) { selectedRoute ->
        currentRoute = selectedRoute
    }
}

data class NavItemm(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val highlight: Boolean = false
)
