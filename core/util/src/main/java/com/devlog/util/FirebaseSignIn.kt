package com.devlog.util

import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class GoogleSignInHelper(
    val activity: ComponentActivity,
    private val onSignInSuccess: (Task<AuthResult>) -> Unit,
    private val onSignInFailure: (Exception) -> Unit
) {
    private val googleSignInClient: GoogleSignInClient

    init {
        val googleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestIdToken("394588487276-ld7mlft9je0i5vq2afuc5bcgp5vhg4ru.apps.googleusercontent.com")
            .requestEmail()
            .build()

         googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
    }



    private val activityResultLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)
                } catch (e: ApiException) {
                    onSignInFailure(e)
                }
            } else {
                onSignInFailure(Exception("Google Sign-In canceled with code ${result.resultCode}"))
            }
        }

    fun startGoogleSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent //구글로그인 페이지로 가는 인텐트 객체

        activityResultLauncher.launch(signInIntent)
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {

                        onSignInSuccess(task)




                    } else {
                      onSignInFailure(Exception(task.exception))

                    }
                })
    }
}