package com.devlog.feature_sign_in

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignInSeen(login:()->Unit){

    SignInView(onClick = login)

}
@Composable
fun SignInView(onClick: () -> Unit){
    Column(modifier = Modifier.fillMaxSize(1f)) {
        GoogleSignInButton(onClick)
    }
}

@Preview
@Composable
fun GoogleSignInButton(
    onClick: () -> Unit={},
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_ai), // Google 로고 아이콘
            contentDescription = "Google Sign-In",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Sign in with Google",
            color = Color.Gray,
            fontSize = 16.sp
        )
    }
}