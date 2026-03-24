package com.ishaan.quizhive.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.FlowRow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(navController: NavController) {

    var selectedCategory by remember { mutableStateOf<Int?>(null) }
    var selectedDifficulty by remember { mutableStateOf<String?>(null) }

    val categories = mapOf(
        "Any Category" to null,
        "Science" to 17,
        "History" to 23,
        "Sports" to 21,
        "Geography" to 22,
        "Movies" to 11,
        "Music" to 12
    )

    val difficulties = listOf("Any", "Easy", "Medium", "Hard")

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 🔥 App Title Section
            Text(
                "QuizHive",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                "Let's test your knowledge",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🎯 Main Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {

                    // 📚 CATEGORY
                    Text(
                        "Category",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    var categoryExpanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = categories.entries.find { it.value == selectedCategory }?.key
                                ?: "Any Category",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Category") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categories.forEach { (name, id) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedCategory = id
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // ⚡ DIFFICULTY
                    Text(
                        "Difficulty",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(10.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        difficulties.forEach { diff ->
                            val value = if (diff == "Any") null else diff

                            FilterChip(
                                selected = selectedDifficulty == value,
                                onClick = {
                                    selectedDifficulty = value
                                },
                                label = {
                                    Text(diff)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 🚀 START BUTTON
            Button(
                onClick = {
                    val categoryArg = selectedCategory ?: -1
                    val difficultyArg = selectedDifficulty ?: "any"
                    navController.navigate("quiz/$categoryArg/$difficultyArg")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Start Quiz",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}