package com.example.eyickanak;

/*Project Name: Kanak-Smart Composting
        Author List : Varad Gorhe ,Team Leader
        Filename    : SplashActivity.java
        Global Variables:None
        Functions   : postDelayed()
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {



    /*
     *
     * Function Name: 	postDelayed()
     * Input        : 	-
     * Output       : 	-
     * Logic        : 	A delay given of 3 seconds .This is the SplashScreen of the app.The next activity will open after the specified delay.
     * Example Call :	new Handler().postDelayed();
     *
     */


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    Intent openMainActivity =  new Intent(SplashActivity.this,Introduction.class);
                    startActivity(openMainActivity);
                    finish();

                }
            }, 3000);
        }
    }

