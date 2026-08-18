package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.model.FavoriteItem
import com.example.navigation.NavRoutes
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun FavoritesScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToRoute: (String) -> Unit
) {
    val favorites by viewModel.favorites.collectAsState()
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    var renameTarget by remember { mutableStateOf<FavoriteItem?>(null) }
    var renameText by remember { mutableStateOf("") }

    if (renameTarget != null) {
        AlertDialog(
            onDismissRequest = { renameTarget = null },
            containerColor = NavySurface,
            title = {
                Text(
                    text = "Rename Favorite",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                OutlinedTextField(
                    value = renameText,
                    onValueChange = { renameText = it },
                    label = { Text("New Name", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberFlame,
                        unfocusedBorderColor = NavyCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("rename_input_field")
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        renameTarget?.let { viewModel.renameFavorite(it.id, renameText) }
                        renameTarget = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberFlame, contentColor = TextPrimary),
                    modifier = Modifier.testTag("confirm_rename_button")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { renameTarget = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    val filteredFavorites = if (selectedCategoryFilter == "ALL") favorites else favorites.filter { it.category == selectedCategoryFilter }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Saved Favorites",
                subtitle = "${favorites.size} Presets & Layouts Stored Locally",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = NavyBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Category Filter Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ALL", "SENSITIVITY", "HUD", "DEVICE").forEach { cat ->
                        val isSelected = selectedCategoryFilter == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = cat },
                            label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberFlame,
                                selectedLabelColor = TextPrimary,
                                containerColor = NavySurface,
                                labelColor = TextSecondary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (isSelected) AmberFlame else NavyCardBorder,
                                selectedBorderColor = AmberFlame,
                                enabled = true,
                                selected = isSelected
                            ),
                            modifier = Modifier.testTag("filter_chip_$cat")
                        )
                    }
                }
            }

            if (filteredFavorites.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        colors = CardDefaults.cardColors(containerColor = NavySurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No Saved Favorites Yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Save your favorite Sensitivity Presets, Custom HUDs, and Device configurations to load them instantly.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredFavorites, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, NavyCardBorder, RoundedCornerShape(12.dp))
                            .testTag("favorite_card_${item.id}"),
                        colors = CardDefaults.cardColors(containerColor = NavySurface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        color = when (item.category) {
                                            "SENSITIVITY" -> AmberFlame.copy(alpha = 0.2f)
                                            "HUD" -> PurpleNeon.copy(alpha = 0.2f)
                                            else -> CyanTech.copy(alpha = 0.2f)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = item.category,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = when (item.category) {
                                                "SENSITIVITY" -> AmberFlameLight
                                                "HUD" -> PurpleNeon
                                                else -> CyanTech
                                            }
                                        )
                                    }

                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextPrimary
                                    )
                                }

                                Row {
                                    IconButton(
                                        onClick = {
                                            renameTarget = item
                                            renameText = item.name
                                        },
                                        modifier = Modifier.size(32.dp).testTag("rename_favorite_${item.id}")
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Rename", tint = TextSecondary, modifier = Modifier.size(16.dp))
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteFavorite(item) },
                                        modifier = Modifier.size(32.dp).testTag("delete_favorite_${item.id}")
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DangerRed, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = item.summary,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val dateStr = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(item.timestamp))
                                Text(
                                    text = "${item.deviceName} • $dateStr",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = TextMuted
                                )

                                Button(
                                    onClick = {
                                        viewModel.loadFavorite(item)
                                        when (item.category) {
                                            "SENSITIVITY" -> onNavigateToRoute(NavRoutes.SENSITIVITY)
                                            "HUD" -> onNavigateToRoute(NavRoutes.HUD_STUDIO)
                                            "DEVICE" -> onNavigateToRoute(NavRoutes.DEVICE_LIBRARY)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = AmberFlame,
                                        contentColor = TextPrimary
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.testTag("apply_favorite_${item.id}")
                                ) {
                                    Text("Load Preset", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
