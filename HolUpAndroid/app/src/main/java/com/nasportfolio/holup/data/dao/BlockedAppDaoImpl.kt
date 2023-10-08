package com.nasportfolio.holup.data.dao

import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import com.nasportfolio.holup.data.models.BlockedApp
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// TODO: NEED ADD USER ID AND CHANGE LOGIC
class BlockedAppDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferences,
) : BlockedAppDao {
    private val userId = sharedPreferences.getString("userId", null)

    override fun getAllBlockedApps(): Flow<List<BlockedApp>> {
        return callbackFlow {
            val listener = firestore
                .collection("blockedApps")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { value, error ->
                    error?.let {
                        this.close(it)
                        return@addSnapshotListener
                    }
                    value ?: return@addSnapshotListener
                    val blockedApps = value.toObjects(BlockedApp::class.java)
                    this.trySend(blockedApps)
                }
            awaitClose {
                listener.remove()
                this.cancel()
            }
        }
    }

    override suspend fun upsertBlockedApp(blockedApp: BlockedApp) {
        blockedApp.id?.let {
            println("UPDATING BLOCKED APP $blockedApp")
            firestore.collection("blockedApps").document(it).set(blockedApp)
        } ?: run {
            println("INSERTING BLOCKED APP $blockedApp")
            firestore.collection("blockedApps").document().set(blockedApp)
        }
    }

    override suspend fun deleteBlockedApp(blockedApp: BlockedApp) {
        println("DELETING BLOCKED APP $blockedApp")
        blockedApp.id?.let {
            firestore.collection("blockedApps").document(it).delete()
        }
    }

    override suspend fun deleteAppByPackageName(packageName: String) {
        println("DELETING BY PACKAGE NAME $packageName")
        val id = firestore.collection("blockedApps")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .firstOrNull { it["packageName"] == packageName }
            ?.id ?: return
        firestore.collection("blockedApps")
            .document(id)
            .delete()
    }
}