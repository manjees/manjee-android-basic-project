package com.manjee.manjeebasicapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.manjee.manjeebasicapp.R

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.home_welcome_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(id = R.string.home_welcome_subtitle),
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
            text = stringResource(id = R.string.discover_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(id = R.string.discover_subtitle),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
