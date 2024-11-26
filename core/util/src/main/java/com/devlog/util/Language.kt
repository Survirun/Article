package com.devlog.util


fun String.isProbablyEnglish(): Boolean {
    var i = 0
    while (i < this.length) {
        val c = this.codePointAt(i)
        if (c in 0x0041..0x007A)
            return true
        i += Character.charCount(c)
    }
    return false
}

fun String.isProbablyKorean(): Boolean {
    var i = 0
    while (i < this.length) {
        val c = this.codePointAt(i)
        if (c in 0xAC00..0xD800)
            return true
        i += Character.charCount(c)
    }
    return false
}


