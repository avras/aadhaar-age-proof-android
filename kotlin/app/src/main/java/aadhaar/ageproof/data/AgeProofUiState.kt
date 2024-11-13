package aadhaar.ageproof.data

data class AgeProofUiState(
    val publicParameters: String = String(),
    val ppGenerated: Boolean = false,
    val ppGenerationInProgress: Boolean = false
)