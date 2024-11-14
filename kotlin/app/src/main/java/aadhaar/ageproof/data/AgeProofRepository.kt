package aadhaar.ageproof.data

interface AgeProofRepository {
    suspend fun generatePublicParameters() : ByteArray
}