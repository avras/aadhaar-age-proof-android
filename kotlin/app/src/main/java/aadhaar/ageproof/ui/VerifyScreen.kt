package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.data.JsonAadhaarAgeProof
import aadhaar.ageproof.data.convertJsonAadhaarAgeProof
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import uniffi.ageproof.AadhaarAgeProof
import java.io.IOException


@Composable
fun VerifyScreen(
    ageProofUiState: AgeProofUiState,
    setProof: (AadhaarAgeProof?) -> Unit,
    resetReceivedProof: () -> Unit,
    verifyProof: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        val context = LocalContext.current
        val proofFileChooserLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument(),
            onResult = { uri: Uri? ->
                if (uri != null) {
                    try {
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val size = inputStream?.available()
                        val buffer = ByteArray(size ?: 0)
                        inputStream?.read(buffer)
                        inputStream?.close()

                        val json = buffer.toString(Charsets.UTF_8)
                        val gson = Gson()
                        val jsonProof = gson.fromJson(json, JsonAadhaarAgeProof::class.java)
                        val proof = convertJsonAadhaarAgeProof(jsonProof)
                        setProof(proof)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    setProof(null)
                }
            }
        )

        Button(
            onClick = {
                resetReceivedProof()
                proofFileChooserLauncher.launch(arrayOf("application/json"))
            },
            enabled = !ageProofUiState.proofGenerationInProgress &&
                    !ageProofUiState.proofVerificationInProgress,
        ) {
            Text("Select proof file")
        }
        Button(
            onClick = verifyProof,
            enabled = !ageProofUiState.proofGenerationInProgress &&
                    !ageProofUiState.proofVerificationInProgress &&
                    ageProofUiState.receivedProof != null,
        ) {
            Text("Verify proof")
        }

        if (ageProofUiState.ppGenerated) {
            Text("Public parameters generated in ${ageProofUiState.ppGenerationTime.inWholeSeconds} sec")
            if (ageProofUiState.proofGenerationInProgress || ageProofUiState.proofVerificationInProgress) {
                if (ageProofUiState.proofGenerationInProgress) {
                    Text("Proof generation in progress")
                } else {
                    Text("Proof verification in progress")
                }
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            } else if (ageProofUiState.proofVerified) {
                if (ageProofUiState.verifyResult != null) {
                    if (ageProofUiState.verifyResult.success) {
                        Text("Proof verified in ${ageProofUiState.proofVerificationTime.inWholeSeconds} sec")
                        Text(
                            "Nullifier: ${ageProofUiState.verifyResult.nullifier} ",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Text("Proof verification failed", color = Color.Red)
                        Text("Reason: ${ageProofUiState.verifyResult.message}")
                    }
                }
            }
        } else {
            if (ageProofUiState.ppGenerationInProgress) {
                Text("Public parameters generation in progress")
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

    }
}