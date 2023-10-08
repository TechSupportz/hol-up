package com.nasportfolio.holup.data.dao

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
    private val firestore: FirebaseFirestore
) : BlockedAppDao {
    override fun getAllBlockedApps(): Flow<List<BlockedApp>> {
        return callbackFlow {
            val listener = firestore.collection("blockedApps").addSnapshotListener { value, error ->
                error?.let {
                    this.close(it)
                    return@addSnapshotListener
                }
                value?.let {
                    val blockedApps = it.toObjects(BlockedApp::class.java)
                    this.trySend(blockedApps)
                }
            }
            awaitClose {
                listener.remove()
                this.cancel()
            }
        }
    }

    override suspend fun upsertBlockedApp(blockedApp: BlockedApp) {
        blockedApp.id?.let {
            firestore.collection("blockedApps").document(it).set(blockedApp)
        } ?: run {
            firestore.collection("blockedApps").document().set(blockedApp)
        }
    }

    override suspend fun deleteBlockedApp(blockedApp: BlockedApp) {
        blockedApp.id?.let {
            firestore.collection("blockedApps").document(it).delete()
        }
    }

    override suspend fun deleteAppByPackageName(packageName: String) {
        val documentId = firestore.collection("blockedApps")
            .whereEqualTo("packageName", packageName)
            .get()
            .await()
            .documents
            .firstOrNull()
            ?.id
        documentId?.let {
            firestore.collection("blockedApps").document(it).delete()
        }
    }
}