package com.devlog.article.data.response

import java.util.Date

data class UserInfoEntity(var status:Boolean,var data:UserData){
    fun toEntity(): UserInfoEntity =
        UserInfoEntity(
            status= status,
            data =data
        )
}

data class UserData(var uid:String,var email:String,var name:String,var keywords:Array<String>)
