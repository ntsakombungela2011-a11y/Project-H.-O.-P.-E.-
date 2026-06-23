package com.example.darkhumor.ui.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.darkhumor.domain.model.Joke
import com.example.darkhumor.util.ShakeDetector
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    val shakeDetector = remember { ShakeDetector { viewModel.getJoke() } }

    DisposableEffect(Unit) {
        sensorManager.registerListener(shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_UI)
        onDispose {
            sensorManager.unregisterListener(shakeDetector)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (val state = uiState) {
                is MainUiState.Empty -> {
                    Button(onClick = { viewModel.getJoke() }) {
                        Text("Get Joke")
                    }
                }
                is MainUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is MainUiState.Success -> {
                    JokeDisplay(
                        joke = state.joke,
                        isFavorite = isFavorite,
                        onToggleFavorite = { viewModel.toggleFavorite(state.joke) },
                        onShare = { shareJoke(context, state.joke) },
                        onCopy = {
                            copyJokeToClipboard(context, state.joke)
                            scope.launch {
                                snackbarHostState.showSnackbar("Copied to clipboard")
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { viewModel.getJoke() }) {
                        Text("Get Another Joke")
                    }
                }
                is MainUiState.Error -> {
                    Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.getJoke() }) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}

@Composable
fun JokeDisplay(
    joke: Joke,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onShare: () -> Unit,
    onCopy: () -> Unit
) {
    var showDelivery by remember(joke.id) { mutableStateOf(false) }

    LaunchedEffect(joke.id) {
        if (joke.type == "twopart") {
            delay(3000)
            showDelivery = true
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (joke.type == "single") {
                Text(
                    text = joke.joke ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = joke.setup ?: "",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                AnimatedVisibility(
                    visible = showDelivery,
                    enter = fadeIn() + expandVertically()
                ) {
                    Text(
                        text = joke.delivery ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (!showDelivery) {
                    TextButton(onClick = { showDelivery = true }) {
                        Text("Show Reveal")
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onCopy) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(onClick = onShare) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
            }
        }
    }
}

private fun copyJokeToClipboard(context: Context, joke: Joke) {
    val text = if (joke.type == "single") {
        joke.joke ?: ""
    } else {
        "${joke.setup}\n\n${joke.delivery}"
    }
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("joke", text)
    clipboard.setPrimaryClip(clip)
}

private fun shareJoke(context: Context, joke: Joke) {
    val text = if (joke.type == "single") {
        joke.joke ?: ""
    } else {
        "${joke.setup}\n\n${joke.delivery}"
    }
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}
