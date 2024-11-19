package aadhaar.ageproof.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uniffi.ageproof.AadhaarAgeProof
import uniffi.ageproof.AadhaarAgeVerifyResult
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.math.BigInteger
import java.util.zip.GZIPInputStream

class AgeProofRepositoryImpl() : AgeProofRepository {

    override suspend fun generatePublicParameters(): ByteArray {
        return withContext(Dispatchers.Default) {
            uniffi.ageproof.generatePublicParameters()
        }
    }

    override suspend fun generateProof(
        ppBytes: ByteArray,
        qrData: ByteArray,
        dateBytes: ByteArray
    ): AadhaarAgeProof {
        return withContext(Dispatchers.Default) {
            uniffi.ageproof.generateProof(
                ppBytes = ppBytes,
                qrDataBytes = qrData,
                currentDateBytes = dateBytes
            )
        }
    }

    override suspend fun verifyProof(
        ppBytes: ByteArray,
        proof: AadhaarAgeProof,
    ): AadhaarAgeVerifyResult {
        return withContext(Dispatchers.Default) {
            uniffi.ageproof.verifyProof(
                ppBytes = ppBytes,
                aadhaarAgeProof = proof,
            )
        }
    }

    override fun decodeQrCode(s: String): Pair<QrCodeScanStatus, ByteArray?> {
        val outputStream = ByteArrayOutputStream()
        try {
            val qrCompressedData = BigInteger(s, 10).toByteArray()
            val inputStream = ByteArrayInputStream(qrCompressedData)
            val gis = GZIPInputStream(inputStream)
            val buffer = ByteArray(8192)
            var size = 0
            while (size >= 0) {
                size = gis.read(buffer, 0, buffer.size)
                if (size > 0) {
                    outputStream.write(buffer, 0, size)
                }
            }

            gis.close()
            inputStream.close()
            outputStream.close()
            return Pair(QrCodeScanStatus.SCAN_SUCCESS, outputStream.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
            return Pair(QrCodeScanStatus.SCAN_FAILURE, null)
        }
    }

}