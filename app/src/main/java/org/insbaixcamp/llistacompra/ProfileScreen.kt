package org.insbaixcamp.llistacompra

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onSignOut: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isPreview = LocalInspectionMode.current

    val user = if (isPreview) null else try {
        Firebase.auth.currentUser
    } catch (_: IllegalStateException) {
        null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Perfil d'Usuari")

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = user?.displayName ?: "Usuari")
        Text(text = user?.email ?: "")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!isPreview) {
                    try {
                        Firebase.auth.signOut()
                    } catch (e: Exception) {
                        Log.e("ProfileScreen", "Error signing out from Firebase: ${e.message}")
                    }
                }
                scope.launch {
                    try {
                        val credentialManager = CredentialManager.create(context)
                        val clearRequest = ClearCredentialStateRequest()
                        credentialManager.clearCredentialState(clearRequest)
                    } catch (e: ClearCredentialException) {
                        Log.e("ProfileScreen", "Couldn't clear user credentials: ${e.localizedMessage}")
                    } finally {
                        onSignOut()
                    }
                }
            }
        ) {
            Text("Tancar Sessió")
        }
    }
}
