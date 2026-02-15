package com.nidoham.bhubishwo.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nidoham.bhubishwo.viewmodel.GameViewModel
import com.nidoham.bhubishwo.viewmodel.PerformanceStats

@Composable
fun ModernScoreHeader(
    currentRound: Int,
    totalRounds: Int,
    score: Int,
    modifier: Modifier = Modifier
) {
    // Smooth score animation
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(800, easing = EaseOutQuart),
        label = "score"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Round indicator
        Surface(
            color = Color(0xFF2A3142),
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Q",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3EBBF5)
                )
                Text(
                    text = "$currentRound",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "/",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
                Text(
                    text = "$totalRounds",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }

        // Score - clean, just the number
        Surface(
            color = Color(0xFF4CAF50).copy(alpha = 0.2f),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color(0xFF4CAF50).copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "★",
                    fontSize = 16.sp,
                    color = Color(0xFFFFD700)
                )
                Text(
                    text = "$animatedScore",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun TimerProgressBar(
    timeRemaining: Int,
    totalTime: Int = 20,
    isAnswerSubmitted: Boolean,
    modifier: Modifier = Modifier
) {
    val progress = timeRemaining.toFloat() / totalTime

    val timerColor = when {
        isAnswerSubmitted -> Color(0xFF4CAF50)
        timeRemaining <= 5 -> Color(0xFFFF5252)
        timeRemaining <= 10 -> Color(0xFFFFC107)
        else -> Color(0xFF3EBBF5)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Time",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )

            // Pulse animation when time is low
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (timeRemaining <= 5 && !isAnswerSubmitted) 1.3f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(400),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Text(
                text = "${timeRemaining}s",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = timerColor,
                modifier = if (timeRemaining <= 5 && !isAnswerSubmitted) Modifier.scale(scale) else Modifier
            )
        }

        // Progress bar only - no bonus hints
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFF2A3142))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                timerColor,
                                timerColor.copy(alpha = 0.8f)
                            )
                        )
                    )
            )
        }
    }
}

@Composable
fun ModernImageCard(imageUrl: String?, modifier: Modifier = Modifier) {
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(imageUrl) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.2f)
            .padding(horizontal = 24.dp)
            .scale(scale.value),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF3EBBF5).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFF2A3142),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (imageUrl != null) "🏳️" else "❓",
                    fontSize = if (imageUrl != null) 120.sp else 100.sp
                )
            }
        }
    }
}

@Composable
fun ModernQuestionTitle(title: String, modifier: Modifier = Modifier) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(title) {
        alpha.animateTo(targetValue = 1f, animationSpec = tween(500))
    }

    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        textAlign = TextAlign.Center,
        lineHeight = 32.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .alpha(alpha.value)
    )
}

@Composable
fun ModernAnswerOption(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean?,
    isCorrectAnswer: Boolean,
    optionIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCorrect == true -> Color(0xFF4CAF50)
            isCorrect == false && isSelected -> Color(0xFFFF5252)
            isCorrectAnswer && isCorrect == null -> Color(0xFF4CAF50).copy(alpha = 0.7f)
            isSelected -> Color(0xFF3EBBF5)
            else -> Color(0xFF2A3142)
        },
        animationSpec = tween(300),
        label = "background"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isCorrect == true -> Color(0xFF66BB6A)
            isCorrect == false && isSelected -> Color(0xFFFF7043)
            isCorrectAnswer && isCorrect == null -> Color(0xFF66BB6A)
            isSelected -> Color(0xFF3EBBF5)
            else -> Color(0xFF3A4156)
        },
        animationSpec = tween(300),
        label = "border"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected || (isCorrectAnswer && isCorrect == null)) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .scale(scale)
            .clickable(enabled = isCorrect == null && !isCorrectAnswer, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = if (isSelected || isCorrectAnswer) 8.dp else 2.dp,
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Option letter only - no points shown
            Surface(
                shape = CircleShape,
                color = if (isSelected || isCorrect != null || isCorrectAnswer) {
                    Color.White.copy(alpha = 0.2f)
                } else {
                    Color(0xFF3EBBF5).copy(alpha = 0.2f)
                },
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    when {
                        isCorrect == true -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        isCorrect == false && isSelected -> Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        isCorrectAnswer && isCorrect == null -> Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        else -> Text(
                            text = "${'A' + optionIndex}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else Color(0xFF3EBBF5)
                        )
                    }
                }
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PerformanceOverlayDialog(
    score: Int,
    stats: PerformanceStats,
    onExit: () -> Unit,
    onDismiss: () -> Unit
) {
    val correctRate = if (stats.totalCorrect + stats.totalWrong > 0) {
        (stats.totalCorrect.toFloat() / (stats.totalCorrect + stats.totalWrong) * 100).toInt()
    } else 0

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1F2E),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "🏆", fontSize = 48.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Performance Report",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Final Score
                Surface(
                    color = Color(0xFF2A3142),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Final Score",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$score",
                            color = Color(0xFF4CAF50),
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Correct",
                        value = "${stats.totalCorrect}",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Wrong",
                        value = "${stats.totalWrong}",
                        color = Color(0xFFFF5252),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Accuracy
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Accuracy",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "$correctRate%",
                            color = if (correctRate >= 70) Color(0xFF4CAF50) else Color(0xFFFFC107),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { correctRate / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (correctRate >= 70) Color(0xFF4CAF50) else Color(0xFFFFC107),
                        trackColor = Color(0xFF1A1F2E),
                        gapSize = 0.dp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onExit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Exit Game")
            }
        },
        dismissButton = null
    )
}

@Composable
fun StatCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = Color(0xFF2A3142),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = title, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    onExit: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
    val totalQuestions = uiState.questions.size
    val currentRound = uiState.currentQuestionIndex + 1

    if (uiState.showPerformanceDialog) {
        PerformanceOverlayDialog(
            score = uiState.userScore,
            stats = uiState.performanceStats,
            onExit = {
                viewModel.exitGame()
                onExit()
            },
            onDismiss = { viewModel.dismissPerformanceDialog() }
        )
    }

    if (uiState.gameFinished && !uiState.showPerformanceDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1F2E)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF3EBBF5))
        }
    } else if (currentQuestion != null) {
        Scaffold(
            topBar = {
                ModernScoreHeader(
                    currentRound = currentRound,
                    totalRounds = totalQuestions,
                    score = uiState.userScore
                )
            },
            bottomBar = {
                TimerProgressBar(
                    timeRemaining = uiState.timeRemaining,
                    isAnswerSubmitted = uiState.isAnswerSubmitted
                )
            },
            containerColor = Color(0xFF1A1F2E)
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                ModernImageCard(imageUrl = currentQuestion.url)
                ModernQuestionTitle(title = currentQuestion.title)

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.forEachIndexed { index, option ->
                        val isCorrectAnswer = option == currentQuestion.options[currentQuestion.index]

                        ModernAnswerOption(
                            text = option,
                            isSelected = uiState.selectedAnswer == option,
                            isCorrect = if (uiState.isAnswerSubmitted) {
                                option == currentQuestion.options[currentQuestion.index]
                            } else null,
                            isCorrectAnswer = uiState.showCorrectAnswer && isCorrectAnswer,
                            optionIndex = index,
                            onClick = {
                                if (!uiState.isAnswerSubmitted) {
                                    viewModel.selectAnswer(option)
                                    viewModel.submitAnswer()
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}