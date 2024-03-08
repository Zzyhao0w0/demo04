package com.example.demo04.ui.component.itinerary

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demo04.ui.component.login.TopBar

@Composable
fun ItineraryListDialog(
    onDismissRequest: () -> Unit = {}
) {
    val navController = rememberNavController()

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // experimental
        ),
    ) {
        Scaffold (
            topBar = { TopBar(title = "行程")},
            bottomBar = {
                BottomAppBar {
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("usedCar") },
                        icon = { Icon(Icons.Filled.LocationOn, contentDescription = "locationOn")},
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate("order")  },
                        icon = { Icon(Icons.Filled.Build, contentDescription = "build") }
                    )
                }
            }
        ) { paddingvalues->
            NavHost(navController = navController, startDestination = "usedCar") {
                composable("usedCar"){
                    UsedCarItineraryList(paddingvalues.calculateTopPadding(),navController=navController)
                }
                composable("order"){
                    OrderItineraryList(
                        topPadding = paddingvalues.calculateTopPadding(),
                        navController = navController
                    )
                }
            }
        }
    }
}


