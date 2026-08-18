package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.data.DeviceDatabase
import com.example.model.DeviceProfile
import com.example.model.PerformanceProfile
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun DeviceLibraryScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val activeProfile by viewModel.deviceProfile.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedBrand by viewModel.selectedBrandFilter.collectAsState()
    val selectedRam by viewModel.selectedRamFilter.collectAsState()
    val filteredDevices by viewModel.filteredDevices.collectAsState()

    val brands = listOf("All", "Samsung", "Xiaomi", "POCO", "Redmi", "Realme", "Infinix", "Tecno", "ASUS ROG", "OnePlus", "Vivo", "iQOO", "Motorola", "Google")
    val ramOptions = listOf("All", "2GB", "3GB", "4GB", "6GB", "8GB", "12GB", "16GB")

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Device Database",
                subtitle = "${filteredDevices.size} Android Devices Available",
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
            // Search Field
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setDeviceSearchQuery(it) },
                    placeholder = { Text("Search brand or model (e.g. Galaxy A15, POCO X6)...", color = TextMuted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AmberFlame) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setDeviceSearchQuery("") }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextSecondary)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = AmberFlame,
                        unfocusedBorderColor = NavyCardBorder,
                        focusedContainerColor = NavySurface,
                        unfocusedContainerColor = NavySurface
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("device_search_input")
                )
            }

            // Brand Filter Chips
            item {
                Column {
                    Text(
                        text = "FILTER BY BRAND",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AmberFlameLight
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        brands.forEach { brand ->
                            val isSelected = selectedBrand == brand
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setBrandFilter(brand) },
                                label = { Text(brand, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
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
                                modifier = Modifier.testTag("brand_chip_$brand")
                            )
                        }
                    }
                }
            }

            // RAM Filter Chips
            item {
                Column {
                    Text(
                        text = "FILTER BY RAM CAPACITY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = CyanTech
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ramOptions.forEach { ram ->
                            val isSelected = selectedRam == ram
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setRamFilter(ram) },
                                label = { Text(ram, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanTech,
                                    selectedLabelColor = NavyBackground,
                                    containerColor = NavySurface,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = if (isSelected) CyanTech else NavyCardBorder,
                                    selectedBorderColor = CyanTech,
                                    enabled = true,
                                    selected = isSelected
                                ),
                                modifier = Modifier.testTag("ram_chip_$ram")
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "AVAILABLE DEVICES (${filteredDevices.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }

            // Device Cards List
            items(filteredDevices, key = { "${it.brand}_${it.model}_${it.ram}" }) { device ->
                val isActive = activeProfile.brand == device.brand && activeProfile.model == device.model && activeProfile.ram == device.ram

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            1.dp,
                            if (isActive) AmberFlame else NavyCardBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .testTag("device_card_${device.brand}_${device.model}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) NavySurfaceVariant else NavySurface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isActive) AmberFlame.copy(alpha = 0.2f) else NavyCardBorder.copy(alpha = 0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = if (isActive) AmberFlame else TextSecondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "${device.brand} ${device.model}",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                if (isActive) {
                                    Surface(
                                        color = AmberFlame.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = AmberFlameLight
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(3.dp))

                            Text(
                                text = "${device.ram} RAM • ${device.performanceProfile.displayName} Profile • ${device.touchSamplingHz}Hz Touch • ${device.defaultDpi} DPI",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = TextSecondary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Button(
                                onClick = { viewModel.selectDevice(device) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isActive) EmeraldPro else AmberFlame,
                                    contentColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("select_device_${device.model.replace(" ", "_")}")
                            ) {
                                Text(
                                    text = if (isActive) "Applied" else "Select",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            IconButton(
                                onClick = { viewModel.saveDeviceFavorite(device) },
                                modifier = Modifier.size(28.dp).testTag("fav_device_${device.model.replace(" ", "_")}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkAdd,
                                    contentDescription = "Save to Favorites",
                                    tint = TextMuted,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
