package aadhaar.ageproof.data

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class AgeProofUiState(
    val publicParameters: ByteArray = byteArrayOf(),
    val ppGenerated: Boolean = false,
    val ppGenerationInProgress: Boolean = false,
    val ppGenerationTime: Duration = 0.seconds,
)