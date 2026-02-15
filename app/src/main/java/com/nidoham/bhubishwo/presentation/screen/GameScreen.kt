package com.nidoham.bhubishwo.presentation.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import kotlinx.coroutines.delay

@Composable
fun ModernScoreHeader(
    currentRound: Int,
    totalRounds: Int,
    score: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Round indicator pill
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

        // Score badge with star
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
                    text = "$score",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun ModernImageCard(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
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
        // Glow effect behind image
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

        // Main image container
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
                if (imageUrl != null) {
                    Text(
                        text = "🏳️",
                        fontSize = 120.sp
                    )
                } else {
                    Text(
                        text = "❓",
                        fontSize = 100.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ModernQuestionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(title) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(500)
        )
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
    optionIndex: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCorrect == true -> Color(0xFF4CAF50)
            isCorrect == false && isSelected -> Color(0xFFFF5252)
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
            isSelected -> Color(0xFF3EBBF5)
            else -> Color(0xFF3A4156)
        },
        animationSpec = tween(300),
        label = "border"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
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
            .clickable(
                enabled = isCorrect == null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        tonalElevation = if (isSelected) 8.dp else 2.dp,
        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Option letter (A, B, C, D) with icon feedback
            Surface(
                shape = CircleShape,
                color = if (isSelected || isCorrect != null) {
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
                            contentDescription = "Correct",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        isCorrect == false && isSelected -> Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Wrong",
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

            // Selection dot
            if (isSelected && isCorrect == null) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.White, CircleShape)
                )
            }
        }
    }
}

@Composable
fun BottomProgressBar(
    currentQuestion: Int,
    totalQuestions: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progress",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${(currentQuestion.toFloat() / totalQuestions * 100).toInt()}%",
                fontSize = 12.sp,
                color = Color(0xFF3EBBF5),
                fontWeight = FontWeight.Bold
            )
        }

        LinearProgressIndicator(
            progress = { currentQuestion.toFloat() / totalQuestions },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF3EBBF5),
            trackColor = Color(0xFF2A3142),
            gapSize = 0.dp
        )
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
    val totalQuestions = uiState.questions.size
    val currentRound = uiState.currentQuestionIndex + 1

    // Auto-advance after selection
    LaunchedEffect(uiState.isAnswerSubmitted) {
        if (uiState.isAnswerSubmitted) {
            delay(1200) // Show feedback for 1.2 seconds
            viewModel.nextQuestion()
        }
    }

    if (uiState.gameFinished) {
        ModernGameFinishedScreen(
            userScore = uiState.userScore,
            totalQuestions = totalQuestions,
            onRestart = { viewModel.restartGame() }
        )
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
                BottomProgressBar(
                    currentQuestion = currentRound,
                    totalQuestions = totalQuestions
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
                // Big Image
                ModernImageCard(imageUrl = currentQuestion.url)

                // Question Title
                ModernQuestionTitle(title = currentQuestion.title)

                Spacer(modifier = Modifier.weight(1f))

                // Answer Options (4 options)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.options.forEachIndexed { index, option ->
                        ModernAnswerOption(
                            text = option,
                            isSelected = uiState.selectedAnswer == option,
                            isCorrect = if (uiState.isAnswerSubmitted) {
                                option == currentQuestion.options[currentQuestion.index]
                            } else null,
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

@Composable
fun ModernGameFinishedScreen(
    userScore: Int,
    totalQuestions: Int,
    onRestart: () -> Unit
) {
    val percentage = (userScore.toFloat() / totalQuestions * 100).toInt()

    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1F2E)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            // Trophy emoji with animation
            Text(
                text = when {
                    percentage >= 90 -> "🏆"
                    percentage >= 70 -> "🎉"
                    percentage >= 50 -> "👍"
                    else -> "💪"
                },
                fontSize = 80.sp,
                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when {
                    percentage >= 90 -> "Outstanding!"
                    percentage >= 70 -> "Great Job!"
                    percentage >= 50 -> "Good Effort!"
                    else -> "Keep Going!"
                },
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Score card
            Surface(
                color = Color(0xFF2A3142),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Your Score",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$userScore",
                        fontSize = 56.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )

                    Text(
                        text = "/ $totalQuestions",
                        fontSize = 20.sp,
                        color = Color.White.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { percentage / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = when {
                            percentage >= 90 -> Color(0xFF4CAF50)
                            percentage >= 70 -> Color(0xFF8BC34A)
                            percentage >= 50 -> Color(0xFFFFC107)
                            else -> Color(0xFFFF9800)
                        },
                        trackColor = Color(0xFF1A1F2E)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$percentage%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3EBBF5)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Play Again",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}