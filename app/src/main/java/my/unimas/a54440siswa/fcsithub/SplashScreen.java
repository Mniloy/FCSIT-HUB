package my.unimas.a54440siswa.fcsithub;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {
    private static int splashscreen_timeout = 4000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Object postDelayed;
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent SignInIntent = new Intent(SplashScreen.this, SignIn.class);
                startActivity(SignInIntent);
                finish();
            }
        }, splashscreen_timeout);

    }
}
