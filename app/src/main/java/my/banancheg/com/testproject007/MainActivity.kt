package my.banancheg.com.testproject007

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

import com.google.firebase.auth.AuthResult
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.support.v4.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential






class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.button_login-> {
                if(!editText_email.text.toString().isEmpty() && !editText_password.text.toString().isEmpty()){
                auth.signInWithEmailAndPassword(editText_email.text.toString(), editText_password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            Toast.makeText(this, "signInWithEmail:success", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }

                        // ...
                    }
            }
            R.id.button_logout -> {
                auth.signOut()
                Log.d(TAG, "logout")
            }

            R.id.sign_in_button ->{
                val signInIntent = mGoogleSignInClient.getSignInIntent()
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
           // R.id.button_register ->
        }
    }

    val RC_SIGN_IN = 777
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val TAG = "myTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        button_login.setOnClickListener(this)
        button_logout.setOnClickListener(this)
        sign_in_button.setOnClickListener(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("862149992697-l7tnasiinusfcbrhrgk1oq54n5pj51ll.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val user = auth.currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = user.uid

            Log.d(TAG, email)


            val account = GoogleSignIn.getLastSignedInAccount(this)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }

        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithGOOGLECredential:success")
                    val user = auth.getCurrentUser()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithGOOGLECredential:failure", task.exception)

                }

                // ...
            })
    }


}
