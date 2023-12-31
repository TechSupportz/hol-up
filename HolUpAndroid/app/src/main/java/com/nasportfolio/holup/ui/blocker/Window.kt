package com.nasportfolio.holup.ui.blocker

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.nasportfolio.holup.data.models.BlockedApp
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Window @Inject constructor(
    @ApplicationContext private val context: Context
) : LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {
    private var isShown = false

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val layoutParams = WindowManager.LayoutParams(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.MATCH_PARENT,
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        else
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
        PixelFormat.TRANSLUCENT
    )

    private val lifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    private fun handleLifecycleEvent(event: Lifecycle.Event) =
        lifecycleRegistry.handleLifecycleEvent(event)

    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    private val store = ViewModelStore()
    override val viewModelStore: ViewModelStore
        get() = store

    fun onCreate() {
        savedStateRegistryController.performRestore(null)
        handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun onDestroy() {
        handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        hide()
    }

    private var view = ComposeView(context).apply {
        layoutParams = this@Window.layoutParams
    }

    private var listener: WindowContentListener? = null

    fun setWindowContentListener(listener: WindowContentListener) {
        this.listener = listener
    }

    fun show(blockedApp: BlockedApp) {
        try {
            if (isShown) return
            view = view.apply {
                setViewTreeLifecycleOwner(this@Window)
                setViewTreeViewModelStoreOwner(this@Window)
                setViewTreeSavedStateRegistryOwner(this@Window)
                setContent {
                    WindowContent(
                        hide = {
                            listener?.hide()
                        },
                        blockedApp = blockedApp
                    )
                }
            }.also {
                windowManager.addView(it, layoutParams)
                isShown = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hide() {
        try {
            if (!isShown) return
            view = view.apply {
                setViewTreeLifecycleOwner(null)
                setViewTreeViewModelStoreOwner(null)
                setViewTreeSavedStateRegistryOwner(null)
            }.also {
                windowManager.removeView(it)
                it.invalidate()
                isShown = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isShown() = isShown

    interface WindowContentListener {
        fun hide()
    }
}