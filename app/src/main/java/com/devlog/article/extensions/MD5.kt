package com.devlog.article.extensions

import java.security.MessageDigest
@OptIn(kotlin.ExperimentalStdlibApi::class)
internal fun String.toMD5():String{
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()

}