package com.nasportfolio.holup.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.nasportfolio.holup.data.dao.BlockedAppDaoImpl
import com.nasportfolio.holup.getAllApplications
import com.nasportfolio.holup.startBlockerService
import com.nasportfolio.holup.ui.theme.HolUpTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onNewIntent(intent)
        val list = packageManager.getAllApplications()

        setContent {
            val mainViewModel = viewModel<MainViewModel>()
            val blockedApps by mainViewModel.blockedApps.collectAsState()
            val mainState by mainViewModel.mainState.collectAsState()
            val behavior = TopAppBarDefaults.pinnedScrollBehavior()

            HolUpTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (mainState.userId == null)
                        Dialog(
                            onDismissRequest = { },
                            properties = DialogProperties(
                                dismissOnBackPress = false,
                                dismissOnClickOutside = false,
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.surface)
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "Enter User ID to continue",
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                TextField(
                                    value = mainState.editingUserId,
                                    onValueChange = mainViewModel::updateEditingUserId,
                                    modifier = Modifier.fillMaxWidth(),
                                    placeholder = { Text(text = "User ID") }
                                )
                                Button(
                                    onClick = {
                                        mainViewModel.updateUserId(mainState.editingUserId)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                ) {
                                    Text(text = "Submit")
                                }
                            }
                        }
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
                            items(list) { appInfo ->
                                val isBlocked = blockedApps.any { blockedApp ->
                                    val isFromServer = blockedApp.packageName == null
                                    if (isFromServer) {
                                        return@any run {
                                            appInfo.packageName.contains(blockedApp.name) ||
                                                    appInfo.name.lowercase()
                                                        .contains(blockedApp.name.lowercase())
                                        }.also {
                                            if (!it) return@also
                                            mainViewModel.updateBlockApp(
                                                blockedApp.copy(packageName = appInfo.packageName)
                                            )
                                            println("blockedApp: ${blockedApp.name}, appInfo: ${appInfo.packageName}")
                                        }
                                    }
                                    blockedApp.packageName == appInfo.packageName
                                }

                                val onCheck = onCheck@{
                                    if (isBlocked) {
                                        mainViewModel.deleteApp(appInfo.packageName)
                                        return@onCheck
                                    }
                                    mainViewModel.addBlockApp(
                                        packageName = appInfo.packageName,
                                        name = appInfo.name,
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .clickable(onClick = onCheck)
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(checked = isBlocked, onCheckedChange = null)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            bitmap = appInfo.icon.asImageBitmap(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .width(50.dp)
                                                .aspectRatio(1f)
                                                .clip(RoundedCornerShape(percent = 50)),
                                        )
                                        Text(
                                            text = appInfo.name,
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

    override fun onResume() {
        super.onResume()
        startBlockerService()
    }
}