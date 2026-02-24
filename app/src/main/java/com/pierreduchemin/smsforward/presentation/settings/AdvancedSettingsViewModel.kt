package com.pierreduchemin.smsforward.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierreduchemin.smsforward.data.GlobalModelRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import com.pierreduchemin.smsforward.data.ReplacementRuleRepository
import com.pierreduchemin.smsforward.data.source.database.GlobalModel
import com.pierreduchemin.smsforward.data.source.database.ReplacementRule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdvancedSettingsViewModel @Inject constructor(
    private val globalModelRepository: GlobalModelRepository,
    private val replacementRuleRepository: ReplacementRuleRepository
) : ViewModel() {

    val globalModel: LiveData<GlobalModel?> = globalModelRepository.observeGlobalModel()
    val replacementRules: LiveData<List<ReplacementRule>> = replacementRuleRepository.observeReplacementRules()

    private var prefixJob: Job? = null
    fun updatePrefix(prefix: String) {
        prefixJob?.cancel()
        prefixJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            val current = globalModelRepository.getGlobalModel() ?: return@launch
            if (current.prefix != prefix) {
                current.prefix = prefix
                globalModelRepository.updateGlobalModel(current)
            }
        }
    }

    private var suffixJob: Job? = null
    fun updateSuffix(suffix: String) {
        suffixJob?.cancel()
        suffixJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            val current = globalModelRepository.getGlobalModel() ?: return@launch
            if (current.suffix != suffix) {
                current.suffix = suffix
                globalModelRepository.updateGlobalModel(current)
            }
        }
    }

    fun addReplacementRule() {
        viewModelScope.launch(Dispatchers.IO) {
            replacementRuleRepository.insertReplacementRule(ReplacementRule(pattern = "", replacement = ""))
        }
    }

    private val replacementJobs = mutableMapOf<Long, Job>()
    fun updateReplacementRule(rule: ReplacementRule) {
        replacementJobs[rule.id]?.cancel()
        replacementJobs[rule.id] = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            replacementRuleRepository.insertReplacementRule(rule)
        }
    }

    fun deleteReplacementRule(rule: ReplacementRule) {
        viewModelScope.launch(Dispatchers.IO) {
            replacementRuleRepository.deleteReplacementRule(rule)
        }
    }
}
