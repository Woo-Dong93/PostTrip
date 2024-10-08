package com.posttrip.journeydex

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.posttrip.journeydex.core.data.model.response.LoginData
import com.posttrip.journeydex.feature.reward.navigation.JTopAppBar
import com.posttrip.journeydex.model.OnboardingStepModel
import com.posttrip.journeydex.model.OnboardingStepModel.Companion.getTargetString
import com.posttrip.journeydex.ui.JourneydexApp
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onSetOnboarding : () -> Unit,
    loginData : LoginData,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val step by viewModel.onboardingStep.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = step.index != 0) {
        coroutineScope.launch {
            viewModel.updateStepIndex(
                step.index -1
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            onSetOnboarding()
        }
    }
    Onboarding(
        step = step,
        onBackClick = {
            viewModel.updateStepIndex(
                step.index -1
            )
        },
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
    onBackClick : () -> Unit,
    onClick: () -> Unit,
    onClickCard: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.statusBarsPadding().padding(bottom = 56.dp)
    ) {
        JTopAppBar(
            navigationIcon = {
                if(step.index != 0){
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
                    }
                }

            },
            actions = {
                Spacer(modifier = Modifier.size(40.dp))
            },
            title = {
                Text(
                    text = "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

        )
        Box(
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(top = 12.dp),
            contentAlignment = Alignment.TopCenter
        ){
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ){
                Spacer(
                    modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(40.dp))
                        .background(
                            color = if(step.index >= 0) Color(0xFF497CFF)
                            else Color(0xFFD9D9D9)
                        )
                )
                Spacer(
                    modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(40.dp))
                        .background(
                            color = if(step.index >= 1) Color(0xFF497CFF)
                            else Color(0xFFD9D9D9)
                        )
                )
                Spacer(
                    modifier = Modifier.width(40.dp).height(4.dp).clip(RoundedCornerShape(40.dp))
                        .background(
                            color = if(step.index >= 2) Color(0xFF497CFF)
                            else Color(0xFFD9D9D9)
                        )
                )
            }
        }

        TitleIntro(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 20.dp),
            title = when (step.index) {
                0 -> "어떤 여행을 선호하시나요?"
                1 -> "선호하는 여행지가 있으신가요?"
                2 -> "좋아하는 액티비티가 있으신가요?"
                else -> ""
            }
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
        ) {


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
            enabled = step.isEnabled(),
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
    enabled : Boolean,
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
        enabled = enabled,
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
    val isSelected = step.isSelected(
        targetStepIndex = step.index,
        target = index
    )
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF4F4F4),
            contentColor = Color.Black
        ),
        border = BorderStroke(
            1.dp, if(isSelected) Color(0xFF497CFF) else Color.Transparent
        ),
        contentPadding = PaddingValues(
           0.dp
        ),
        onClick = {
            onClick(text)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize().padding(
                    if(step.index == 1 || step.index == 0) 0.dp
                    else if(step.index == 2 && index == 0) 32.dp
                    else 24.dp
                )
            ){
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(
                        OnboardingStepModel.getTargetImage(
                            step.index,
                            index
                        )
                    ),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
            }


            Text(
                modifier = Modifier.padding(16.dp),
                text = OnboardingStepModel.getTargetTitle(
                    step.index,
                    index
                ),
                color =  if(isSelected && step.index == 2) Color(0xFF497CFF) else Color.Black
            )
            Spacer(
                modifier = Modifier.fillMaxSize()
                    .background(
                        color = if(isSelected) Color(0x33497CFF) else Color.Transparent
                    )
            )
            if(isSelected){
                Icon(
                    modifier = Modifier.size(60.dp).align(Alignment.Center),
                    painter = painterResource(id = R.drawable.ic_favorite_onboarding    ),
                    tint = if(step.index == 1) Color.White else Color(0xFF497CFF),
                    contentDescription = null
                )
            }
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
