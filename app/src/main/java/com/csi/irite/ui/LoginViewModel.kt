package com.csi.irite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.csi.irite.data.repo.AuthRepository
import kotlinx.coroutines.launch

// LoginViewModel.kt
class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> get() = _loginState

    /*fun login(username: String, password: String) {
        viewModelScope.launch {
            val success = repository.login(username, password)
            _loginState.postValue(success)
        }
    }*/

    fun getCachedUser() = liveData {
        emit(repository.getCachedUser())
    }
}
