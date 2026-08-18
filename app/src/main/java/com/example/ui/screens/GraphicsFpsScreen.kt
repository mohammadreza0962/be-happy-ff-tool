package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun GraphicsFpsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val graphicsConfig by viewModel.graphicsConfig.collectAsState()

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Graphics & FPS Tuning",
                subtitle = "${deviceProfile.brand} ${deviceProfile.model}",
                showBackButton = true,
                onBackClick = onNavigateBack
            )
        },
        containerColor = NavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            DeviceProfileHeaderCard(
                device = deviceProfile,
                onChangeDeviceClick = onNavigateToDevicePicker
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Recommendation Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, EmeraldPro.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("graphics_recommendations_summary_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "RECOMMENDED IN-GAME DISPLAY PROFILE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = EmeraldPro
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GraphicsDisplayPill(
                            label = "GRAPHICS",
                            value = graphicsConfig.graphicsLevel,
                            color = AmberFlame,
                            modifier = Modifier.weight(1f)
                        )
                        GraphicsDisplayPill(
                            label = "HIGH FPS",
                            value = graphicsConfig.fpsLevel,
                            color = EmeraldPro,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        GraphicsDisplayPill(
                            label = "SHADOWS",
                            value = graphicsConfig.shadows,
                            color = CyanTech,
                            modifier = Modifier.weight(1f)
                        )
                        GraphicsDisplayPill(
                            label = "HIGH RES",
                            value = graphicsConfig.highRes,
                            color = PurpleNeon,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = graphicsConfig.thermalAdvice,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.applyGraphicsSettings(graphicsConfig.graphicsLevel, graphicsConfig.fpsLevel) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldPro,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("apply_graphics_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPLY & SHOW FREE FIRE DISPLAY GUIDE",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Battery vs Performance Analysis
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HARDWARE & THERMAL TRADE-OFF ANALYSIS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AmberFlameLight
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TradeOffItem(
                        icon = Icons.Default.BatteryChargingFull,
                        title = "Battery Consumption",
                        description = if (deviceProfile.ram.replace("GB", "").toIntOrNull() ?: 6 <= 4)
                            "Smooth graphics conserves up to 35% more battery per hour during continuous ranked play."
                        else "High FPS delivers the fastest aim tracking with moderate battery draw.",
                        color = EmeraldPro
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TradeOffItem(
                        icon = Icons.Default.Thermostat,
                        title = "Thermal Throttling Protection",
                        description = "Keeping Shadows OFF prevents GPU thermal throttling, ensuring you do not experience sudden frame dips during close-range shotgun fights.",
                        color = AmberFlame
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TradeOffItem(
                        icon = Icons.Default.TouchApp,
                        title = "Touch Latency Optimization",
                        description = "Enabling High FPS reduces input lag by up to 16ms, making your vertical drag headshots connect instantly.",
                        color = CyanTech
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GraphicsDisplayPill(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                ),
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                ),
                color = color
            )
        }
    }
}

@Composable
fun TradeOffItem(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}
