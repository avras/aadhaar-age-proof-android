package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProveScreen(
    navController: NavController,
    ageProofUiState: AgeProofUiState,
    generatePublicParameters: () -> Unit,
    resetQrCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (ageProofUiState.ppGenerated) {
            Button(
                onClick = {
                    resetQrCode()
                    navController.navigate(NavigationItem.Scan.route)
                },
                modifier = modifier.fillMaxWidth()
            ) {
                Text("Scan Aadhaar QR Code")
            }
            if (!ageProofUiState.qrCodeString.isNullOrEmpty()) {
                Text("Scanned QR code success")
                Text("Size = ${ageProofUiState.qrCodeString.length}")
            }
        } else {
            Button(
                onClick = generatePublicParameters,
                enabled = !ageProofUiState.ppGenerationInProgress,
                modifier = modifier.fillMaxWidth()
            ) {
                Text("Generate Parameters")
            }
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

@Preview(showBackground = true)
@Composable
fun ProveScreenPreview() {
    AadhaarAgeProofTheme {
        ProveScreen(
            navController = rememberNavController(),
            ageProofUiState = AgeProofUiState(
                ppGenerationInProgress = false,
                ppGenerated = true,
            ),
            {},
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}