package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GamingTopAppBar
import com.example.ui.theme.*
import com.example.viewmodel.AppViewModel

@Composable
fun HeadshotTrainingScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val trainingState by viewModel.trainingState.collectAsState()
    val timerSeconds by viewModel.timerSecondsRemaining.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    val activeRound by viewModel.activeRound.collectAsState()
    val hitScore by viewModel.targetHitScore.collectAsState()
    val targetPos by viewModel.targetPosition.collectAsState()

    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60
    val timerFormatted = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            GamingTopAppBar(
                title = "Headshot Training Routine",
                subtitle = "3-Round Daily Aim Warm-up",
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

            // Daily Progress & Streak Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        Brush.horizontalGradient(listOf(AmberFlame, EmeraldPro)),
                        RoundedCornerShape(16.dp)
                    )
                    .testTag("training_progress_header_card"),
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
                                text = "TODAY'S TRAINING",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                ),
                                color = AmberFlameLight
                            )
                            Text(
                                text = "${trainingState.totalRoundsCompleted} / 3 Rounds Completed",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }

                        Surface(
                            color = AmberFlame.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AmberFlame)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "🔥", fontSize = 14.sp)
                                Text(
                                    text = "${trainingState.streakDays} Day Streak",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = AmberFlameLight
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { trainingState.totalRoundsCompleted / 3f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .testTag("training_daily_progress_bar"),
                        color = if (trainingState.totalRoundsCompleted == 3) EmeraldPro else AmberFlame,
                        trackColor = NavyCardBorder
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Target Practice Hits: ${trainingState.dragPracticeHits}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        TextButton(
                            onClick = { viewModel.resetTrainingProgress() },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.testTag("reset_training_button")
                        ) {
                            Text("Reset Today", fontSize = 11.sp, color = TextMuted)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active Timer / Training Controller Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, CyanTech.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .testTag("training_timer_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "ACTIVE DRILL: ROUND $activeRound",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        ),
                        color = CyanTech
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Large Digital Countdown Clock
                    Surface(
                        color = NavyBackground,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isTimerRunning) AmberFlame else NavyCardBorder)
                    ) {
                        Text(
                            text = timerFormatted,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                            style = MaterialTheme.typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            ),
                            color = if (isTimerRunning) AmberFlameLight else TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (!isTimerRunning) {
                            Button(
                                onClick = {
                                    if (timerSeconds == 0 || timerSeconds == 180) {
                                        viewModel.startTimer(activeRound)
                                    } else {
                                        viewModel.resumeTimer()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AmberFlame,
                                    contentColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("start_round_timer_button")
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (timerSeconds < 180 && timerSeconds > 0) "Resume Drill" else "Start Round $activeRound",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Button(
                                onClick = { viewModel.pauseTimer() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DangerRed,
                                    contentColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("pause_round_timer_button")
                            ) {
                                Icon(Icons.Default.Pause, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Pause", fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = { viewModel.completeRound(activeRound) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldPro,
                                contentColor = TextPrimary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("complete_round_button")
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Mark Done", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive Drag & Flick Reaction Mini-Target Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, AmberFlame.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                    .testTag("interactive_drag_practice_canvas"),
                colors = CardDefaults.cardColors(containerColor = NavySurface)
            ) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val boxW = maxWidth
                    val boxH = maxHeight

                    Text(
                        text = "REACTION & FLICK DRILL: Tap moving head target",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        ),
                        color = TextSecondary,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                    )

                    Surface(
                        color = AmberFlame.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "Hits: $hitScore",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = AmberFlameLight
                        )
                    }

                    // Interactive Headshot Target Button
                    val targetX = (targetPos.first * (boxW.value - 60)).dp
                    val targetY = (targetPos.second * (boxH.value - 60)).dp

                    Box(
                        modifier = Modifier
                            .offset(x = targetX, y = targetY)
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(listOf(DangerRed, AmberFlame, Color(0xFF7F1D1D)))
                            )
                            .border(2.dp, Color.White, CircleShape)
                            .clickable { viewModel.hitTrainingTarget() }
                            .testTag("interactive_target_circle"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🎯", fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3-Round Detailed Curriculum
            Text(
                text = "3-ROUND DAILY WARM-UP CURRICULUM",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                color = AmberFlameLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            RoundCurriculumCard(
                roundNum = 1,
                title = "ROUND 1 — Sensitivity & Camera Warm-up (3 Min)",
                description = "Enter Free Fire Training Ground. Practice smooth 360 camera turns, weapon swapping without looking, and tracking stationary bots with crosshair centered at chin level.",
                isCompleted = trainingState.round1Completed,
                isActive = activeRound == 1,
                onSelectRound = { viewModel.startTimer(1) },
                testTag = "round_1_card"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RoundCurriculumCard(
                roundNum = 2,
                title = "ROUND 2 — Upward Drag & Flick Drills (3 Min)",
                description = "Equip shotgun (M1887/M1014) or SMG (MP40). Practice straight-up and J-shape drags against moving targets. Release finger immediately when red numbers appear.",
                isCompleted = trainingState.round2Completed,
                isActive = activeRound == 2,
                onSelectRound = { viewModel.startTimer(2) },
                testTag = "round_2_card"
            )

            Spacer(modifier = Modifier.height(10.dp))

            RoundCurriculumCard(
                roundNum = 3,
                title = "ROUND 3 — Lone Wolf / Ranked Accuracy (3 Min)",
                description = "Play 1 match of Lone Wolf or Clash Squad. Focus 100% on crosshair pre-aiming around corners at enemy head height before pulling the fire button.",
                isCompleted = trainingState.round3Completed,
                isActive = activeRound == 3,
                onSelectRound = { viewModel.startTimer(3) },
                testTag = "round_3_card"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoundCurriculumCard(
    roundNum: Int,
    title: String,
    description: String,
    isCompleted: Boolean,
    isActive: Boolean,
    onSelectRound: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                if (isCompleted) EmeraldPro.copy(alpha = 0.5f) else if (isActive) AmberFlame else NavyCardBorder,
                RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onSelectRound)
            .testTag(testTag),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) NavySurfaceVariant else NavySurface
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isCompleted) EmeraldPro else if (isActive) AmberFlame else NavyCardBorder,
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    } else {
                        Text(
                            text = "$roundNum",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isCompleted) EmeraldPro else TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = TextSecondary,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
