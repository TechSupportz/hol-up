package com.nasportfolio.holup.ui.blocker

import android.content.Intent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nasportfolio.holup.ui.theme.HolUpTheme

@Composable
fun WindowContent(
    hide: () -> Unit
) {
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
                    Text(
                        text = "Oops, you're blocked!",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "You can't use this app right now.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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