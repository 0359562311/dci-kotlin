package com.tankiem.kotlin.dci.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tankiem.kotlin.dci.utils.GlobalVariable
import com.tankiem.kotlin.dci.app.network.responses.Student
import com.tankiem.kotlin.dci.app.repository.UserRepository
import com.tankiem.kotlin.dci.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingViewModel: ViewModel() {
    private val userRepository = UserRepository()

    init {
        if(GlobalVariable.currentUser == null && GlobalVariable.session != null) {
            yieldState(SettingLoadingState)
            getUserProfile()
        } else {
            yieldState(SettingCompleteState)
        }
    }

    // state management
    private val _stateStream = MutableLiveData<SettingState>()
    val stateStream: LiveData<SettingState>
        get() = _stateStream

    // for loading avatar with Glide
    private val _imageSource = MutableLiveData<String>()
    val imageSource: LiveData<String>
        get() = _imageSource

    // bind user name
    private val _studentName = MutableLiveData("")
    val studentName: LiveData<String>
        get() = _studentName

    // bind user info
    private val _studentInfo = MutableLiveData("")
    val studentInfo: LiveData<String>
        get() = _studentInfo

    // responsive to user press
    private val _isPressing = listOf(
        MutableLiveData(false),
        MutableLiveData(false),
        MutableLiveData(false),
        MutableLiveData(false),
        MutableLiveData(false),
    )
    val isPressing: List<LiveData<Boolean>>
        get() = _isPressing

    private fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                delay(1000)
            }
            when(val res = userRepository.getMyProfile()) {
                is Result.Success<*> -> {
                    val myUser = res.data as Student
                    print(myUser)
                    GlobalVariable.currentUser = myUser
                    viewModelScope.launch (Dispatchers.Main){
                        _imageSource.value = myUser.avatar
                        _studentName.value = myUser.name
                        _studentInfo.value = "MSV: ${myUser.studentId}   Lớp: ${myUser.administrativeClass.administrativeClassId}"
                        yieldState(SettingCompleteState)
                    }
                }
                is Result.Failure -> {
                    yieldState(SettingErrorState(res.message))
                }
            }
        }
    }

    private fun yieldState(state: SettingState) {
        viewModelScope.launch(Dispatchers.Main){
            _stateStream.value = state
        }
    }

    fun onPress(pos : Int) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _isPressing[pos].value = true
            }
            delay(200)
            withContext(Dispatchers.Main) {
                _isPressing[pos].value = false
            }
        }
    }
}

sealed class SettingState
object SettingLoadingState: SettingState()
object SettingCompleteState: SettingState()
data class SettingErrorState(val message: String): SettingState()
