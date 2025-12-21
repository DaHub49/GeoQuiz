package za.co.howtogeek.geoquiz

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import za.co.howtogeek.geoquiz.R.string
import za.co.howtogeek.geoquiz.ui.theme.GeoQuizTheme


enum class QuizQuestion(val textResId: Int, val answer: Boolean) {
    AUSTRALIA(R.string.question_australia, true),
    OCEANS(R.string.question_oceans, true),
    MIDEAST(R.string.question_mideast, false),
    AFRICA(R.string.question_africa, false),
    AMERICAS(R.string.question_americas, true),
    ASIA(R.string.question_asia, true); // The semicolon here is important
}

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        setContent {
            GeoQuizTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        QuizContent()
                    }
                }
            }
        }
    }

    // onStart
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    // onResume
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    // onPause
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    // onStop
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    // onDestroy
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

}

@Composable
fun QuizContent(){

    val context = LocalContext.current

    val questionBank = QuizQuestion.entries

    var currentIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            text = stringResource(questionBank[currentIndex].textResId),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .clickable(true, onClick = {
                    currentIndex = (currentIndex + 1) % questionBank.size
                })
        )
        Row(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {

            // True Button
            Button(
                onClick = {
                    checkAnswer(true, questionBank[currentIndex], context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                Text(stringResource(string.trueButton))
            }

            // False Button
            Button(
                onClick = {
                    checkAnswer(false, questionBank[currentIndex], context)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                Text(
                    text = stringResource(string.falseButton)
                )
            }
        }
        // Next Button
        /*Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                //Toast.makeText(context, "Next Question", Toast.LENGTH_SHORT).show()
                currentIndex = (currentIndex + 1) % questionBank.size
            }
        ) {
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentIndex > 0) {
                TextButton(
                    onClick = {
                        currentIndex = (currentIndex - 1) % questionBank.size
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(string.previousButton)
                    )
                    Text(text = if (currentIndex > 0) "Previous" else "")
                }
            } else {
                    // Add an empty Spacer to maintain the layout when the button is hidden
                    Spacer(Modifier.weight(1f))
                }
                TextButton(
                    onClick = {
                        currentIndex = (currentIndex +1) % questionBank.size
                    }
                ) {
                    Text("Next")
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(string.nextButton)
                    )
                }
            }
            //}
        }
    }

    fun checkAnswer(userAnswer: Boolean, currentQuestion: QuizQuestion, context: android.content.Context){
        val correctAnswer = currentQuestion.answer

        val messageResId = if (userAnswer == correctAnswer){
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun QuestionRows(modifier: Modifier = Modifier) {
        Text(text = stringResource(string.question_text))
    }