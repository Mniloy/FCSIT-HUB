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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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


public class SignUp extends AppCompatActivity
{
    // Declaration of UI elements

    Button BTNRegister;
    EditText ETEmail, ETPassword, ETPasswordConfirm;

    TextView TVSignIn;

    // Firebase Database
    FirebaseAuth myAuth;
    private final static int RC_SIGN_IN = 54440;
    GoogleApiClient myGoogleApiClient;
    FirebaseAuth.AuthStateListener myAuthListener;
    /*--------------------------------------------------------------------------------------------*/

    @Override
    protected void onStart() {
        super.onStart();
        myAuth.addAuthStateListener(myAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        myAuth = FirebaseAuth.getInstance();

        BTNRegister = (Button) findViewById(R.id.BTNRegister);

        ETPassword = (EditText) findViewById(R.id.ETpasswordReg);
        ETPasswordConfirm = (EditText) findViewById(R.id.ETCpasswordReg);

        TVSignIn = (TextView) findViewById(R.id.TVsignin);
        ETEmail=(EditText) findViewById(R.id.ETEmailReg);


        BTNRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              if (ETEmail.getText().toString().length() == 0) {
                    ETEmail.setError("Email is needed!");
                } else if (ETPassword.getText().toString().length() <= 5) {
                    ETPassword.setError("at least 6 Characters needed!");
                } else if (ETPassword.getText().toString().length() != ETPasswordConfirm.getText().toString().length()) {
                    ETPasswordConfirm.setError("Password does not match!");
                } else {
                    signUpUser(ETEmail.getText().toString(), ETPassword.getText().toString());
                }
            }

        });

        TVSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signinintent = new Intent (SignUp.this, SignIn.class);
                startActivity(signinintent);

            }
        });




        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null){
                    startActivity(new Intent(SignUp.this, SignIn.class));
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

                        Toast.makeText(SignUp.this, "Operation Failed!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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
                Toast.makeText(this, "Authentication failed!", Toast.LENGTH_SHORT).show();
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
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = myAuth.getCurrentUser();
                            String userID = myAuth.getCurrentUser().getUid();
                            String userName = myAuth.getCurrentUser().getDisplayName();
                            String userEmail = myAuth.getCurrentUser().getEmail();
                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users");
                            Map newPost = new HashMap();
                            newPost.put("userName", userName);
                            newPost.put("userEmail", userEmail);
                            current_user_db.updateChildren(newPost);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "Sign In With Credential:failure", task.getException());
                            Toast.makeText(SignUp.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                            // updateUI(null);
                        }
                        // ...
                    }
                });
    }


    public void signUpUser(final String userEmail, final String userPassword ) {

        myAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    ETEmail.setError("Email is not valid!");
                    Toast.makeText(SignUp.this,"Sign up Unsuccessful", Toast.LENGTH_LONG).show();
                } else {
                    String userID = myAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    Map newPost = new HashMap();
                    newPost.put("userEmail", userEmail);
                    newPost.put("userPassword", userPassword);
                    current_user_db.setValue(newPost);
                    Toast.makeText(SignUp.this, "Sign up Successful", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

}