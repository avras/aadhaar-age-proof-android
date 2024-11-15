package aadhaar.ageproof.ui

import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.ui.scanner.ScannerActivity
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ProveScreen(
    ageProofUiState: AgeProofUiState,
    generatePublicParameters: () -> Unit,
    resetQrCode: () -> Unit,
    setQrCodeData: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    // Create a launcher for starting the activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = result.data
        if (intent != null) {
            val s = intent.dataString
            if (s != null) {
                setQrCodeData(s)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        if (ageProofUiState.ppGenerated) {
            Button(
                onClick = {
//                    resetQrCode()
                    val intent = Intent(context, ScannerActivity::class.java)
                    // Start the activity using the launcher
                    launcher.launch(intent)
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
            ageProofUiState = AgeProofUiState(
                ppGenerationInProgress = false,
                ppGenerated = true,
            ),
            {},
            {},
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}