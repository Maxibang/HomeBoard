package com.example.homeboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homeboard.ui.theme.HomeBoardTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeBoardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EditAndSendText(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun EditAndSendText(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var textState by remember { mutableStateOf("") }
    var explicitIntentDone by remember { mutableStateOf(true) }
    var phoneNumber by remember { mutableStateOf("") }
    var phoneCallEnabled by remember { mutableStateOf(false) }
    var shareText: String by remember { mutableStateOf(value = "") }
    var shareTextEnabled: Boolean by remember { mutableStateOf(value = false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val returnedIntent: Intent? = result.data
            val textFromDestroyedWindow: String? =
                returnedIntent?.getStringExtra("ExplicitIntent_KEY")

            if (textFromDestroyedWindow != null) {
                textState = textFromDestroyedWindow
            }

            explicitIntentDone = false
        }
    }



    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        // Explicit Intent
        Text("Explicit Intent")
        OutlinedTextField(
            value = textState,
            onValueChange = { newValue ->
                textState = newValue
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    "Add Explicit Intent Text",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            enabled = explicitIntentDone,
            modifier = Modifier.width(250.dp)
        )
        Button(
            onClick = {
                val explicitIntent = Intent(context, SecondActivity::class.java)
                explicitIntent.putExtra("ExplicitText", textState)
                launcher.launch(explicitIntent)
            },
            enabled = explicitIntentDone
        ) {
            Text("Send Explicit Intent")
        }

        // Emplicit Intent call a friend
        Spacer(Modifier.padding(top = 25.dp))
        Text("Emplicit Intent ACTION_DIAL")
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { newValue ->

                val filteredNumber  = newValue.filterIndexed { index, char ->
                    char.isDigit() || (char == '+' && index == 0)
                }
                val maxNumberLength = if (filteredNumber.startsWith('+')) 12 else 11
                if (filteredNumber.length <= maxNumberLength)
                {
                    phoneNumber = filteredNumber
                }
                phoneCallEnabled = phoneNumber.length == maxNumberLength
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    "Put phone number",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier.width(250.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Button(
            onClick = {
                if (phoneNumber.isBlank())
                    return@Button

                val implicitSystemIntent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:${phoneNumber.trim()}".toUri()
                }
                context.startActivity(implicitSystemIntent)
            },
            enabled = phoneCallEnabled
        ) {
            Text("Call number")
        }


        // Emplicit Intent call a friend
        Spacer(Modifier.padding(top = 25.dp))
        Text("Emplicit Intent ACTION_SEND")
        OutlinedTextField(
            value = shareText,
            onValueChange = { newValue ->
                shareText = newValue
                shareTextEnabled = !shareText.isBlank()
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            placeholder = {
                Text(
                    "Put Share Text",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            modifier = Modifier.width(250.dp)
        )

        Button(
            onClick = {
                val implicitSystemIntent = Intent(Intent.ACTION_SEND).apply{
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                val chooserIntent = Intent.createChooser(implicitSystemIntent, "Share via")
                context.startActivity(chooserIntent)
            },
            enabled = shareTextEnabled,
        ) {
            Text("Share")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeBoardTheme(content = ::EditAndSendText)
}
