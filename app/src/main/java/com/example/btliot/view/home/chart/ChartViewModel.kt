package com.example.btliot.view.home.chart

import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.btliot.MyApp
import com.example.btliot.R
import com.example.btliot.common.StatusLevel
import com.example.btliot.database.AppDatabase
import com.example.btliot.databinding.FragmentChartBinding
import com.example.btliot.model.LogData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChartViewModel(
    private val chartRepository: ChartRepository?,
    private val database: AppDatabase
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ChartViewModel(
                    MyApp.getInstance().appContainer.chartRepository,
                    MyApp.getInstance().database
                ) as T
            }
        }
    }

    fun start() {
        viewModelScope.launch(Dispatchers.Default) {
            chartRepository?.start(
                updateDataListener = object : ChartRepository.UpdateDataListener {
                    override fun onUpdateTemperature(temperature: Float) {
                        onUpdateTemperatureHandler(temperature)
                    }

                    override fun onUpdateGasRate(gasRate: Int) {
                        onUpdateGasRateHandler(gasRate)
                    }

                    override fun onUpdateIsFire(isFire: Boolean) {
                        onUpdateFireStatusHandler(isFire)
                    }

                    override fun onUpdateIsGasLeak(isGasLeak: Boolean) {
                        onUpdateGasLeakStatusHandler(isGasLeak)
                    }
                }
            )
        }
    }

    private fun onUpdateTemperatureHandler(temperature: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            database.logDao().insert(
                LogData(
                    id = null,
                    content = "Update Temperature: $temperature",
                    isFromFirebase = true
                )
            )
        }
    }

    private fun onUpdateGasRateHandler(gasRate: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            database.logDao().insert(
                LogData(
                    id = null,
                    content = "Update Gas: $gasRate",
                    isFromFirebase = true
                )
            )
        }
    }


    private fun onUpdateFireStatusHandler(isFire: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            database.logDao().insert(
                LogData(
                    id = null,
                    content = "Update Fire State: $isFire",
                    isFromFirebase = true
                )
            )
        }
    }

    private fun onUpdateGasLeakStatusHandler(isGasLeak: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            database.logDao().insert(
                LogData(
                    id = null,
                    content = "Update Gas Leaking State: $isGasLeak",
                    isFromFirebase = true
                )
            )
        }
    }

    suspend fun observeData(binding: FragmentChartBinding, lifecycleOwner: LifecycleOwner) {
        chartRepository?.temperatureRecord?.observe(lifecycleOwner) {
            binding.tvTemperature.text = it.toString()

            binding.cvTemperature.setCardBackgroundColor(
                getStatusColor(chartRepository.getTemperatureStatus(temperature = it))
            )
        }

        chartRepository?.gasRecord?.observe(lifecycleOwner) {
            binding.tvGas.text = it.toString()

            binding.cvGas.setCardBackgroundColor(
                getStatusColor(chartRepository.getGasRateStatus(gas = it))
            )
        }

        chartRepository?.isFire?.observe(lifecycleOwner) {
            binding.tvStatus.text =
                chartRepository.getStatusString(isFire = it)

            binding.cvStatus.setCardBackgroundColor(
                getStatusColor(chartRepository.getAvgStatus(isFire = it))
            )
        }

        chartRepository?.isGasLeak?.observe(lifecycleOwner) {
            binding.tvStatus.text =
                chartRepository.getStatusString(isGasLeak = it)

            binding.cvStatus.setCardBackgroundColor(
                getStatusColor(chartRepository.getAvgStatus(isGasLeak = it))
            )
        }
    }

    private fun getStatusColor(statusLevel: StatusLevel): Int {
        return ContextCompat.getColor(
            MyApp.context(),
            when (statusLevel) {
                StatusLevel.NORMAL -> R.color.color_status_normal
                StatusLevel.WARNING -> R.color.color_status_warning
                StatusLevel.DANGER -> R.color.color_status_dangerous
            }
        )
    }
}