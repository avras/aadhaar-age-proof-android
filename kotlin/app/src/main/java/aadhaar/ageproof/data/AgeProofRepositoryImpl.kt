package aadhaar.ageproof.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AgeProofRepositoryImpl() : AgeProofRepository {

    override suspend fun generatePublicParameters() : ByteArray {
        return withContext(Dispatchers.IO) {
            uniffi.ageproof.generatePublicParameters()
        }
    }

}