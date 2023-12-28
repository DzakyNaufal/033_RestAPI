package com.example.consumeapi.navigation

import androidx.compose.runtime.Composable
import com.example.consumeapi.ui.home.viewmodel.HomeViewModel
import com.example.consumeapi.ui.kontak.screen.DestinasiEntry
import com.example.consumeapi.ui.kontak.screen.EntryKontakScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {
    
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        
        composable(DestinasiHome.route) {
            HomeScreen(navigateToItemEntry = {
                navController.navigate(DestinasiEntry.route)
            },
                onDetailClick = {

                })
        }
        composable(DestinasiEntry.route) {
            EntryKontakScreen(navigetBack = {
                navController.navigate(

                ) {
                    popUpTo(DestinasiHome.route){
                        inclusive = true
                    }
                }
            })
        }
    }
}