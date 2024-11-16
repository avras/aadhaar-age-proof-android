package aadhaar.ageproof.data

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class QrCodeScanStatus {
    NOT_SCANNED,
    SCAN_SUCCESS,
    SCAN_FAILURE,
}

data class AgeProofUiState(
    val publicParameters: ByteArray = byteArrayOf(),
    val ppGenerated: Boolean = false,
    val ppGenerationInProgress: Boolean = false,
    val ppGenerationTime: Duration = 0.seconds,
    val qrCodeData: ByteArray = byteArrayOf(),
    val qrCodeScanStatus: QrCodeScanStatus = QrCodeScanStatus.NOT_SCANNED,
    val proofGenerated: Boolean = false,
    val proofGenerationInProgress: Boolean = false,
    val proofGenerationTime: Duration = 0.seconds,
    val proofValid: Boolean = false,
)