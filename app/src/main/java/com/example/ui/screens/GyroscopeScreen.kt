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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DeviceProfileHeaderCard
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun GyroscopeScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToDevicePicker: () -> Unit
) {
    val deviceProfile by viewModel.deviceProfile.collectAsState()
    val gyroConfig by viewModel.gyroConfig.collectAsState()

    var generalGyro by remember(gyroConfig.generalGyro) { mutableIntStateOf(gyroConfig.generalGyro) }
    var redDotGyro by remember(gyroConfig.redDotGyro) { mutableIntStateOf(gyroConfig.redDotGyro) }
    var scope2xGyro by remember(gyroConfig.scope2xGyro) { mutableIntStateOf(gyroConfig.scope2xGyro) }
    var scope4xGyro by remember(gyroConfig.scope4xGyro) { mutableIntStateOf(gyroConfig.scope4xGyro) }
    var sniperGyro by remember(gyroConfig.sniperGyro) { mutableIntStateOf(gyroConfig.sniperGyro) }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Gyroscope Aim Tuning",
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

            // Gyroscope Motion Calibration Visualizer
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CyanTech.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("gyro_motion_visualizer_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Concentric tilt pitch circles
                        drawCircle(color = NavyCardBorder, radius = 70.dp.toPx(), center = center, style = Stroke(1.dp.toPx()))
                        drawCircle(color = CyanTech.copy(alpha = 0.4f), radius = 45.dp.toPx(), center = center, style = Stroke(1.5.dp.toPx()))
                        drawCircle(color = AmberFlame.copy(alpha = 0.6f), radius = 20.dp.toPx(), center = center, style = Stroke(2.dp.toPx()))

                        // Crosshair axis
                        drawLine(color = Color(0x44FFFFFF), start = androidx.compose.ui.geometry.Offset(center.x - 90.dp.toPx(), center.y), end = androidx.compose.ui.geometry.Offset(center.x + 90.dp.toPx(), center.y), strokeWidth = 1.dp.toPx())
                        drawLine(color = Color(0x44FFFFFF), start = androidx.compose.ui.geometry.Offset(center.x, center.y - 90.dp.toPx()), end = androidx.compose.ui.geometry.Offset(center.x, center.y + 90.dp.toPx()), strokeWidth = 1.dp.toPx())
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${gyroConfig.skillLevel.uppercase()} GYRO PROFILE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            ),
                            color = CyanTech
                        )
                        Text(
                            text = "Tilt phone to test physical wrist recoil control",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Gyroscope Sliders Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(14.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "RECOMMENDED GYROSCOPE STARTING VALUES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = AmberFlameLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    GyroValueRow(
                        name = "General Gyro",
                        value = generalGyro,
                        onValueChange = { generalGyro = it },
                        testTag = "gyro_slider_general"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GyroValueRow(
                        name = "Red Dot Gyro",
                        value = redDotGyro,
                        onValueChange = { redDotGyro = it },
                        testTag = "gyro_slider_red_dot"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GyroValueRow(
                        name = "2X Scope Gyro",
                        value = scope2xGyro,
                        onValueChange = { scope2xGyro = it },
                        testTag = "gyro_slider_2x"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GyroValueRow(
                        name = "4X Scope Gyro",
                        value = scope4xGyro,
                        onValueChange = { scope4xGyro = it },
                        testTag = "gyro_slider_4x"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    GyroValueRow(
                        name = "Sniper Scope Gyro",
                        value = sniperGyro,
                        onValueChange = { sniperGyro = it },
                        testTag = "gyro_slider_sniper"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = gyroConfig.guidance,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.applyGyroSettings(generalGyro, redDotGyro) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CyanTech,
                            contentColor = NavyBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("apply_gyro_button")
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "APPLY & SHOW GYROSCOPE GUIDE",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GyroValueRow(
    name: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    testTag: String
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
            Surface(
                color = CyanTech.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "$value",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = CyanTech
                )
            }
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 0f..200f,
            colors = SliderDefaults.colors(
                thumbColor = CyanTech,
                activeTrackColor = CyanTech,
                inactiveTrackColor = NavyCardBorder
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag(testTag)
        )
    }
}
