package com.example.marketapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.marketapp.design.theme.VKTestMarketTheme
import com.example.marketapp.presentation.catalog.CatalogDestination
import com.example.marketapp.presentation.catalog.catalogNavGraph
import com.example.marketapp.presentation.details.ProductDetailsDestination
import com.example.marketapp.presentation.details.ProductsDetailsScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKTestMarketTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = CatalogDestination,
    ) {
        catalogNavGraph(navController)

        composable<ProductDetailsDestination> { navBackStackEntry ->
            val productId = navBackStackEntry
                .toRoute<ProductDetailsDestination>().id
            ProductsDetailsScreen(productId = productId)
        }
    }
}

@Serializable
data object TestDestination

fun NavHostController.navigateSingleTopTo(route: Any) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
    }
