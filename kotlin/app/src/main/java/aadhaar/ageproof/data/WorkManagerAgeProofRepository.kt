package aadhaar.ageproof.data

import aadhaar.ageproof.PUBLIC_PARAMETERS_GEN_WORK_NAME
import aadhaar.ageproof.PUBLIC_PARAMETERS_RESET_WORK_NAME
import aadhaar.ageproof.TAG_GEN_PP
import aadhaar.ageproof.TAG_OUTPUT
import aadhaar.ageproof.TAG_RESET_PP
import aadhaar.ageproof.workers.PublicParametersWorker
import aadhaar.ageproof.workers.ResetPublicParametersWorker
import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class WorkManagerAgeProofRepository(context: Context) : AgeProofRepository {
    private val workManager = WorkManager.getInstance(context)

    override val outputWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT).asFlow().mapNotNull {
            if (it.isNotEmpty()) it.first() else null
        }

    override fun generatePublicParameters() {

        val ppGen = OneTimeWorkRequestBuilder<PublicParametersWorker>()
            .addTag(TAG_OUTPUT)
            .addTag(TAG_GEN_PP)
            .build()
        val continuation = workManager.beginUniqueWork(
            PUBLIC_PARAMETERS_GEN_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            ppGen,
        )
        continuation.enqueue()
    }

    override fun resetPublicParameters() {
        val resetPp =
            OneTimeWorkRequestBuilder<ResetPublicParametersWorker>()
                .addTag(TAG_OUTPUT)
                .addTag(TAG_RESET_PP)
                .build()
        val continuation = workManager.beginWith(
            resetPp
        )
        continuation.enqueue()
    }

    override fun cancelPublicParameterGeneration() {
        workManager.cancelUniqueWork(PUBLIC_PARAMETERS_GEN_WORK_NAME)
    }
}