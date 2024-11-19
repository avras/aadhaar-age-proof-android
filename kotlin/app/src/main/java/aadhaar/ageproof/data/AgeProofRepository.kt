package aadhaar.ageproof.data

import uniffi.ageproof.AadhaarAgeProof
import uniffi.ageproof.AadhaarAgeVerifyResult

interface AgeProofRepository {
    suspend fun generatePublicParameters(): ByteArray

    suspend fun generateProof(
        ppBytes: ByteArray,
        qrData: ByteArray,
        dateBytes: ByteArray
    ): AadhaarAgeProof

    suspend fun verifyProof(
        ppBytes: ByteArray,
        proof: AadhaarAgeProof,
    ): AadhaarAgeVerifyResult

    fun decodeQrCode(s: String): Pair<QrCodeScanStatus, ByteArray?>
}