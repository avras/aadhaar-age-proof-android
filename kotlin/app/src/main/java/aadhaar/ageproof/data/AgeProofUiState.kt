package aadhaar.ageproof.data

data class AgeProofUiState(
    val publicParameters: ByteArray = byteArrayOf(),
    val ppGenerated: Boolean = false,
    val ppGenerationInProgress: Boolean = false
)