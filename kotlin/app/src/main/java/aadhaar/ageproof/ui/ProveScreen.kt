package aadhaar.ageproof.ui

import aadhaar.ageproof.R
import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.data.QrCodeScanStatus
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.config.ScannerConfig


@Composable
fun ProveScreen(
    ageProofUiState: AgeProofUiState,
    generatePublicParameters: () -> Unit,
    setQrCodeBytes: (String) -> Unit,
    resetQrCodeBytes: () -> Unit,
    generateProof: () -> Unit,
    resetProof: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scannerConfig = ScannerConfig.build {
        setBarcodeFormats(listOf(BarcodeFormat.FORMAT_QR_CODE))
        setOverlayStringRes(R.string.scan_aadhaar_qr_code)
        setShowCloseButton(true)
        setShowTorchToggle(true)
    }
    val scanQrCodeLauncher = rememberLauncherForActivityResult(ScanCustomCode()) { result ->
        when (result) {
            is QRResult.QRSuccess -> {
                setQrCodeBytes(result.content.rawValue!!)
            }

            else -> {
                setQrCodeBytes(String())
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Button(
            onClick = generatePublicParameters,
            enabled = !ageProofUiState.ppGenerationInProgress && !ageProofUiState.ppGenerated,
            modifier = modifier.fillMaxWidth()
        ) {
            if (ageProofUiState.ppGenerated) {
                Text("Public parameters generated")
            } else {
                Text("Generate Parameters")
            }
        }

        val scanButtonEnabled =
            ageProofUiState.ppGenerated && !ageProofUiState.proofGenerationInProgress
        Button(
            onClick = {
                resetQrCodeBytes()
                resetProof()
                scanQrCodeLauncher.launch(scannerConfig)
            },
            enabled = scanButtonEnabled,
            modifier = modifier.fillMaxWidth()
        ) {
            if (ageProofUiState.qrCodeData.isEmpty()) {
                Text("Scan Aadhaar QR code")
            } else {
                Text("Scan another Aadhaar QR code")
            }
        }

        Button(
            onClick = {
                resetProof()
                generateProof()
            },
            enabled = ageProofUiState.qrCodeData.isNotEmpty() && ageProofUiState.ppGenerated && !ageProofUiState.proofGenerationInProgress,
            modifier = modifier.fillMaxWidth()
        ) {
            Text("Generate Proof")
        }
        if (!ageProofUiState.ppGenerated) {
            if (ageProofUiState.ppGenerationInProgress) {
                Text("Public parameters generation in progress")
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            Text("Public parameters generated in ${ageProofUiState.ppGenerationTime.inWholeSeconds} sec")
            if (ageProofUiState.qrCodeScanStatus == QrCodeScanStatus.SCAN_SUCCESS) {
                Text("Aadhaar QR code scanned")
                if (ageProofUiState.proofGenerationInProgress) {
                    Text("Proof generation in progress")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
                if (ageProofUiState.proofGenerated) {
                    if (ageProofUiState.proof != null) {
                        if (ageProofUiState.proof.success) {
                            Text("Proof generated in ${ageProofUiState.proofGenerationTime.inWholeSeconds} sec")
                        } else {
                            Text("Proof generation failed", color = Color.Red)
                            Text("Reason: ${ageProofUiState.proof.message}")
                        }
                    }
                }
            } else if (ageProofUiState.qrCodeScanStatus == QrCodeScanStatus.SCAN_FAILURE) {
                Text("Aadhaar QR code scan failed", color = Color.Red)
                Text("Please try again", color = Color.Red)
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
            {},
            {},
            modifier = Modifier.padding(16.dp)
        )
    }
}