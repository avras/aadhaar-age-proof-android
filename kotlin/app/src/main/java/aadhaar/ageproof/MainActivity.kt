package aadhaar.ageproof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import aadhaar.ageproof.ui.theme.AadhaarAgeProofTheme
import uniffi.ageproof.*
import java.math.RoundingMode
import kotlin.time.TimeSource

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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

@Composable
fun Greeting(greeting: String, modifier: Modifier = Modifier) {
    Text(
        text = greeting,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AadhaarAgeProofTheme {
        Greeting("Hello Android")
    }
}