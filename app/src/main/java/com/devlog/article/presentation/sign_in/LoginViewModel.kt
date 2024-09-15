package com.devlog.article.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit

import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.UserRepository
import com.devlog.article.domain.usecase.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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

        postLoginUseCase.execute(LoginEntity(uid = uid , email =email ,name=name),
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