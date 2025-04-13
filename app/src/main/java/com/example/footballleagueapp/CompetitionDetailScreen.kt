package com.example.footballleagueapp

import Competition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompetitionDetailScreen(
    competition: Competition?,
    onBack: () -> Unit
) {
    if (competition == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Competition details not available")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Go Back")
                }
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(competition.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = competition.emblem,
                        contentDescription = "Competition emblem",
                        modifier = Modifier.size(64.dp),
                        placeholder = painterResource(R.drawable.ic_launcher_foreground)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = competition.name,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "${competition.area.name} (${competition.area.code})",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Divider()
            }

            // Display all competition details
            competition.currentSeason?.let { season ->
                item {
                    DetailSection("Current Season") {
                        Text("${season.startDate} to ${season.endDate}")
                        season.currentMatchday?.let {
                            Text("Current matchday: $it")
                        }
                        season.winner?.let { winner ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                winner.crest?.let { crestUrl ->
                                    AsyncImage(
                                        model = crestUrl,
                                        contentDescription = "Winner crest",
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                                Text("Winner: ${winner.name ?: "TBD"}")
                            }
                        }
                    }
                }
            }

            item {
                DetailSection("Additional Information") {
                    Text("ID: ${competition.id}")
                    competition.code?.let { code ->
                        Text("Code: $code")
                    }
                    Text("Type: ${competition.type.replaceFirstChar { it.uppercase() }}")
                }
            }
        }
    }
}

@Composable
fun DetailSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
fun CompetitionDetailContent(
    competition: Competition,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = competition.emblem,
                    contentDescription = "${competition.name} emblem",
                    modifier = Modifier.size(64.dp),
                    placeholder = painterResource(R.drawable.ic_launcher_background)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = competition.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "${competition.area.name} • ${competition.type.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Area Information
        item {
            DetailSection("Area Information") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    competition.area.flag?.let { flagUrl ->
                        AsyncImage(
                            model = flagUrl,
                            contentDescription = "${competition.area.name} flag",
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("${competition.area.name} (${competition.area.code})")
                }
            }
        }

        // Current Season
        competition.currentSeason?.let { season ->
            item {
                DetailSection("Current Season") {
                    Text("${season.startDate} - ${season.endDate}")
                    season.currentMatchday?.let {
                        Text("Current match day: $it")
                    }
                    season.winner?.let { winner ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            winner.crest?.let { crestUrl ->
                                AsyncImage(
                                    model = crestUrl,
                                    contentDescription = "${winner.name} crest",
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text("Winner: ${winner.name ?: "TBD"}")
                        }
                    }
                }
            }
        }

        // Additional Info
        item {
            DetailSection("Additional Information") {
                Text("ID: ${competition.id}")
                competition.code?.let { code ->
                    Text("Code: $code")
                }
            }
        }
    }
}

/**
@Composable
private fun CompetitionExpandableList(areas: List<AreaWithCompetitions>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(areas) { area ->
            ExpandableAreaItem(area = area)
        }
    }
}
@Composable
private fun ExpandableAreaItem(area: AreaWithCompetitions) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = area.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Code: ${area.code}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    area.competitions.forEach { competition ->
                        CompetitionItem(competition = competition)
                        Divider(thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}
@Composable
 fun CompetitionItem(competition: Competition) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = competition.emblem,
            contentDescription = "${competition.name} emblem",
            modifier = Modifier.size(40.dp),
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = competition.name,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = competition.type.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
@Composable
fun CompetitionCard(competition: Competition) {
Card(
modifier = Modifier.fillMaxWidth(),
elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
) {
Column(modifier = Modifier.padding(16.dp)) {
// Competition header
Row(verticalAlignment = Alignment.CenterVertically) {
AsyncImage(
model = competition.emblem,
contentDescription = "${competition.name} emblem",
modifier = Modifier.size(48.dp),
placeholder = painterResource(R.drawable.ic_launcher_foreground)
)
Spacer(modifier = Modifier.width(16.dp))
Column(modifier = Modifier.weight(1f)) {
Text(
text = competition.name,
style = MaterialTheme.typography.titleLarge,
fontWeight = FontWeight.Bold
)
Text(
text = "Type: ${competition.type.replaceFirstChar { it.uppercase() }}",
style = MaterialTheme.typography.bodyMedium
)
}
}

Spacer(modifier = Modifier.height(16.dp))

// Area information
Text(
text = "Area:",
style = MaterialTheme.typography.labelLarge,
color = MaterialTheme.colorScheme.primary
)
Row(
modifier = Modifier.padding(start = 8.dp, top = 4.dp),
verticalAlignment = Alignment.CenterVertically
) {
competition.area.flag?.let { flagUrl ->
AsyncImage(
model = flagUrl,
contentDescription = "${competition.area.name} flag",
modifier = Modifier.size(24.dp)
)
Spacer(modifier = Modifier.width(8.dp))
}
Text(
text = "${competition.area.name} (${competition.area.code})",
style = MaterialTheme.typography.bodyMedium
)
}

// Current season (if available)
competition.currentSeason?.let { season ->
Spacer(modifier = Modifier.height(8.dp))
Text(
text = "Current Season:",
style = MaterialTheme.typography.labelLarge,
color = MaterialTheme.colorScheme.primary
)
Column(modifier = Modifier.padding(start = 8.dp, top = 4.dp)) {
Text(text = "Period: ${season.startDate} to ${season.endDate}")
season.currentMatchday?.let {
Text(text = "Matchday: $it")
}
season.winner?.let { winner ->
Row(verticalAlignment = Alignment.CenterVertically) {
winner.crest?.let { crestUrl ->
AsyncImage(
model = crestUrl,
contentDescription = "${winner.name} crest",
modifier = Modifier.size(24.dp)
)
Spacer(modifier = Modifier.width(8.dp))
}
Text(text = "Winner: ${winner.name ?: "TBD"}")
}
}
}
}

// Additional info
Spacer(modifier = Modifier.height(8.dp))
Text(
text = "ID: ${competition.id}",
style = MaterialTheme.typography.bodySmall,
color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
)
competition.code?.let {
Text(
text = "Code: $it",
style = MaterialTheme.typography.bodySmall,
color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
)
}
}
}
}
 **/