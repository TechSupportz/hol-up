package com.nasportfolio.holup

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasportfolio.holup.dao.BlockedAppDao
import com.nasportfolio.holup.models.BlockedApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val blockedAppDao: BlockedAppDao
) : ViewModel() {

    val blockedApps = blockedAppDao.getAllBlockedApps()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun addBlockApp(packageName: String, name: String) {
        viewModelScope.launch {
            blockedAppDao.upsertBlockedApp(
                BlockedApp(
                    packageName = packageName,
                    name = name,
                )
            )
        }
    }

    fun deleteApp(packageName: String) {
        viewModelScope.launch {
            blockedAppDao.deleteAppByPackageName(packageName = packageName)
        }
    }
}