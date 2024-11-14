package aadhaar.ageproof.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class CoroutinesAgeProofRepository() : AgeProofRepository {

    override suspend fun generatePublicParameters() : String {

        return withContext(Dispatchers.IO) {
            delay(5000L)
            "Dummy Public Parameters".toString()
        }

    }

}