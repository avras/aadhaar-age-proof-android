package aadhaar.ageproof.data

import uniffi.ageproof.AadhaarAgeProof

interface AgeProofRepository {
    suspend fun generatePublicParameters(): ByteArray
    suspend fun generateProof(
        ppBytes: ByteArray,
        qrData: ByteArray,
        dateBytes: ByteArray
    ): AadhaarAgeProof

    fun decodeQrCode(s: String): Pair<QrCodeScanStatus, ByteArray?>
}