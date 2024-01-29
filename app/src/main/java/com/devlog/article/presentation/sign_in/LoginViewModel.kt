package com.devlog.article.presentation.sign_in

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devlog.article.data.entity.LoginEntity
import com.devlog.article.data.network.ApiService
import com.devlog.article.data.network.LoginBuildOkHttpClient
import com.devlog.article.data.network.buildOkHttpClient
import com.devlog.article.data.network.provideGsonConverterFactory
import com.devlog.article.data.network.provideProductRetrofit
import com.devlog.article.data.repository.DefaultRepository
import com.devlog.article.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

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
        val repository: Repository = DefaultRepository.getInstance(api, ioDispatcher = Dispatchers.IO)
        val serverConde= repository.postLogin(LoginEntity(uid = uid , email =email ,name=name))

        if (serverConde==200){
            loginSucceed()
        }else{
            loginFailed()
        }

    }


}