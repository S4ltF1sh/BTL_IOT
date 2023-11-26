package com.example.btliot.view.home.chart

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.btliot.MyApp
import com.example.btliot.databinding.FragmentChartBinding
import com.example.btliot.base.BaseFragment
import kotlinx.coroutines.launch

class ChartFragment : BaseFragment<FragmentChartBinding>(FragmentChartBinding::inflate) {
    private val viewModel by viewModels<ChartViewModel>(factoryProducer = { ChartViewModel.Factory })
    private var speechRecognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.start()
    }

    override fun initView() {
        setupToolbarVisible(false)
        super.initView()

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MyApp.context())
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                Toast.makeText(MyApp.context(), "Listening...", Toast.LENGTH_SHORT).show()
            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(bundle: Bundle?) {
                val result =
                    bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)

                if (result?.contains("turn on") == true) {
                    viewBinding.swAirConditioner.isChecked = true
//                    Toast.makeText(MyApp.context(), result ?: "nothing", Toast.LENGTH_SHORT)
//                        .show()
                } else if (result?.contains("turn off") == true) {
                    viewBinding.swAirConditioner.isChecked = false
//                    Toast.makeText(MyApp.context(), result ?: "nothing", Toast.LENGTH_SHORT)
//                        .show()
                } else {
                    Toast.makeText(MyApp.context(), "Please try again", Toast.LENGTH_SHORT)
                        .show()
                }


            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })
    }

    override fun initAction() {
        super.initAction()
        viewModel.setSwitchActions(viewBinding)
        viewModel.setOpenMicAction(viewBinding) { isStop ->
            if (!isStop) {
                speechRecognizer?.stopListening()
            } else {
                speechRecognizer?.startListening(getSpeechRecognizerIntent())
            }
        }
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observeData(viewBinding, this@ChartFragment)
            }
        }
    }

    override val backPressCallback: () -> Boolean
        get() = {
            true
        }

    private fun getSpeechRecognizerIntent(): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")

        return intent
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }
}