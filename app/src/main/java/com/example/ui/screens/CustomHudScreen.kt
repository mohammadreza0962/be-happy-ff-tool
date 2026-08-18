package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HudTemplates
import com.example.model.HudControl
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel
import kotlin.math.roundToInt

@Composable
fun CustomHudScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedFingers by viewModel.selectedFingerCount.collectAsState()
    val activeLayout by viewModel.activeHudLayout.collectAsState()
    var showSaveDialog by remember { mutableStateOf(false) }
    var favoriteName by remember { mutableStateOf("") }
    var selectedControlId by remember { mutableStateOf<String?>("fire_right") }

    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = NavySurface,
            title = {
                Text(
                    text = "Save Custom HUD Layout",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Save this $selectedFingers-finger HUD preset to your local Favorites.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = favoriteName,
                        onValueChange = { favoriteName = it },
                        label = { Text("HUD Name", color = TextSecondary) },
                        placeholder = { Text("My $selectedFingers-Finger Esports Setup", color = TextMuted) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = AmberFlame,
                            unfocusedBorderColor = NavyCardBorder
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("save_hud_name_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveHudFavorite(favoriteName)
                        showSaveDialog = false
                        favoriteName = ""
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberFlame,
                        contentColor = TextPrimary
                    ),
                    modifier = Modifier.testTag("confirm_save_hud_button")
                ) {
                    Text("Save HUD")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSaveDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "HUD Studio",
                subtitle = "${activeLayout.fingerCount}-Finger Layout: ${activeLayout.title}",
                showBackButton = true,
                onBackClick = onNavigateBack,
                actions = {
                    IconButton(
                        onClick = { viewModel.shareHudLayout() },
                        modifier = Modifier.testTag("hud_share_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
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
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Finger Tab Switcher (2 Finger, 3 Finger, 4 Finger, 5 Finger)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                Row(
                    modifier = Modifier
                        .padding(6.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(2, 3, 4, 5).forEach { count ->
                        val isSelected = selectedFingers == count
                        Button(
                            onClick = { viewModel.selectHudFingerCount(count) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) AmberFlame else Color.Transparent,
                                contentColor = if (isSelected) TextPrimary else TextSecondary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("hud_tab_${count}_finger"),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "$count Finger",
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Phone-Screen Visual HUD Simulation Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.5.dp,
                        Brush.linearGradient(listOf(CyanTech.copy(alpha = 0.6f), AmberFlame.copy(alpha = 0.6f))),
                        RoundedCornerShape(16.dp)
                    )
                    .testTag("hud_interactive_canvas_card"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF070B14))
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    val canvasWidth = maxWidth
                    val canvasHeight = maxHeight

                    // Decorative phone notch & map / crosshair grid
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Top notch indicator
                        drawRoundRect(
                            color = Color(0xFF1E293B),
                            size = androidx.compose.ui.geometry.Size(60.dp.toPx(), 8.dp.toPx()),
                            topLeft = androidx.compose.ui.geometry.Offset((size.width - 60.dp.toPx()) / 2, 0f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                        // Center crosshair
                        drawCircle(
                            color = Color(0x33FFFFFF),
                            radius = 12.dp.toPx(),
                            center = center,
                            style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx())
                        )
                    }

                    // Render Interactive Controls on Canvas
                    activeLayout.controls.forEach { ctrl ->
                        val isSelected = ctrl.id == selectedControlId

                        val controlX = (ctrl.xPercent * (canvasWidth.value - 40)).dp
                        val controlY = (ctrl.yPercent * (canvasHeight.value - 40)).dp

                        Box(
                            modifier = Modifier
                                .offset(x = controlX, y = controlY)
                                .size((ctrl.sizeDp * 0.55).dp)
                                .clip(CircleShape)
                                .background(
                                    when (ctrl.iconType) {
                                        "fire" -> AmberFlame.copy(alpha = ctrl.opacityPercent / 100f)
                                        "shield" -> CyanTech.copy(alpha = ctrl.opacityPercent / 100f)
                                        "jump" -> EmeraldPro.copy(alpha = ctrl.opacityPercent / 100f)
                                        "scope" -> PurpleNeon.copy(alpha = ctrl.opacityPercent / 100f)
                                        "joystick" -> Color(0xFF334155).copy(alpha = ctrl.opacityPercent / 100f)
                                        else -> NavySurfaceVariant.copy(alpha = ctrl.opacityPercent / 100f)
                                    }
                                )
                                .border(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                                    CircleShape
                                )
                                .pointerInput(ctrl.id) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        selectedControlId = ctrl.id
                                        val newX = ctrl.xPercent + (dragAmount.x / size.width.toFloat())
                                        val newY = ctrl.yPercent + (dragAmount.y / size.height.toFloat())
                                        viewModel.updateHudControlPosition(ctrl.id, newX, newY)
                                    }
                                }
                                .testTag("hud_control_${ctrl.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (ctrl.iconType) {
                                    "fire" -> "🔥"
                                    "shield" -> "🛡️"
                                    "jump" -> "⬆️"
                                    "crouch" -> "🧎"
                                    "prone" -> "🛌"
                                    "scope" -> "🎯"
                                    "joystick" -> "🕹️"
                                    "gun" -> "🔫"
                                    "medkit" -> "➕"
                                    "skill" -> "⚡"
                                    "reload" -> "🔄"
                                    else -> "🔘"
                                },
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Canvas Hint
                    Text(
                        text = "Touch & drag any button to reposition layout",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = TextMuted,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Layout Specs & Description
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
                        Text(
                            text = activeLayout.title.uppercase(),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Surface(
                            color = AmberFlame.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = activeLayout.difficulty,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                ),
                                color = AmberFlameLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = activeLayout.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showSaveDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("hud_save_favorite_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldPro),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldPro.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Save HUD", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { viewModel.applyHudLayout(activeLayout) },
                            modifier = Modifier
                                .weight(1.3f)
                                .testTag("hud_apply_guide_button"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AmberFlame,
                                contentColor = TextPrimary
                            )
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Apply & Guide", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
