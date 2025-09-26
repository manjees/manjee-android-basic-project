package com.manjee.manjeebasicapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Manjee",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Use the search icon to browse books",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun DiscoverScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Discover",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Coming soon",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Configure your account settings",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
