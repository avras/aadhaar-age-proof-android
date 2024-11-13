package aadhaar.ageproof.workers

import aadhaar.ageproof.DELAY_TIME_MILLIS
import aadhaar.ageproof.KEY_PUBLIC_PARAMS
import aadhaar.ageproof.R
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private const val TAG = "ResetPublicParametersWorker"

class ResetPublicParametersWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        makeStatusNotification(
            applicationContext.resources.getString(R.string.resetting_public_parameters),
            applicationContext
        )

        return withContext(Dispatchers.Default) {

            return@withContext try {

                val outputData = workDataOf(KEY_PUBLIC_PARAMS to "Reset public parameters")
                Result.success(outputData)

            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_resetting_public_parameters),
                    throwable
                )
                Result.failure()
            }
        }
    }
}