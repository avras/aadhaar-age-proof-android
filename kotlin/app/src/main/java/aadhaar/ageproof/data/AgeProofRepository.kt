package aadhaar.ageproof.data

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface AgeProofRepository {
    val outputWorkInfo: Flow<WorkInfo>
    fun generatePublicParameters()
    fun cancelPublicParameterGeneration()
}