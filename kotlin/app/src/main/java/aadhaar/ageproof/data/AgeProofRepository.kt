package aadhaar.ageproof.data

interface AgeProofRepository {
    suspend fun generatePublicParameters(): ByteArray
    suspend fun generateProof(ppBytes: ByteArray, qrData: ByteArray, dateBytes: ByteArray): Boolean
    fun decodeQrCode(s: String): Pair<QrCodeScanStatus, ByteArray?>
}