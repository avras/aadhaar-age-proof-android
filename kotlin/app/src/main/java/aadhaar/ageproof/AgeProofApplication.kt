package aadhaar.ageproof

import aadhaar.ageproof.data.AppContainer
import aadhaar.ageproof.data.DefaultAppContainer
import android.app.Application

class AgeProofApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}