package aadhaar.ageproof

import aadhaar.ageproof.ui.MainScreen
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val greeting = helloWorld()
//        val timeSource = TimeSource.Monotonic
//        val ppStartTime = timeSource.markNow()
//        val ppSizeInMB = (generatePublicParameters().size / (1024.0 * 1024.0)).toBigDecimal()
//            .setScale(2, RoundingMode.HALF_UP).toDouble()
//        val ppEndTime = timeSource.markNow()
//        val ppGenTime = ppEndTime - ppStartTime
        setContent {
            AadhaarAgeProofTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainScreen()
//                    Greeting(
//                        greeting = buildString {
//                            append(greeting)
//                            append("\nPublic Parameters size = ")
//                            append(ppSizeInMB)
//                            append(" MB\n")
//                            append("Time taken = ")
//                            append(ppGenTime.inWholeSeconds)
//                            append(" sec")
//                        },
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}