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

private const val TAG = "PublicParametersWorker"

class PublicParametersWorker(ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        makeStatusNotification(
            applicationContext.resources.getString(R.string.generating_public_parameters),
            applicationContext
        )

        return withContext(Dispatchers.IO) {
            delay(DELAY_TIME_MILLIS)

            return@withContext try {

                val outputData = workDataOf(KEY_PUBLIC_PARAMS to "Dummy Public Params")
                Result.success(outputData)

            } catch (throwable: Throwable) {
                Log.e(
                    TAG,
                    applicationContext.resources.getString(R.string.error_generating_public_parameters),
                    throwable
                )
                Result.failure()
            }
        }
    }
}