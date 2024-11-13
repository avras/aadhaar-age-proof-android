package aadhaar.ageproof.data

import android.content.Context

interface AppContainer {
    val ageProofRepository: AgeProofRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    override val ageProofRepository = WorkManagerAgeProofRepository(context)

}