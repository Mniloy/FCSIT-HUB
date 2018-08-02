package my.unimas.a54440siswa.fcsithub;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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


public class ForgotPassword extends AppCompatActivity {

    EditText ETEmail;
    Button BTNResetPassword;
    TextView TVSigninfp, TVResetMessage;

    /*---------------------------------- Firebase Database ---------------------------------------*/
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);



        ETEmail = (EditText) findViewById(R.id.etemailfp);
        BTNResetPassword =(Button)  findViewById(R.id.btnresetpassword);
        TVSigninfp = (TextView) findViewById(R.id.tvsigninfp);
        TVResetMessage=(TextView) findViewById(R.id.tvresetmessage);



        myAuth = FirebaseAuth.getInstance();

        TVSigninfp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (ForgotPassword.this, SignIn.class);

                startActivity(intent);
            }

        });



        BTNResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = ETEmail.getText().toString().trim();

                     if (ETEmail.getText().toString().length() == 0) {
                            ETEmail.setError("Email is needed!");
                        }else {
                         myAuth.sendPasswordResetEmail(email)

                                 .addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                         if (task.isSuccessful()) {
                                             TVResetMessage.setText("We have sent you instructions to reset your password to the Email ID: " + email);

                                         } else {
                                             TVResetMessage.setText("The entered email " + email + " is invalid!");

                                         }
                                     }

                                 });
                     }
            }
        });


        myAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null){
                    startActivity(new Intent(ForgotPassword.this, HomeActivity.class));
                }
            }
        };

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        myGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                        Toast.makeText(ForgotPassword.this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


}
