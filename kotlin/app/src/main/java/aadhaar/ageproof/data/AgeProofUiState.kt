package aadhaar.ageproof.data

import com.google.gson.annotations.SerializedName
import uniffi.ageproof.AadhaarAgeProof
import uniffi.ageproof.AadhaarAgeVerifyResult
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
    val generatedProof: AadhaarAgeProof? = null,
    val proofVerified: Boolean = false,
    val proofVerificationInProgress: Boolean = false,
    val proofVerificationTime: Duration = 0.seconds,
    val verifyResult: AadhaarAgeVerifyResult? = null,
    val proofReadFromFile: Boolean = false,
    val receivedProof: AadhaarAgeProof? = null,
)

data class JsonAadhaarAgeProof(
    @SerializedName("version") val version: UInt,
    @SerializedName("pp_hash") val ppHash: String,
    @SerializedName("num_steps") val numSteps: UInt,
    @SerializedName("current_date_ddmmyyyy") val currentDateDdmmyyyy: String,
    @SerializedName("snark_proof") val snarkProof: String,
)

fun convertJsonAadhaarAgeProof(jsonProof: JsonAadhaarAgeProof): AadhaarAgeProof {
    return AadhaarAgeProof(
        success = false,
        message = "",
        version = jsonProof.version,
        ppHash = jsonProof.ppHash,
        numSteps = jsonProof.numSteps,
        currentDateDdmmyyyy = jsonProof.currentDateDdmmyyyy,
        snarkProof = jsonProof.snarkProof,
    )
}