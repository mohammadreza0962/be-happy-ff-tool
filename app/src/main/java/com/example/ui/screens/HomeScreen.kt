package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeviceProfile
import com.example.navigation.NavRoutes
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.components.SafetyDisclaimerCard
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

data class HomeFeatureCard(
    val route: String,
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val accentColor: Color
)

@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onNavigate: (String) -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val trainingState by viewModel.trainingState.collectAsState()
    val optimizationState by viewModel.optimizationState.collectAsState()

    val features = listOf(
        HomeFeatureCard(
            route = NavRoutes.SENSITIVITY,
            title = "Sensitivity",
            subtitle = "General, Red Dot & Scope presets",
            badge = "Pro Drag",
            icon = Icons.Default.Tune,
            accentColor = AmberFlame
        ),
        HomeFeatureCard(
            route = NavRoutes.FIRE_BUTTON,
            title = "Fire Button",
            subtitle = "Size % & upward drag calibration",
            badge = "Essential",
            icon = Icons.Default.RadioButtonChecked,
            accentColor = AmberFlameLight
        ),
        HomeFeatureCard(
            route = NavRoutes.DPI,
            title = "DPI Guidance",
            subtitle = "Smallest width tuning & safe limits",
            badge = "Precision",
            icon = Icons.Default.AspectRatio,
            accentColor = CyanTech
        ),
        HomeFeatureCard(
            route = NavRoutes.GRAPHICS,
            title = "Graphics & FPS",
            subtitle = "60/90/120 FPS display profiles",
            badge = "Stability",
            icon = Icons.Default.Speed,
            accentColor = EmeraldPro
        ),
        HomeFeatureCard(
            route = NavRoutes.HUD_STUDIO,
            title = "Custom HUD",
            subtitle = "2, 3, 4 & 5-Finger visual layout studio",
            badge = "Interactive",
            icon = Icons.Default.Dashboard,
            accentColor = PurpleNeon
        ),
        HomeFeatureCard(
            route = NavRoutes.OPTIMIZATION,
            title = "Optimization",
            subtitle = "${optimizationState.completedCount}/8 steps done • Boost stability",
            badge = if (optimizationState.completedCount == 8) "100% Ready" else "Checklist",
            icon = Icons.Default.Bolt,
            accentColor = if (optimizationState.completedCount == 8) EmeraldPro else AmberFlame
        ),
        HomeFeatureCard(
            route = NavRoutes.TRAINING,
            title = "Headshot Training",
            subtitle = "${trainingState.totalRoundsCompleted}/3 rounds • ${trainingState.streakDays}d streak",
            badge = "Practice",
            icon = Icons.Default.FitnessCenter,
            accentColor = AmberFlame
        ),
        HomeFeatureCard(
            route = NavRoutes.GYROSCOPE,
            title = "Gyroscope",
            subtitle = "Tilt aim & calibration values",
            badge = "Aim Assist",
            icon = Icons.Default.ScreenRotation,
            accentColor = CyanTech
        ),
        HomeFeatureCard(
            route = NavRoutes.DEVICE_LIBRARY,
            title = "Device Library",
            subtitle = "50+ Android phones & RAM configs",
            badge = "50+ Phones",
            icon = Icons.Default.DevicesOther,
            accentColor = PurpleNeon
        ),
        HomeFeatureCard(
            route = NavRoutes.FAVORITES,
            title = "Favorites",
            subtitle = "Saved presets, HUDs & devices",
            badge = "Saved",
            icon = Icons.Default.Bookmark,
            accentColor = EmeraldPro
        )
    )

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Be Happy FF Tools",
                subtitle = "Free Fire Gaming Companion",
                actions = {
                    IconButton(
                        onClick = { onNavigate(NavRoutes.FAVORITES) },
                        modifier = Modifier.testTag("home_top_bar_favorites_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Favorites",
                            tint = AmberFlameLight
                        )
                    }
                }
            )
        },
        containerColor = NavyBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Current Device Header Card
            DeviceProfileHeaderCard(
                device = deviceProfile,
                onChangeDeviceClick = { onNavigate(NavRoutes.DEVICE_LIBRARY) }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Safety Badge
            SafetyDisclaimerCard()

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "GAMING COMPANION TOOLS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AmberFlameLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier.fillMaxSize().testTag("home_features_grid")
            ) {
                items(features) { card ->
                    HomeCardItem(card = card, onClick = { onNavigate(card.route) })
                }
            }
        }
    }
}

@Composable
fun HomeCardItem(
    card: HomeFeatureCard,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(
                1.dp,
                card.accentColor.copy(alpha = 0.35f),
                RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .testTag("home_card_${card.route}"),
        colors = CardDefaults.cardColors(containerColor = NavySurface)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(card.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = card.icon,
                        contentDescription = card.title,
                        tint = card.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Surface(
                    color = card.accentColor.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = card.badge,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = card.accentColor
                    )
                }
            }

            Column {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    ),
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = card.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondary,
                    maxLines = 2,
                    lineHeight = 14.sp
                )
            }
        }
    }
}
