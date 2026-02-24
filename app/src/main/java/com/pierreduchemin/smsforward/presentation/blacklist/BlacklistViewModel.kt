package com.pierreduchemin.smsforward.presentation.blacklist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pierreduchemin.smsforward.data.GlobalModelRepository
import com.pierreduchemin.smsforward.data.source.database.GlobalModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlacklistViewModel @Inject constructor(
    private val globalModelRepository: GlobalModelRepository
) : ViewModel() {

    val blacklistText = MutableLiveData<String>()
    val isSaved = MutableLiveData<Boolean>()
    val isLoaded = MutableLiveData<Boolean>(false)

    private var globalModel: GlobalModel? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            globalModel = globalModelRepository.getGlobalModel()
            blacklistText.postValue(globalModel?.blacklist ?: "")
            isLoaded.postValue(true)
        }
    }

    fun onSaveClicked(blacklist: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentGlobalModel = globalModel ?: GlobalModel(1)
            currentGlobalModel.blacklist = blacklist
            if (globalModel == null) {
                globalModelRepository.insertGlobalModel(currentGlobalModel)
            } else {
                globalModelRepository.updateGlobalModel(currentGlobalModel)
            }
            isSaved.postValue(true)
        }
    }
}
