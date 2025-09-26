package com.manjee.manjeebasicapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.manjee.manjeebasicapp.ui.book.BookDetailScreen
import com.manjee.manjeebasicapp.ui.book.BookListScreen
import com.manjee.manjeebasicapp.ui.library.LibraryScreen

object Routes {
    const val HOME = "home"
    const val DISCOVER = "discover"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val BOOK_LIST = "book_list"
    const val BOOK_DETAIL = "book_detail/{bookId}"

    fun getBookDetailPath(bookId: String) = "book_detail/$bookId"
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem(route = Routes.HOME, label = "Home", icon = Icons.Filled.Home),
        BottomNavItem(route = Routes.DISCOVER, label = "Discover", icon = Icons.Filled.Person),
        BottomNavItem(route = Routes.LIBRARY, label = "Favorites", icon = Icons.Filled.Person),
        BottomNavItem(route = Routes.PROFILE, label = "Profile", icon = Icons.Filled.Person)
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    val bottomDestinations = bottomNavItems.map { it.route }
    val showBottomBar = currentDestination?.hierarchy?.any { it.route in bottomDestinations } == true ||
        currentRoute == Routes.BOOK_LIST

    Scaffold(
        topBar = {
            if (currentRoute == Routes.HOME) {
                TopAppBar(
                    title = { Text("Home") },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Routes.BOOK_LIST) {
                                launchSingleTop = true
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search books")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = when {
                            currentRoute == Routes.BOOK_LIST && item.route == Routes.HOME -> true
                            else -> currentDestination?.hierarchy?.any { it.route == item.route } == true
                        }
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen()
            }
            composable(Routes.DISCOVER) {
                DiscoverScreen()
            }
            composable(Routes.LIBRARY) {
                LibraryScreen(navController = navController)
            }
            composable(Routes.PROFILE) {
                ProfileScreen()
            }
            composable(Routes.BOOK_LIST) {
                BookListScreen(navController = navController)
            }
            composable(
                route = Routes.BOOK_DETAIL,
                arguments = listOf(navArgument("bookId") { type = NavType.StringType })
            ) {
                BookDetailScreen(navController = navController)
            }
        }
    }
}
