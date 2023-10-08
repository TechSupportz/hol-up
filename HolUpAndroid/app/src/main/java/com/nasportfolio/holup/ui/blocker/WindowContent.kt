package com.nasportfolio.holup.ui.blocker

import android.content.Intent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nasportfolio.holup.R
import com.nasportfolio.holup.data.models.BlockedApp
import com.nasportfolio.holup.ui.theme.HolUpTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun WindowContent(
    blockedApp: BlockedApp,
    hide: () -> Unit
) {
    var cooldown by remember {
        mutableStateOf(blockedApp.cooldown)
    }
    val transition = rememberInfiniteTransition()
    val scaleAnimation by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val paddingTopAnimation by transition.animateValue(
        initialValue = 0.dp,
        targetValue = 16.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(true) {
        while (cooldown > 0 && isActive) {
            delay(1000)
            cooldown--
        }
    }

    HolUpTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hol_up),
                        contentDescription = "Hol Up",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .scale(if (cooldown == 0) 1f else scaleAnimation)
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (cooldown == 0) 0.dp else paddingTopAnimation),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Oops, you're blocked!",
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = "You can't use this app right now.",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        if (cooldown > 0) {
                            Text(
                                text = "You can use it again in $cooldown seconds.",
                                modifier = Modifier.padding(top = 16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
                if (cooldown == 0)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val context = LocalContext.current

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                Intent(Intent.ACTION_MAIN).also {
                                    it.addCategory(Intent.CATEGORY_HOME)
                                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(it)
                                }
                            }
                        ) {
                            Text(text = "I want to be productive!")
                        }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = hide
                        ) {
                            Text(text = "I want to procrastinate!")
                        }
                    }
            }
        }
    }
}