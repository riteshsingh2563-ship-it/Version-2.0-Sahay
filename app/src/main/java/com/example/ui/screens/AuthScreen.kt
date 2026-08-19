package com.example.ui.screens

import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.ui.components.SahayFullLogo
import com.example.ui.theme.SahayNavy
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import java.security.SecureRandom

@Composable
fun AuthScreen(
    onLoginSuccess: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    fun generateNonce(): String {
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }

    fun signInWithGoogle() {
        scope.launch {
            isLoading = true
            errorMessage = null

            try {
                val nonce = generateNonce()

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(
                        context.getString(
                            com.example.R.string.default_web_client_id
                        )
                    )
                    .setAutoSelectEnabled(false)
                    .setNonce(nonce)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val credentialManager = CredentialManager.create(context)

                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                val credential = result.credential

                val googleCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                val idToken = googleCredential.idToken

                val firebaseCredential =
                    GoogleAuthProvider.getCredential(idToken, null)

                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        isLoading = false

                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            if (user != null) {
                                onLoginSuccess(
                                    user.email ?: user.uid
                                )
                            }
                        } else {
                            errorMessage =
                                task.exception?.message
                                    ?: "Google login failed"
                        }
                    }

            } catch (e: Exception) {
                isLoading = false
                errorMessage =
                    e.message ?: "Google login cancelled or failed"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        SahayFullLogo(
            size = 170.dp,
            showTagline = true,
            animated = false
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Welcome to SAHAY",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = SahayNavy
                    )
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Sign in with your Google account to continue.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Button(
                    onClick = {
                        if (!isLoading) {
                            signInWithGoogle()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SahayNavy,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = if (isLoading)
                            "Signing in..."
                        else
                            "Continue with Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (errorMessage != null) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Text(
            text = "By continuing, you agree to SAHAY's Terms of Service & Privacy Policy.",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp
            ),
            color = Color.Gray
        )
    }
}
