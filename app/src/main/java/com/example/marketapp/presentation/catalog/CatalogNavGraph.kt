package com.example.marketapp.presentation.catalog

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.marketapp.navigateSingleTopTo
import com.example.marketapp.presentation.catalog.categories.CategoriesDestination
import com.example.marketapp.presentation.catalog.categories.CategoriesScreen
import com.example.marketapp.presentation.catalog.categories.CategoriesViewModel
import com.example.marketapp.presentation.catalog.produtslist.ProductsListDestination
import com.example.marketapp.presentation.catalog.produtslist.ProductsListScreen
import com.example.marketapp.presentation.details.ProductDetailsDestination
import com.example.marketapp.sharedViewModel

fun NavGraphBuilder.catalogNavGraph(
    navController: NavHostController,
) = navigation<CatalogDestination>(
    startDestination = ProductsListDestination,
) {
    composable<ProductsListDestination> { navBackStackEntry ->
        val categoriesViewModel = navBackStackEntry
            .sharedViewModel<CategoriesViewModel>(navController)
        ProductsListScreen(
            categoriesViewModel = categoriesViewModel,
            onProductClick = { productId ->
                navController.navigateSingleTopTo(
                    ProductDetailsDestination(productId)
                )
            },
            onSelectCategoriesClick = {
                navController.navigateSingleTopTo(
                    CategoriesDestination
                )
            },
        )
    }
    composable<CategoriesDestination> { navBackStackEntry ->
        val categoriesViewModel = navBackStackEntry
                .sharedViewModel<CategoriesViewModel>(navController)
        CategoriesScreen(
            onApplyClick = { navController.popBackStack() },
            categoriesViewModel = categoriesViewModel,
        )
    }
}