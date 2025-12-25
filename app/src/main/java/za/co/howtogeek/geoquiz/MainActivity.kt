package za.co.howtogeek.geoquiz

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState



/*
enum class QuizQuestion(val textResId: Int, val answer: Boolean) {
    ; // The semicolon here is important
}
*/


private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        Log.d(TAG, "onCreate: Got a QuizViewModel: $quizViewModel")
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
                        // Pass the viewModel instance to QuizContent
                        QuizContent(quizViewModel = quizViewModel)
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
fun QuizContent(quizViewModel: QuizViewModel){
    val context = LocalContext.current

    // Collect the value from the StateFlow
    val currentQuestion by quizViewModel.currentQuestion.collectAsState()

    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Text(
            //text = stringResource(questionTextResId),
            text = stringResource(id = currentQuestion?.textResId ?:R.string.question_placeholder),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth()
                .clickable(true, onClick = {
                    quizViewModel.moveToNext()
                    //updateQuestion()
                })
        )
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // True Button
            Button(
                onClick = {
                    checkAnswer(true, currentQuestion?.answer ?: false, context)
                    quizViewModel.markQuestionAsAnswered()
                },
                enabled = currentQuestion?.answered == false,
            ) {
                Text(stringResource(string.trueButton))
            }

            // False Button
            Button(
                onClick = {
                    checkAnswer(false, currentQuestion?.answer ?: false, context)
                    quizViewModel.markQuestionAsAnswered()
                },
                enabled = currentQuestion?.answered == false
            ) {
                Text(
                    text = stringResource(string.falseButton)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous Button
            //if (quizViewModel.currentIndex > 0) {
                TextButton(
                    onClick = {
                        quizViewModel.moveToPrevious()
                        //updateQuestion()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(string.previousButton)
                    )
                    Text("Previous")
                }
            //}

            // Next Button
            TextButton(
                onClick = {
                    //currentIndex = (currentIndex +1) % questionBank.size
                    quizViewModel.moveToNext()
                    //updateQuestion()
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

    /*
    fun updateQuestion(){
        val questionTextResId = quizViewModel.currentQuestionText
    }
     */
}


// --- 4. Update checkAnswer function ---
// It no longer needs the whole Question object
fun checkAnswer(userAnswer: Boolean, correctAnswer: Boolean, context: android.content.Context) {
    val messageResId = if (userAnswer == correctAnswer) {
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