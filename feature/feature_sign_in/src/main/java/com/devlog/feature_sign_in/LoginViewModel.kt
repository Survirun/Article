package com.devlog.feature_sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.date.entity.article.LoginEntity
import com.devlog.domain.usecase.user.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase
):ViewModel() {


    lateinit var loginSucceed :()->Unit
    lateinit var loginFailed :()->Unit


    fun login(uid:String,email:String,name:String):Job =viewModelScope.launch {

        postLoginUseCase.execute(
            LoginEntity(uid = uid , email =email ,name=name),
            onError = {
                loginFailed()
            },
            onException = {
                loginFailed()
            },
            onComplete = {
            }
        ).collect {
            loginSucceed()

        }




    }


}