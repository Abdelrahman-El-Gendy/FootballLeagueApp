package com.example.footballleagueapp

import AreaWithCompetitions
import Competition
import android.R.attr.onClick
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.footballleagueapp.CompetitionItem


@Composable
fun HomeScreen(
    viewModel: FootballViewModel = viewModel(),
    onCompetitionClick: (Competition) -> Unit
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is UiState.Loading -> FullScreenLoading()
        is UiState.Error -> ErrorScreen(
            message = (state as UiState.Error).message,
            onRetry = { viewModel.fetchCompetitions() }
        )
        is UiState.Success -> {
            val areas = (state as UiState.Success).areas
            AreaExpandableList(
                areas = areas,
                onCompetitionClick = onCompetitionClick
            )
        }
    }
}

@Composable
fun ExpandableAreaItem(
    area: AreaWithCompetitions,
    onCompetitionClick: (Competition) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Area header (only for expanding/collapsing)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = area.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Code: ${area.code}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }

            // Competitions list - these are clickable
            AnimatedVisibility(visible = expanded) {
                Column {
                    area.competitions.forEach { competition ->
                        CompetitionItem(
                            competition = competition,
                            onClick = { onCompetitionClick(competition) } // Pass the click handler
                        )
                        Divider(thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorScreen(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
fun AreaExpandableList(
    areas: List<AreaWithCompetitions>,
    onCompetitionClick: (Competition) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(areas) { area ->
            ExpandableAreaItem(
                area = area,
                onCompetitionClick = onCompetitionClick
            )
        }
    }
}

@Composable
fun CompetitionItem(
    competition: Competition,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // This makes the whole item clickable
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = competition.emblem,
                contentDescription = "${competition.name} emblem",
                modifier = Modifier.size(40.dp),
                placeholder = painterResource(R.drawable.ic_launcher_foreground)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = competition.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = competition.type.replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}