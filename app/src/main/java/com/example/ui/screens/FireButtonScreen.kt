package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DeviceProfile
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun FireButtonScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val fireConfig by viewModel.fireButtonConfig.collectAsState()
    var currentSize by remember(fireConfig.sizePercent) { mutableIntStateOf(fireConfig.sizePercent) }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Fire Button Calibration",
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

            // Visual Fire Button Preview Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, AmberFlame.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("fire_button_visual_preview_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Background crosshair & grid canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val centerX = size.width / 2
                        val centerY = size.height / 2

                        // Outer safe ring
                        drawCircle(
                            color = NavyCardBorder,
                            radius = 90.dp.toPx(),
                            center = center,
                            style = Stroke(
                                width = 1.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                            )
                        )

                        // Drag trajectory line
                        drawLine(
                            brush = Brush.verticalGradient(listOf(AmberFlame, Color.Transparent)),
                            start = androidx.compose.ui.geometry.Offset(centerX, centerY - (currentSize * 1.4f)),
                            end = androidx.compose.ui.geometry.Offset(centerX, centerY - 80.dp.toPx()),
                            strokeWidth = 3.dp.toPx()
                        )
                    }

                    // Scaled Dynamic Fire Button Circle
                    val dynamicSizeDp = (currentSize * 1.8).dp
                    Box(
                        modifier = Modifier
                            .size(dynamicSizeDp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        AmberFlame,
                                        AmberFlameDark,
                                        Color(0xFF8B0000)
                                    )
                                )
                            )
                            .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Fire Button",
                                tint = Color.White,
                                modifier = Modifier.size((dynamicSizeDp / 3).coerceAtLeast(18.dp))
                            )
                            Text(
                                text = "$currentSize%",
                                color = Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Drag Indicator Label
                    Surface(
                        color = NavySurfaceVariant.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NavyCardBorder),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                                tint = AmberFlameLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Recommended Upward Drag Zone",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberFlameLight
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Calibration Slider Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "RECOMMENDED FIRE BUTTON SIZE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = TextSecondary
                            )
                            Text(
                                text = "${deviceProfile.fullName} (${deviceProfile.ram})",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = AmberFlame.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, AmberFlame)
                        ) {
                            Text(
                                text = "$currentSize%",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                                color = AmberFlameLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = currentSize.toFloat(),
                        onValueChange = {
                            currentSize = it.toInt()
                            viewModel.updateFireButtonSize(currentSize)
                        },
                        valueRange = 35f..75f,
                        colors = SliderDefaults.colors(
                            thumbColor = AmberFlame,
                            activeTrackColor = AmberFlame,
                            inactiveTrackColor = NavyCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("fire_button_size_slider")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("35% (Compact / High Precision)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Text("75% (Large Contact Area)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = fireConfig.tip,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.applyFireButton(currentSize) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmberFlame,
                            contentColor = TextPrimary
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("apply_fire_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPLY & SHOW MANUAL HUD GUIDE",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Placement & Drag Guidance Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RECOMMENDED DRAG TECHNIQUE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = CyanTech
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = fireConfig.dragTechnique,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "RECOMMENDED SCREEN PLACEMENT",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = EmeraldPro
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = fireConfig.positionZone,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
