package com.nasportfolio.holup

import android.app.AppOpsManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.provider.Settings
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nasportfolio.holup.service.BlockerService
import com.nasportfolio.holup.ui.theme.HolUpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private fun checkUsageStatsPermission(): Boolean {
        val appOpsManager = getSystemService(APP_OPS_SERVICE) as AppOpsManager
        // `AppOpsManager.checkOpNoThrow` is deprecated from Android Q
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(),
                packageName
            )
        } else {
            appOpsManager.checkOpNoThrow(
                "android:get_usage_stats",
                Process.myUid(),
                packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val list = packageManager.getAllApplications()
            val mainViewModel = viewModel<MainViewModel>()
            val blockedApps by mainViewModel.blockedApps.collectAsState()
            val behavior = TopAppBarDefaults.pinnedScrollBehavior()

            if (checkUsageStatsPermission()) {
                ContextCompat.startForegroundService(this, Intent(this, BlockerService::class.java))
            } else {
                Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    startActivityForResult(this, 100)
                }
            }

            onNewIntent(intent)

            HolUpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .nestedScroll(connection = behavior.nestedScrollConnection),
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(text = "Hol-Up")
                                },
                                scrollBehavior = behavior
                            )
                        }
                    ) { paddingValues ->
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            items(list) {
                                val packageName = it.activityInfo.packageName
                                val name = it.activityInfo
                                    .loadLabel(packageManager)
                                    .toString()
                                val bitmap = it.activityInfo
                                    .loadIcon(packageManager)
                                    .toBitmap()
                                val isBlocked = blockedApps
                                    .map { it.packageName }
                                    .contains(packageName)

                                val onCheck = onCheck@{
                                    if (isBlocked) {
                                        mainViewModel.deleteApp(packageName)
                                        return@onCheck
                                    }
                                    mainViewModel.addBlockApp(
                                        packageName = packageName,
                                        name = name,
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable(onClick = onCheck)
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = isBlocked, onCheckedChange = { onCheck() })
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(60.dp)
                                            .aspectRatio(1f)
                                            .clip(RoundedCornerShape(percent = 50)),
                                    )
                                    Column {
                                        Text(
                                            text = name,
                                            fontSize = 18.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val packageName = intent?.getStringExtra("package")
        packageName?.let {
            Intent(this, TakeABreakActivity::class.java).also { intent ->
                intent.putExtra("package", packageName)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != 100) return
        if (!checkUsageStatsPermission()) return
        ContextCompat.startForegroundService(
            this,
            Intent(this, BlockerService::class.java)
        )
    }
}