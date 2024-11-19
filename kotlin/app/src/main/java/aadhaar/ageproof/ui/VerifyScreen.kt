package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun VerifyScreen(
    ageProofUiState: AgeProofUiState,
    verifyProof: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
//        Button(
//            onClick = {
//                imageFileChooserLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//            },
//            enabled = !(ageProofUiState.proofGenerationInProgress || ageProofUiState.proofVerificationInProgress),
//        ) {
//            Text("Select proof file")
//        }
//        Text("or")
        Button(
            onClick = verifyProof,
            enabled = !ageProofUiState.proofGenerationInProgress &&
                    !ageProofUiState.proofVerificationInProgress &&
                    ageProofUiState.proof != null,
        ) {
            Text("Verify generated proof")
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