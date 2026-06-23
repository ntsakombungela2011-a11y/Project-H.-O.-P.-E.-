package com.example.darkhumor.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.darkhumor.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val preferences by viewModel.preferences.collectAsState()
    val currentTheme by viewModel.currentTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Theme Selection Section
            Text("App Theme", style = MaterialTheme.typography.titleMedium)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(AppTheme.entries.toList()) { theme ->
                    ThemeColorItem(
                        theme = theme,
                        isSelected = theme == currentTheme,
                        onClick = { viewModel.setTheme(theme) }
                    )
                }
            }

            HorizontalDivider()

            // Joke Categories Section
            Text("Joke Categories", style = MaterialTheme.typography.titleMedium)
            Text(
                "Select multiple categories to fetch jokes from:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.availableCategories.forEach { category ->
                    SettingsToggle(
                        label = category,
                        checked = preferences.selectedCategories.contains(category),
                        onCheckedChange = { viewModel.toggleCategory(category) }
                    )
                }
            }

            HorizontalDivider()

            // Blacklist Settings Section
            Text("Blacklist Settings", style = MaterialTheme.typography.titleMedium)
            Text(
                "Select categories to blacklist from jokes:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

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
fun ThemeColorItem(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(theme.primaryColor)
                .then(
                    if (isSelected) {
                        Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    } else {
                        Modifier
                    }
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = theme.displayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
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
