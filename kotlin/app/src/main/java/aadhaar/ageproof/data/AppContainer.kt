package aadhaar.ageproof.data

interface AppContainer {
    val ageProofRepository: AgeProofRepository
}

class DefaultAppContainer() : AppContainer {
    override val ageProofRepository = CoroutinesAgeProofRepository()

}