package com.manjee.manjeebasicapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.manjee.manjeebasicapp.ui.book.BookDetailScreen
import com.manjee.manjeebasicapp.ui.book.BookListScreen

object Routes {
    const val BOOK_LIST = "book_list"
    const val BOOK_DETAIL = "book_detail/{bookId}"

    fun getBookDetailPath(bookId: String) = "book_detail/$bookId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.BOOK_LIST) {
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
