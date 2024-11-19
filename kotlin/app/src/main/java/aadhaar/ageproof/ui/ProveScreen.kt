package aadhaar.ageproof.ui

import aadhaar.ageproof.R
import aadhaar.ageproof.data.AgeProofUiState
import aadhaar.ageproof.data.QrCodeScanStatus
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.BarcodeFormat
import io.github.g00fy2.quickie.config.ScannerConfig
import java.io.FileNotFoundException


@Composable
fun ProveScreen(
    ageProofUiState: AgeProofUiState,
    setQrCodeBytes: (String) -> Unit,
    resetQrCodeBytes: () -> Unit,
    generateProof: (Context) -> Unit,
    resetProof: () -> Unit,
    shareProof: (Context) -> Unit,
    getProofJsonBytes: (Context) -> ByteArray,
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

    val context = LocalContext.current
    val imageFileChooserLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            if (uri != null) {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val width = bitmap.width
                    val height = bitmap.height
                    val pixels = IntArray(width * height)
                    bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                    bitmap.recycle()
                    val source = RGBLuminanceSource(width, height, pixels)
                    val bBitmap = BinaryBitmap(HybridBinarizer(source))
                    val reader = MultiFormatReader()
                    try {
                        val result = reader.decode(bBitmap)
                        val resultText = result?.text ?: ""
                        setQrCodeBytes(resultText)
                    } catch (e: NotFoundException) {
                        setQrCodeBytes(String())
                        e.printStackTrace()
                    }
                } catch (e: FileNotFoundException) {
                    setQrCodeBytes(String())
                    e.printStackTrace()
                } catch (e: NullPointerException) {
                    setQrCodeBytes(String())
                    e.printStackTrace()
                }
            } else {
                setQrCodeBytes(String())
            }
        }
    )

    val saveFileLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("application/json"),
            onResult = { uri: Uri? ->
                uri?.let {
                    context.contentResolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(getProofJsonBytes(context))
                    }
                }
            })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val scanButtonEnabled = !ageProofUiState.proofGenerationInProgress
            Button(
                onClick = {
                    resetQrCodeBytes()
                    resetProof()
                    scanQrCodeLauncher.launch(scannerConfig)
                },
                enabled = scanButtonEnabled,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_qrcode),
                    contentDescription = "Image icon",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Scan QR code")
            }
            Text(
                "or",
                Modifier.padding(4.dp)
            )
            Button(
                onClick = {
                    resetQrCodeBytes()
                    resetProof()
                    imageFileChooserLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                enabled = scanButtonEnabled,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "Image icon",
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Choose image")
            }
        }

        Button(
            onClick = {
                resetProof()
                generateProof(context)
            },
            enabled = ageProofUiState.qrCodeData.isNotEmpty() &&
                    !ageProofUiState.ppGenerationInProgress &&
                    !ageProofUiState.proofGenerationInProgress,
        ) {
            Text("Generate Proof")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = { saveFileLauncher.launch("aadhaar-age-proof.json") },
                enabled = ageProofUiState.proof != null &&
                        ageProofUiState.ppGenerated &&
                        !ageProofUiState.proofGenerationInProgress,
            ) {
                Text("Save Proof")
            }
            Text(
                "or",
                Modifier.padding(8.dp)
            )
            Button(
                onClick = { shareProof(context) },
                enabled = ageProofUiState.proof != null &&
                        ageProofUiState.ppGenerated &&
                        !ageProofUiState.proofGenerationInProgress,
            ) {
                Text("Share Proof")
            }
        }

        if (ageProofUiState.qrCodeScanStatus == QrCodeScanStatus.SCAN_SUCCESS) {
            Text("Aadhaar QR code scanned")
            if (ageProofUiState.ppGenerated) {
                Text("Public parameters generated in ${ageProofUiState.ppGenerationTime.inWholeSeconds} sec")
                if (ageProofUiState.proofGenerationInProgress) {
                    Text("Proof generation in progress")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
                else if (ageProofUiState.proofGenerated) {
                    if (ageProofUiState.proof != null) {
                        if (ageProofUiState.proof.success) {
                            Text("Proof generated in ${ageProofUiState.proofGenerationTime.inWholeSeconds} sec")
                        } else {
                            Text("Proof generation failed", color = Color.Red)
                            Text("Reason: ${ageProofUiState.proof.message}")
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

        } else if (ageProofUiState.qrCodeScanStatus == QrCodeScanStatus.SCAN_FAILURE) {
            Text("Aadhaar QR code scan failed", color = Color.Red)
            Text("Please try again", color = Color.Red)
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
            { byteArrayOf() },
            modifier = Modifier.padding(16.dp)
        )
    }
}