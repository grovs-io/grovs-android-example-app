package io.grovs.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.grovs.Grovs
import io.grovs.utils.flow
import io.grovs.example.ui.theme.GrovsExampleAppTheme
import io.grovs.model.exceptions.GrovsException
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrovsExampleAppTheme {
                CenteredTextViewAndButton(viewModel)
            }
        }

        Grovs.setOnDeeplinkReceivedListener(this) { link, payload ->
            val message = "Got link from listener: $link payload: $payload"
            Log.d("MainActivity", message)
            viewModel.updateState(message)
        }

        lifecycleScope.launchWhenStarted {
            Grovs.Companion::openedLinkDetails.flow.collect { deeplinkDetails ->
                val message = "Got link from flow: ${deeplinkDetails?.link} payload: ${deeplinkDetails?.data}"
                Log.d("MainActivity", message)
            }
        }
    }
}

@Composable
fun CenteredTextViewAndButton(viewModel: MainViewModel) {
    // State for the text content
    val generatedLinkState = remember { mutableStateOf("") }
    val incomingLinkState by viewModel::incomingLinkState
    val context = LocalContext.current
    val activity = context as? MainActivity
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = generatedLinkState.value,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(onClick = {
//                Grovs.generateLink(title = "Test title",
//                    subtitle = "Test subtitle",
//                    imageURL = null,
//                    data = mapOf("param1" to "Test value"),
//                    tags = null,
//                    lifecycleOwner = activity,
//                    listener = { link, error ->
//                        link?.let { link ->
//                            generatedLinkState.value = link
//                        }
//                        error?.let { error ->
//                            generatedLinkState.value = error.toString()
//                        }
//                    })

                coroutineScope.launch {
                    try {
                        val link = Grovs.generateLink(
                            title = "Test title",
                            subtitle = "Test subtitle",
                            imageURL = null,
                            data = mapOf("param1" to "Test value"),
                            tags = null
                        )
                        generatedLinkState.value = link
                    } catch (e: GrovsException) {
                        generatedLinkState.value = e.toString()
                    }
                }
            }) {
                Text(text = "Generate link")
            }

            Text(
                text = incomingLinkState,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GrovsExampleAppTheme {
        CenteredTextViewAndButton(viewModel = MainViewModel())
    }
}