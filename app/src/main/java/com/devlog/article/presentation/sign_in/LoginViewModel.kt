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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel:ViewModel() {


    lateinit var loginSucceed :()->Unit
    lateinit var loginFailed :()->Unit


    fun login(uid:String,email:String,name:String):Job =viewModelScope.launch {
        val api= ApiService(
            provideProductRetrofit(
                buildOkHttpClient(),
                provideGsonConverterFactory()
            )
        )
        val repository: UserRepository = DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val response= repository.postLogin(LoginEntity(uid = uid , email =email ,name=name))

        if(response != null){
            loginSucceed()
        }else{
            loginFailed()
        }



    }


}