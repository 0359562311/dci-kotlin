package com.tankiem.kotlin.dci.app.presentation.login
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tankiem.kotlin.dci.utils.GlobalVariable
import com.tankiem.kotlin.dci.app.network.responses.Session
import com.tankiem.kotlin.dci.app.repository.AuthenticationRepository
import com.tankiem.kotlin.dci.utils.Result
import com.tankiem.kotlin.dci.utils.SharePreferenceUtils
import kotlinx.coroutines.*

class LoginViewModel: ViewModel(), Observable{
    private val _authenticationRepository: AuthenticationRepository = AuthenticationRepository()

    private val _loginState = MutableLiveData<LoginState>()
    val loginState: LiveData<LoginState>
        get() = _loginState

    @Bindable
    val username = MutableLiveData("")
    @Bindable
    val password = MutableLiveData("")

    private val _isLoading = MutableLiveData(false)
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            yieldState(LoginLoadingState)
            withContext(Dispatchers.IO) {
                delay(1000)
            }
            when (val result = _authenticationRepository.login(username.value!!, password.value!!)) {
                is Result.Success<*> -> {
                    GlobalVariable.session = (result.data as Session)
                    SharePreferenceUtils.addString("access", result.data.access)
                    SharePreferenceUtils.addString("refresh", result.data.refresh)
                    yieldState(LoginSuccessfulState)
                }
                is Result.Failure -> {
                    yieldState(LoginFailState(message = result.message))
                }
            }
        }
    }

    private fun yieldState(state: LoginState) {
        viewModelScope.launch(Dispatchers.Main) {
            _isLoading.value = state == LoginLoadingState
            _loginState.value = state
        }
    }

    private val callbacks: PropertyChangeRegistry by lazy { PropertyChangeRegistry()}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.add(callback)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callbacks.remove(callback)
    }
}

sealed interface LoginState

object LoginSuccessfulState : LoginState

object LoginLoadingState : LoginState

class LoginFailState(val message: String): LoginState