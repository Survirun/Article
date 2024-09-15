package com.devlog.article.presentation.sign_in

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.devlog.article.R
import com.devlog.article.databinding.ActivitySignInBinding
import com.devlog.article.data.preference.UserPreference
import com.devlog.article.presentation.my_keywords_select.MyKeywordSelectActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Base64
@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var userPreference : UserPreference
    private val loginViewModel:LoginViewModel by viewModels()
    private var activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes in this method
                val data: Intent? = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)

                } catch (e: ApiException) {
                    Log.d("polaris",e.toString())
                }

            }else{
                Log.d("polaris",result.resultCode.toString())
            }


        }

    lateinit var bindind:ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindind=ActivitySignInBinding.inflate(layoutInflater)
        setContentView(bindind.root)
        userPreference= UserPreference.getInstance(this)
        bindind.googleSignInButton.setOnClickListener{
            signIn()
        }
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        auth = Firebase.auth

        loginViewModel.loginSucceed = {

            userPreference.userSignInCheck = true

            startActivity(Intent(this, MyKeywordSelectActivity::class.java))
            finish()
        }
        loginViewModel.loginFailed ={
            Toast.makeText(this,"로그인 실패",Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent //구글로그인 페이지로 가는 인텐트 객체
        activityResultLauncher.launch(signInIntent)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun decodeToken(jwt: String): String {
        val parts = jwt.split(".")
        val charset = charset("UTF-8")
        val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
        return JSONObject(payload).getString("permission")
    }
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        Log.d("polaris","1")
                        userPreference.userUid=task.result.user!!.uid
                        // 로그인 성공
                        userPreference.userName=task.result.user!!.displayName.toString()

                        loginViewModel.login( userPreference.userUid,task.result.user!!.email.toString(),task.result.user!!.displayName.toString())



                    } else {
                        Log.d("polaris","2")
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
    }
}