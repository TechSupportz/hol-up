package com.nasportfolio.holup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import com.nasportfolio.holup.ui.theme.HolUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val list = packageManager.getAllApplications()
            HolUpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LazyColumn {
                        items(list) {
                            Row(
                                modifier = Modifier
                                    .clickable {

                                    }
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    bitmap = it.activityInfo
                                        .loadIcon(packageManager)
                                        .toBitmap()
                                        .asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(percent = 50)),
                                )
                                Column {
                                    Text(
                                        text = it.activityInfo
                                            .loadLabel(packageManager)
                                            .toString(),
                                        fontSize = 24.sp
                                    )
                                    Text(text = it.activityInfo.packageName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}