package com.example.darkhumor.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Blacklist Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Select categories to blacklist from jokes:", style = MaterialTheme.typography.bodyLarge)

            SettingsToggle("NSFW", preferences.nsfw) { viewModel.updateNsfw(it) }
            SettingsToggle("Religious", preferences.religious) { viewModel.updateReligious(it) }
            SettingsToggle("Political", preferences.political) { viewModel.updatePolitical(it) }
            SettingsToggle("Racist", preferences.racist) { viewModel.updateRacist(it) }
            SettingsToggle("Sexist", preferences.sexist) { viewModel.updateSextist(it) }
            SettingsToggle("Explicit", preferences.explicit) { viewModel.updateExplicit(it) }
        }
    }
}

@Composable
fun SettingsToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
