package com.example.homeboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.homeboard.ui.theme.HomeBoardTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val receivedText = intent.getStringExtra("ExplicitText") ?: "Текст не пришел"
        setContent {
            HomeBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExplicitIntentResult(textFromParentWindow=receivedText,
                        modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ExplicitIntentResult(textFromParentWindow: String, modifier: Modifier = Modifier)
{
    val context = LocalContext.current
    val activity = context as android.app.Activity

    Column(
        modifier=modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = textFromParentWindow,
        )

        Button(
            onClick = {
                val returnIntent = Intent()
                returnIntent.putExtra("ExplicitIntent_KEY", "Successful!")
                activity.setResult(RESULT_OK, returnIntent)
                activity.finish()
            }
        ) {
            Text("Go Home")
        }
    }

}


@Preview(showBackground = true)
@Composable
fun SecondGreetingPreview() {
    HomeBoardTheme {
        ExplicitIntentResult(textFromParentWindow = "test")
    }
}
