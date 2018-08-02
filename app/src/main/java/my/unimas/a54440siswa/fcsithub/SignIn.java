package my.unimas.a54440siswa.fcsithub;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {


Button BTNSignIn,  BTNGSignIn;
EditText ETEmail, ETPassword;
TextView BTNSignUp, TVForgotPassword;

//Firebase Database
FirebaseAuth myAuth;
private final static int RC_SIGN_IN = 54440;
GoogleApiClient myGoogleApiClient;
FirebaseAuth.AuthStateListener myAuthListener;


@Override
protected void onStart() {
super.onStart();
myAuth.addAuthStateListener(myAuthListener);
}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_sign_in);

//Defineing UI Element
    BTNSignIn = (Button) findViewById(R.id.BTNsignin);
    BTNGSignIn = (Button) findViewById(R.id.BTNGSignIn);
    BTNSignUp= (TextView) findViewById(R.id.tvsignup);
    ETEmail = (EditText) findViewById(R.id.ETemail);
    ETPassword = (EditText) findViewById(R.id.ETpassword);
    TVForgotPassword= (TextView) findViewById(R.id.tvforgotpassword);




myAuth = FirebaseAuth.getInstance();

    BTNSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if( ETEmail.getText().toString().trim().length() == 0 || ETPassword.getText().toString().trim().length() <= 5) {
                ETEmail.setError( "Email is needed!" );
                ETEmail.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                ETPassword.setError("At least 6 Characters  ");
                ETPassword.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
            }
            else{
                Sign_In_User( ETEmail.getText().toString().trim(),ETPassword.getText().toString().trim());
            }
        }
    });

BTNSignUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent signupintent = new Intent (SignIn.this, SignUp.class);
        startActivity(signupintent);
    }
});

TVForgotPassword.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent forgotintent = new Intent (SignIn.this, ForgotPassword.class);
        startActivity(forgotintent);
    }
});


BTNGSignIn.setOnClickListener(new View.OnClickListener(){
    @Override

    public void onClick(View view){
        sign_In();
    }
});


myAuthListener = new FirebaseAuth.AuthStateListener() {
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if (firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(SignIn.this, HomeActivity.class));
        }
    }
};


GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();


myGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                Toast.makeText(SignIn.this, "Operation Failed!!", Toast.LENGTH_SHORT).show();
            }
        })
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
}


private void sign_In() {
Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(myGoogleApiClient);
startActivityForResult(signInIntent, RC_SIGN_IN);
}

private void Sign_In_User(String email, final String password) {
myAuth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful())
                {
                    Toast.makeText(SignIn.this,"Wrong Email and Password combination!", Toast.LENGTH_LONG).show();
                }
                else{
                    startActivity(new Intent(SignIn.this,HomeActivity.class));
                }
            }
        });
}


@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
super.onActivityResult(requestCode, resultCode, data);

if (requestCode == RC_SIGN_IN) {
    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
    if (result.isSuccess()) {
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
    } else {
        Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
    }
}
}


private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
myAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("TAG", "SignIn With Credential:Success");
                    FirebaseUser user = myAuth.getCurrentUser();

                    String userID = myAuth.getCurrentUser().getUid();
                    String userName = myAuth.getCurrentUser().getDisplayName();
                    String userEmail = myAuth.getCurrentUser().getEmail();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    Map newPost = new HashMap();
                    newPost.put("userName", userName);
                    newPost.put("userEmail", userEmail);
                    current_user_db.updateChildren(newPost);
                    // updateUI(user);
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.getException());
                    Toast.makeText(SignIn.this, "Authentication failed.",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
}

}


