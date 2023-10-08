package com.nasportfolio.holup.ui

import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nasportfolio.holup.data.dao.BlockedAppDao
import com.nasportfolio.holup.data.models.BlockedApp
import com.nasportfolio.holup.service.BlockerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainState(
    val userId: String? = null,
    val editingUserId: String = "",
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val blockedAppDao: BlockedAppDao,
    private val sharedPreferences: SharedPreferences,
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    init {
        _mainState.value = MainState(
            userId = sharedPreferences.getString("userId", null),
            editingUserId = ""
        )
    }

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
                    userId = _mainState.value.userId,
                )
            )
            BlockerService.toRefresh = true
        }
    }

    fun updateUserId(userId: String) {
        sharedPreferences.edit().putString("userId", userId).apply()
        _mainState.update {
            it.copy(
                userId = userId,
                editingUserId = "",
            )
        }
    }

    fun updateEditingUserId(userId: String) {
        _mainState.update {
            it.copy(
                editingUserId = userId,
            )
        }
    }

    fun updateBlockApp(blockedApp: BlockedApp) {
        viewModelScope.launch {
            blockedAppDao.upsertBlockedApp(blockedApp)
            BlockerService.toRefresh = true
        }
    }

    fun deleteApp(packageName: String) {
        viewModelScope.launch {
            blockedAppDao.deleteAppByPackageName(packageName = packageName)
            BlockerService.toRefresh = true
        }
    }
}