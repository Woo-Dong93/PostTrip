package com.posttrip.journeydex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.model.OnboardingStepModel
import com.posttrip.journeydex.ui.JourneydexApp

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onSetOnboarding : () -> Unit,
    loginData : LoginData,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val step by viewModel.onboardingStep.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            onSetOnboarding()
        }
    }
    Onboarding(
        step = step,
        onClick = {
            if (step.index <= 1) {
                viewModel.updateStepIndex(
                    step.index + 1
                )
            } else if (step.index == 2 &&
                step.style.isNotEmpty() &&
                step.type.isNotEmpty() &&
                step.destination.isNotEmpty()
            ) {
                viewModel.setOnboarding(loginData.id)
            }

        },
        onClickCard = {
            if (step.index == 0) {
                viewModel.updateStepStyle(it)
            } else if (step.index == 1) {
                viewModel.updateStepDestination(it)
            } else {
                viewModel.updateStepType(it)
            }
        },
        modifier = modifier
    )
}


@Composable
fun Onboarding(
    step: OnboardingStepModel,
    onClick: () -> Unit,
    onClickCard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(bottom = 56.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
        ) {
            TitleIntro(
                modifier = Modifier.fillMaxHeight(0.2f),
                title = when (step.index) {
                    0 -> "어떤 여행을 선호하시나요?"
                    1 -> "선호하는 여행지가 있으신가요?"
                    2 -> "좋아하는 액티비티가 있으신가요?"
                    else -> ""
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OnboardingCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        step = step,
                        index = 0,
                        onClick = onClickCard
                    )
                    OnboardingCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        step = step,
                        index = 1,
                        onClick = onClickCard
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OnboardingCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        step = step,
                        index = 2,
                        onClick = onClickCard
                    )
                    OnboardingCard(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        step = step,
                        index = 3,
                        onClick = onClickCard
                    )
                }
            }
        }

        NextButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            onClick = {
                onClick()
            },
        )

    }
}

@Composable
fun TitleIntro(
    title: String,
    modifier: Modifier = Modifier,
    intro: String = "저니덱스에 오신 것을 환영해요!",
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = intro,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

    }
}

@Composable
fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    containerColor: Color = Color(0xFF497CFF),
    contentColor: Color = Color(0xFFFFFFFF),
    disabledContainerColor: Color = Color(0xFFD9D9D9),
    disabledContentColor: Color = Color(0xDD212121)
) {
    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        ),
        onClick = onClick
    ) {
        Row(
            modifier = modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier.weight(1f),
                text = "확인",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OnboardingCard(
    step: OnboardingStepModel,
    index: Int,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = OnboardingStepModel.getTargetString(
        step.index,
        index
    )
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF4F4F4),
            contentColor = Color.Black
        ),
        onClick = {
            onClick(text)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
        ) {
            CardText(
                text = text,
                selected = step.isSelected(
                    targetStepIndex = step.index,
                    target = index
                )
            )
        }

    }
}

@Composable
fun CardText(
    text: String,
    selected: Boolean
) {
    Text(text = text + if (selected) " O" else "")
}